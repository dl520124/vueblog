import axios from 'axios'
import Element from 'element-ui'
import router from './router'
import store from './store'


var s=window.location.toString();
var s1=s.substr(7,s.length);
var s2=s1.indexOf("/");
s=s.substr(0,8+s2);
/*var a = "http://localhost:8081";*/
//连接后端,获取连接前缀相当于 http://localhost:8081/
var a = "http://43.142.249.132:8081";

/*axios.defaults.baseURL = 'http://localhost:8443/api'*/
/*全局axios前缀*/
/*这个是vue的前缀*/
axios.defaults.baseURL=a
/*前置拦截*/
axios.defaults.withCredentials = true  //前端实现cookie跨域
axios.interceptors.request.use(config=>{
    return config
})

/*后置拦截*/
axios.interceptors.response.use(response=> {
    let res = response.data;
    console.log("========")
    console.log(res)
    console.log("========")

    if (res.code === 200) {
        return response
    } else {
        /*这里导入main的，所以要添加import，不能用this*/
        Element.Message.error('错了哦，这是一条错误消息', {duration: 3 * 1000});
        /*阻止他走完整个axios*/
        return Promise.reject(response.data.msg)
    }
},
    error =>{
        console.log(error)
        /*如果返回的错误有信息的话*/
        if (error.response.data) {
            error.message = error.response.data.msg
        }


        /*401异常，没有权限,还没有登录的情况等*/
        if(error.response.state === 401){

            store.commit("REMOVE_INFO")
            router.push("/login")
        }
        Element.Message.error(error.message, {duration: 3 * 1000});
        return Promise.reject(error)

})
