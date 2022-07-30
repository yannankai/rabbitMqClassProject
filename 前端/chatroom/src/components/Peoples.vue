<template>
  <div class="chat">
  
    <h1> 群聊人员 </h1>

  <ul>
    <li  v-for="item in userList" :key="item"> <button @click="SingleChat( item.userName )">{{item?.userName}} </button>  </li>
  </ul> 
  <!--                                         因为是异步，未返回刷新时会先报错。加?等待返回在加载 -->

    </div>

</template>

<script>
import router from "@/router"
import axios from "axios"
export default {
  name: 'PeopleChater',
  data(){
    return {
     host:"Host",
     chatWith:"testPeople",  //如何与button上的用户绑定起来
     userList:[
         this.host,
     ],
     total : 0,
    }
  },
  created(){
      this.getAllUsers()
    },
  methods: {
    getAllUsers(){
      axios.get("http://localhost:9000/all/Users").then((res)=>{
        this.userList = res.data
        console.log(this.userList)
      })
    },
   SingleChat(toUsr){
        this.chatWith = toUsr
         var me = sessionStorage.getItem("username")
         console.log(me)
         if (me==toUsr){
           alert("不能和自己聊天！")
         }else{
            router.push("/SingleChat/"+me+"/"+this.chatWith)
         }       
   },
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">

</style>
