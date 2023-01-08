<template>

    <div>
        <Header>  </Header>

        <div class="mblog">
            <h2>   {{blog.title}}</h2>
            <el-link icon="el-icon-edit"  v-if="ownBlog">
                <router-link :to="{name:'BlogEdit',parms:{blogId:blog.id}}">
                编辑
                </router-link>
            </el-link>
            &#8194;

            <el-link icon="el-icon-delete" v-if="ownBlog" class="linklist">
                <el-button type="danger" round @click="delblog">删除</el-button>
            </el-link>

            <el-divider></el-divider>
            <div  class="markdown-body" v-html="blog.content"></div>

        </div>


    </div>
</template>

<script>
    import  Header from "../components/Header";
    import 'github-markdown-css/github-markdown.css' // 然后添加样式markdown-body

    export default {
        name: "BlogDetail.vue",
     components:{Header},
        data(){
            return{
                blog:{
                    id:"",
                    title:"moren",
                    content:"nierong"
                },
                ownBlog:false
            }
        },

        methods: {
            delblog () {
                /*blogID哪里来？哪里定义*/
                const blogId = this.$route.params.blogId  /*获取url中的参数，与router中的index对应*/
                console.log(blogId)
                const _this = this

                console.log(blogId)
                if (blogId) {
                    this.$confirm('此操作将永久删除该文章, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        _this.$axios.post(`/blogdel/${blogId}`, null, {
                            headers: {
                                "Authorization": localStorage.getItem("token")
                            }
                        }).then(res => {
                            this.$message({
                                type: 'success',
                                message: res.data.data
                            });
                            _this.$router.push("/blogs")
                        })

                    }).catch(() => {

                        this.$message({
                            type: 'info',
                            message: '已取消删除'
                        });
                    });


                }
            }
        },
        /*初始化就获取详情*/
        created() {
            /*拿id ，注意是在route里面找*/
            const blogId = this.$route.params.blogId
            console.log(blogId)
            const _this = this
            if(blogId){
                this.$axios.get('/blog/'+blogId).then(res=>{
                    const  blog = res.data.data
                    _this.blog.id =blog.id
                    _this.blog.title =blog.title

                    var MardownIt = require("markdown-it")
                    var md = new MardownIt()

                    var result=  md.render(blog.content)

                    _this.blog.content =result

                    _this.ownBlog =(blog.userId === _this.$store.getters.getUser.id)
                })
            }
        }

    }
</script>

<style scoped>

    .mblog{
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        width: 100%;
        min-height: 700px;
    padding: 20px 15px;
    }
</style>
