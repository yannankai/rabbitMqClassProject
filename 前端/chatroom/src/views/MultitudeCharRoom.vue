<template>
  <div class="hello">
    这里是聊天室
   <chat-page></chat-page>
  </div>
</template>

<script>
import ChatPage from '@/components/ChatPage.vue'
export default {
  components: { ChatPage },
  name: 'ChatRoom',
  props: {
    
  },
  methods: {
   
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">

</style>
