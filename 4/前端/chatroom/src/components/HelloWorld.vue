<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    
   
       <p>  Please input your username to enter the chatroom.</p>
      <form > 用户名:<input type="text" v-model="User.userName" style="padding:5px;margin:5px">  </form>
      <form > 密码:<input type="password" v-model="User.password" style="padding:5px;margin:5px">  </form>
       <div>
       <button  @click="login()"  style="font-size:18px;color:#708090;width:120px;height:40px;margin:13px">进入聊天室</button> 
       <button  @click="register()"  style="font-size:18px;color:#708090;width:120px;height:40px;margin:13px">注册</button> 
       </div>
      <p style="color:#B0C4DE;margin:40px"> Attention:<br/>if the name has been registered,<br/> you need to input another username to login.</p>

  </div>
</template>

<script>
import router from '@/router'
import axios from 'axios';
export default {
  name: 'HelloWorld',
  data(){
    return {
      User:{
        username:"",
        password:""
      }
    }
  },
  props: {
    msg: String
  },
  methods: {
    login(){
      // alert(this.inputUserId)
      axios.post("http://localhost:9000/login",this.User).then((res)=>{
        console.log(res.data.statusCode)
        console.log(res.data.message)
        if(res.data.statusCode!=0){
          alert(res.data.message)
        }else{
          sessionStorage.setItem('username',this.User.userName)
          router.push("chatroom")
        }
        
      })
      
    },
    register(){
      router.push("register")
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
