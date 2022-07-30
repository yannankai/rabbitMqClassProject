import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ChatRoom from '../views/MultitudeCharRoom.vue'
import ChatPage from '../components/ChatPage.vue'
import RegisterUser from '../components/register.vue'
import SingleChat from '../components/SingleChat.vue'
import PeopleChater from '../components/Peoples.vue'

const routes = [
    {
        path: '/',
        name: 'home',
        component: HomeView
    },
    {
        path: '/about',
        name: 'about',
        component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
    },
    {
        path: '/chatRoom',
        name: 'chatroom',
        component: ChatRoom, ChatPage
    },
    {
        path: '/register',
        name: 'register',
        component: RegisterUser
    },
    {
        path: '/SingleChat/:from/:to',
        name: "SingleChat",
        component: SingleChat
    },
    {
        path: '/PeopleChater',
        name:"PeopleChater",
        component:PeopleChater
    },


]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
