<template>
<div  class="m-content">
    <h3>欢迎来到我的博客？上传vueblog-vue到github1</h3>
    <div class="block"><el-avatar :size="50" :src="user.avatar"></el-avatar></div>
    <div>{{user.username}}</div>

    <div class="maction">
        <span> <el-link  href="/blogs">主页</el-link></span>
        <el-divider direction="vertical"></el-divider>
        <span> <el-link type="success" href="/blog/add">发表文章</el-link></span>
        <el-divider direction="vertical"></el-divider>
        <span v-show="!hasLogin"><el-link type="primary" href="/login">登录</el-link></span>
        <el-divider direction="vertical"></el-divider>
        <span  v-show="hasLogin" > <el-link type="danger" @click="logout">退出</el-link></span>
    </div>




</div>
</template>

<script>
    export default {
        name: "Header",
        data(){
            return{
                user:{
                    username:"请先登录",
                    avatar:"https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
                },
                /*默认不登录*/
                hasLogin: false
            }
        },
        methods:{
            logout(){
                /*如果后端的token有状态的，就需要去请求*/
                /*由于jwt 无状态 所以直接清除 游览器的信息就行*/
                const  _this = this
                _this.$axios.get("/logout",{
                    headers:{
                        "Authorization":localStorage.getItem("token")
                    }
                }).then(res=>{
                    _this.$store.commit("REMOVE_INFO")
                    _this.$router.push("/login")
                })
            }
        },
        created() {
            /*如果用户名不为空，将赋值显示*/
            if (this.$store.getters.getUser.username) {
                this.user.username = this.$store.getters.getUser.username
                this.user.avatar = this.$store.getters.getUser.avatar

                /*登录后显示退出按钮*/
                this.hasLogin = true
            }
        }
    }
</script>

<style scoped>
    .m-content{
        max-width: 960px;
        /*居中要给给宽度才会*/
        margin:0 auto;
        text-align: center;
    }
    .maction{
        margin:10px;
    }


</style>
