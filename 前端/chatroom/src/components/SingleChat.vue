<template>
  <div class="singlechat">
      <h1 style="text-align:center">{{ from }} and {{ to }} is chatting:<br> </h1>

     <div class="talk_con">
        <div class="talk_show" id="chatBox"   style="overflow:scroll">
          <ul>
            <li v-for='(item,index) in msgList' :key="index" class="Message"> {{ item.sender }} {{item.sendTimeStamp}} send: <br/>   <span> <button style="width:120px;height:30px;"  @click="translate(item)" >点击翻译成中文</button> {{ item.message }}</span> </li>
          </ul>
        </div>
        <div class="talk_input"   >
            <input type="text" class="talk_word" style="border-style:solid" id="talkwords" v-model="myMsg"  @keyup.enter="send()"> 
            <input type="button" value="发送"    @click="send()" class="talk_sub"  id="talksub">
              <div>
                                <el-form :inline="true">
                      <el-form-item>
                        <el-button @click="showConfirmCodeDialog()">延时发送</el-button>
                      </el-form-item>
                    </el-form>
                    <el-dialog title="定时消息" v-model="dialogVisible" center width="20%">
                      <div style="display: flex;flex-direction: column;align-items: center;justify-content: center;">
                    <el-form :inline="true">
                      <el-form-item>
                        <el-input v-model="delayMinute" placeholder="请输入延时发送多少分钟" clearable></el-input>
                      </el-form-item>
                    </el-form>
                    <p style="color: #f00;">提示: 输入的一定得是分钟！</p>
                  </div>
                  <span  class="dialog-footer">
                    <el-button @click="dialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="delaySend()">确定</el-button>
                  </span>
                </el-dialog>
              </div>
                <el-upload
                action="http://localhost:9000/SingleChat/send/pdf"
                  :limit="10"
                  accept=".pdf"
                >
            <el-button type="primary"   style="width:60px;border:solid">upload</el-button> 
         </el-upload>
         <el-button type="primary" @click="record()"  style="margin-left:500px;margin-bottom:500px;width:70px;border:solid">聊天记录</el-button>
        </div>
      </div>
    </div>

</template>

<script>
import axios from "axios"
export default {
  name: 'SingleChat',
  data(){
    return {
      msgList:[],
      websocket:null,
        to:"",
        from:"",
        myMsg:"",
        sendMeta:"",
        sendInfo:"",
        dialogVisible:false,
      delayMinute:0
    }
  },
  mounted(){ //onload()
   
    this.to = this.$route.params.to
    this.from  = this.$route.params.from
    this.createChat()
    //判断当前浏览器是否支持WebSocket, 主要此处要更换为自己的地址
    if ('WebSocket' in window) {
        //用户 (from)
     //   console.log("ws://localhost:9000/websocket/"+this.from)
        this.websocket = new WebSocket("ws://localhost:9000/websocket/"+this.from);//整合时：链接后端的websocket（作为controller），内置监听消息的onmessage（别的MQ给这个websocket的user发送消息） 每个user之间使用websocket进行mq的消息发送
       this.websocket.onclose =this.endWebSocket
    this.websocket.onmessage = this.onMessageWebSocket
    console.log("websocket init ok")
    } else {
        alert('Not support websocket')
    }
    // 接收到消息的回调方法
    

   
    //连接关闭的回调方法
    
    
  },
   
  methods: {
    delaySend(){
  var sender = sessionStorage.getItem("username")
  var message = this.myMsg
  var sendTimeStamp = this.getCurTime().toString()
  var receiver = "group"
  var delaytime = this.delayMinute*60*1000
  var req = {
   "msg":{
      "sender":sender,
    "message":message,
    "sendTimeStamp":sendTimeStamp,
    "receiver":receiver
   },
   "delayMs":delaytime
  }
    this.showConfirmCodeDialog()
    
    var url = "http://localhost:9000/delay/message/"
  axios.post(url,req).then(()=>{
    this.dialogVisible = false
  })

},
    endWebSocket(){
      //console.log("关闭websocket")
      this.msgList = []
    },
    onMessageWebSocket(event){
      console.log(event.data)
      this.msgList.push(JSON.parse(event.data))
    },
    createChat(){
      //创建数据表
      var params = {
        to: this.to,
        from: this.from
      }
      axios.post("http://localhost:9000/SingleChat/createDB",params).then(()=>{
          //如果存在，我建议读取DB中的所有msg赋值给myMsg。
          // var reqUrl = "http://localhost:9000/SingleChat/getAllSingleChatInfo/"+params.from+"/"+params.to
          // console.log(reqUrl)
      //   axios.get("http://localhost:9000/SingleChat/getAllSingleChatInfo/"+params.from+"/"+params.to).then((res)=>{
      //     this.msgList = res.data
      // })  读取数据库的消息内容
      });
      
    }
    ,
     getLocalTime(nS) {  //时间戳转换    
        return new Date(parseInt(nS) * 1000).toLocaleString().replace(/:\d{1,2}$/,' ');     
      },
      getCurTime(){
        return Date.now();
      },
       translate(item){

      axios.post("http://localhost:9000/translate/en2zh",item.message).then((res)=>{
        item.message = res.data
    })
   },
      send() {
        // var msg = document.getElementById('talkwords').value;
               var msg = this.myMsg
              var params = {
                "sender" :this.from,
                "receiver":this.to,
                "message":msg,
                "sendTimeStamp":this.getCurTime().toString()
              }
              params = JSON.stringify(params)
                  this.websocket.send(params);
                  this.myMsg = "";
    },
    showConfirmCodeDialog(){
  this.dialogVisible = true
},
    record(){
      this.msgList = []
       axios.get("http://localhost:9000/SingleChat/getAllSingleChatInfo/"+this.to+"/"+this.from).then((res)=>{
          this.msgList = res.data
        })
      
      },
    // send(){
     
    //    var msg = this.myMsg
    //  var params = {
    //    "sender" :this.from,
    //    "receiver":this.to,
    //    "message":msg,
    //    "sendTimeStamp":""
    //  }
      
    //  axios.post("http://localhost:9000/SingleChat/sendInfo",params).then((res)=>{
      
    //    var time =this.getLocalTime(res.data.sendTimeStamp) 
    //     this.sendMeta = time + " " + this.from +" :"  //发出去的信息两行  第一个是发送的元信息
    //     this.sendInfo = msg       //第二个是发送内容
    //     this.websocket.onmessage(res)
       // alert("消息发送成功")
        //页面刚开始的时候就创建一个二人DB，
        // this.msgList = res.data
        // this.myMsg = ""
        //从二人DB中读出数据
        //  axios.get("http://localhost:9000/group/message").then((res)=>{
        // this.list = res.data
        // console.log(this.list)
        // }) 
    //  })
    // },
    file(){
      alert("开始传文件")
    }
  }
}

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
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
            width:350px;
            height:26px;
            padding:0px;
            float:left;
            margin-left:10px;
            outline:none;
            text-indent:10px;
        }        
        .talk_sub{
            width:50px;
            height:30px;
            float:left;
            margin-left:5px;
        }
        // .atalk{
        //    margin:10px; 
        //    text-align:left;
        // }
        // .atalk span{
        //     display:inline-block;
        //     background:#0181cc;
        //     border-radius:10px;
        //     color:#fff;
        //     padding:5px 15px;
        // }
        // .btalk{
        //     margin:10px;
        //     text-align:right;
        // }
        // .btalk span{
        //     display:inline-block;
        //     background:#ef8201;
        //     border-radius:10px;
        //     color:#fff;
        //     padding:5px 10px;
        // }
</style>
