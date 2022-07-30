<template>
  <div class="chat">
  
      <div class="talk_con">
        <div class="talk_show" id="chatBox"   style="overflow:scroll">
          <ul>
            <li v-for='(item,index) in msgList' :key="index" class="Message"> {{ item.sender }} {{item.sendTimeStamp}} send: <br/>   <span> <button style="width:120px;height:30px;"  @click="translate(item)" >点击翻译成中文</button> {{ item.message }}</span> </li>
          </ul>
            <!-- <div class="atalk"><span id="asay">A说：吃饭了吗？</span></div>
            <div class="btalk"><span id="bsay">B说：还没呢，你呢？</span></div>  -->
            <!-- <div class="btalk"><span>{{myMsg}}</span></div> -->
        </div>
        <div class="talk_input">
            <input type="text" class="talk_word"  @keyup.enter="send()" id="talkwords" v-model="myMsg">
            <input type="button" value="发送" @click="send()" class="talk_sub" id="talksub">
            <input type="button" value="人员" @click="people()" class="talk_sub" id="talksub">
             <el-button type="primary" @click="record()"  style="margin-left:500px;margin-bottom:500px;width:70px;border:solid">聊天记录</el-button>
        </div>
      </div>

  </div>
</template>

<script>
import axios from "axios"
export default {
  name: 'ChatPage',
  data(){
    return {
      msgList:[],
      websocket:null,
      message:"mesgs for ",
      myMsg:"",
      displayInfo:"",
      userName:"",
      sendMeta:"",
      sendInfo:"",
      
      // Words:"",
      // Who:"",
      // talkWords:"",
    
    }
  },
  props: {
    msg: String
  },
  mounted(){
      this.userName = sessionStorage.getItem("username")
      if ('WebSocket' in window) {
        //用户 (from)
     //   console.log("ws://localhost:9000/websocket/"+this.from)
        this.websocket = new WebSocket("ws://localhost:9000/websocket/"+this.userName);//整合时：链接后端的websocket（作为controller），内置监听消息的onmessage（别的MQ给这个websocket的user发送消息） 每个user之间使用websocket进行mq的消息发送
       this.websocket.onclose =this.endWebSocket
    this.websocket.onmessage = this.onMessageWebSocket
    } else {
        alert('Not support websocket')
    }
  },
  methods: {
    getLocalTime(nS) {  //时间戳转换    
   return new Date(parseInt(nS) * 1000).toLocaleString().replace(/:\d{1,2}$/,' ');     
}, endWebSocket(){
      //console.log("关闭websocket")
      this.msgList = []
    },
    onMessageWebSocket(event){
      this.msgList.push(JSON.parse(event.data))
    },
record(){
 axios.get("http://localhost:9000/group/message").then((res)=>{
        this.msgList = res.data
        console.log(this.list)
        this.myMsg = ""
    })
},getCurTime(){
        return Date.now();
      },
   send(){
     var msg = this.myMsg
     var name = sessionStorage.getItem("username")
     var params = {
       "sender" : name,
       "message":msg,
       "receiver":"group",
       "sendTimeStamp":this.getCurTime().toString()
     }
     params = JSON.stringify(params)
     this.websocket.send(params)
     
   },
   translate(item){
     //var english = item.message 
      axios.post("http://localhost:9000/translate/en2zh",item.message ).then((res)=>{
        item.message = res.data
    })
   },
    people(){
      this.$router.push("PeopleChater")
    }


  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
//btalk: me     atalk:other
.Message{
   width:350px;
            height:100px;
            border:1px solid #666;
            background:#fff;
            margin:10px auto 0;
            overflow:auto;
}
  .talk_con{
            width:600px;
            height:500px;
            border:1px solid #666;
            margin:50px auto 0;
            background:#f9f9f9;
        }
        .talk_show{
            width:580px;
            height:420px;
            border:1px solid #666;
            background:#fff;
            margin:10px auto 0;
            overflow:auto;
        }
        .talk_input{
            width:580px;
            margin:10px auto 0;
        }
        .whotalk{
            width:80px;
            height:30px;
            float:left;
            outline:none;
        }
        .talk_word{
            width:420px;
            height:26px;
            padding:0px;
            float:left;
            margin-left:10px;
            outline:none;
            text-indent:10px;
        }        
        .talk_sub{
            width:56px;
            height:30px;
            float:left;
            margin-left:10px;
        }
        .atalk{
           margin:10px; 
           text-align:left;
        }
        .atalk span{
            display:inline-block;
            background:#0181cc;
            border-radius:10px;
            color:#fff;
            padding:5px 15px;
        }
        .btalk{
            margin:10px;
            text-align:right;
        }
        .btalk span{
            display:inline-block;
            background:#ef8201;
            border-radius:10px;
            color:#fff;
            padding:5px 10px;
        }
</style>
