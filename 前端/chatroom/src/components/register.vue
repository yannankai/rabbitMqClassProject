<template>
  <div class="register">
    <h1>注册页面</h1>
    
    <p>
      Please input your username to Register the chatroom.</p>
      <form method = "POST"> 用户名:<input type="text" v-model="userRegistered.userName" style="padding:5px;margin:5px">  </form>
       <form method = "POST"> 密码: <input type="password" v-model="userRegistered.password" style="padding:6px;margin:50px">  </form>

       <div>
       <button  @click="register()"  style="font-size:18px;color:#708090;width:120px;height:40px;margin:13px">注册</button> 
       </div>
      <p style="color:#B0C4DE;margin:40px"> Attention:<br/>if the name has been registered,<br/> you need to input another username to login.</p>


   
   
  </div>
</template>

<script>
import router from "@/router"
import axios from "axios";
export default {
  name: 'RegisterUser',
  data(){
    return {
      userRegistered:{
        //是个对象 和后端对接好
        userName:"",
        password:""
      },
    }
  },
  props: {
    msg: String
  },
  methods: {
   
    register(){
        
         axios.post('http://localhost:9000/register',this.userRegistered).then((response)=>{
             if (response.data.statusCode == 0){
               alert(response.data.message)
               router.push("/chatroom")
               sessionStorage.setItem('username',this.userRegistered.userName)
             }else{
               alert(response.data.message)
             }
         })
      
    }
  }
}
</script>

<style scoped lang="scss">

</style>
