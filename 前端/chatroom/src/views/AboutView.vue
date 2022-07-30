<template>
  <div class="about">
    <h1>This is a System info page</h1>
    <button @click="getExchangeInfo">exchangeInfo</button>
    <!-- <button @click="getQueueInfo">queueInfo</button>-->
     <ul> 
       <li  v-for="item in exchangeList" :key="item"> 时间戳:{{ getLocalTime(item.timeStamp) }} <br/>MQ中目前有{{item?.exchangeNum}}  个交换机 </li>
     </ul> 


     
  </div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'SingleChat',
  components: {
    
  },
  data(){
    return{
      exchangeList:[],
      queueList:[]
    }
  },
  methods:{
    getExchangeInfo(){
      axios.get("http://localhost:9000/sys/exchange").then((res)=>{
        console.log(res.data)
        this.exchangeList = res.data
      })
    },
  getLocalTime(nS) {     
   return new Date(parseFloat(nS)).toLocaleString().replace(/:\d{1,2}$/,' ');     
  }     
    // getQueueInfo(){

    // },

  }
}
</script>

