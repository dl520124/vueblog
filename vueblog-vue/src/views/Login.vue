<template>

    <div>
        <el-container>
            <el-header>
          <img   class="mlogo" src="https://typora-1300213864.cos.ap-guangzhou.myqcloud.com/typora/QQ%E5%9B%BE%E7%89%8720210222041346.jpg " alt="">
        </el-header>
            <el-main>
                <!--  ：双向绑定，data数据与该标签数据绑定  -->
                <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                    <el-form-item label="用户名" prop="username">
                        <!--v-model实现这些标签数据的双向绑定-->
                        <el-input v-model="ruleForm.username"></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password">
                        <el-input  type="password" v-model="ruleForm.password"></el-input>
                    </el-form-item>

                    <el-form-item>
                        <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
                        <el-button @click="resetForm('ruleForm')">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "Login.vue",
        data() {
            return {
                /*数据*/
                ruleForm: {
                    username: '',
                    password: ''
                },
                rules: {
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' },
                        { min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur' }
                    ],
                   password: [
                        { required: true, message: '请输入密码', trigger: 'change' }
                    ]
                }
            };
        },
        methods: {
            submitForm(formName) {
                /*先把全局的this存储起来，不然axios发起请求之后this就变了*/
                const _this = this

                this.$refs[formName].validate((valid) => {
                    if (valid) {

                        /*跳转地址，表单数据,之后会得到结果，在then里面*/
                        this.$axios.post('/login',this.ruleForm).then(res =>{
                            /*从headers里面拿到jwt数据*/
                            const jwt = res.headers['authorization']
                            /*存储在localstore,将jwt全局配给其他组件，要设置为全局参数*/
                            const  userInfo = res.data.data



                            /*将jwt、user赋值给全局(store)中的token,user*/
                            _this.$store.commit("SET_TOKEN",jwt)
                            _this.$store.commit("SET_USERINFO",userInfo)


                            //获取
                            console.log(_this.$store.getters.getUser)

                            _this.$router.push("/blogs")

                        })


                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        }

    }
</script>

<style scoped>
    .el-header, .el-footer {
        background-color: #B3C0D1;
        color: #333;
        text-align: center;
        line-height: 60px;
    }

    .el-aside {
        background-color: #D3DCE6;
        color: #333;
        text-align: center;
        line-height: 200px;
    }

    .el-main {
        /*background-color: #E9EEF3;*/
        color: #333;
        text-align: center;
        line-height: 160px;
    }

    body > .el-container {
        margin-bottom: 40px;
    }

    .el-container:nth-child(5) .el-aside,
    .el-container:nth-child(6) .el-aside {
        line-height: 260px;
    }

    .el-container:nth-child(7) .el-aside {
        line-height: 320px;
    }

    .mlogo{
        height: 60%;
        margin-top: 10px;
    }

    .demo-ruleForm{
        max-width: 500px;
        margin:0 auto;
    }

</style>
