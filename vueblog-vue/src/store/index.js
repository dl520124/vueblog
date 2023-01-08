import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token:'',
    /*得到的一系列代码，要反序列化，json不能传对象*/
    userInfo:JSON.parse(sessionStorage.getItem("userInfo"))
  },
  mutations: {
    //set
    //(state,载体（也就是传过来的值）)=>
     SET_TOKEN:(state,token)=>{
       state.token = token
       /*还要存储在游览器里面*/
       localStorage.setItem("token",token)
     },
    SET_USERINFO:(state,userInfo)=>{
      state.userInfo = userInfo
      /*存储在一次会话里面，关掉游览器就没了*/
      /*sessionStorage不能存储一个对象，只能存一些字符串，所以要徐序列化*/
      sessionStorage.setItem("userInfo",JSON.stringify(userInfo))
    },

    REMOVE_INFO:(state)=>{
      state.token =''
      state.userInfo={}
      /*移除*/
      localStorage.setItem("token",'')
      sessionStorage.setItem('userInfo',JSON.stringify(''))
    },

  },
  getters:{
    //get
    getUser:state => {
      return state.userInfo
    }

  },
  actions: {
  },
  modules: {
  }
})
