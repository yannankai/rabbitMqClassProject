<template>
  <div class="home">
    <img alt="Vue logo" style="height:250px;width:350px;" src="../assets/chatroom.png">
    <HelloWorld msg="Welcome to Chatroom"/>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'

export default {
  name: 'HomeView',
  components: {
    HelloWorld
  }
}
</script>
