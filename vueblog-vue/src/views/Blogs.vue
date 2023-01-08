<template>


    <div>
        <Header>  </Header>

        <!--内容-->
        <div class="block">

            <!--测试-->




            <el-timeline>
                <!--  :  表示绑定值 -->
                <!--时间、   循环v-for="blog in blogs"  for循环每个blog-->
    <!-- <el-timeline-item :timestamp="$moment(blog.created).utcOffset(480).format('YYYY-MM-DD HH:mm:ss')" placement="top"  v-for="blog in blogs">-->

          <el-timeline-item :timestamp="blog.created" placement="top"  v-for="blog in blogs">
                    <el-card>
                        <h4>
                        <!-- router-link  跳到哪个主键   name为路由名字 -->
                            <!--  超链接点标题根据id跳转 name为  BlogDetail 的路由
                               path: '/blog/:blogId',  携带参数为blog.id-->
                        <router-link :to ="{name : 'BlogDetail' ,params:{blogId:blog.id}}">
                            {{blog.title}}
                        </router-link>
                        </h4>
                        <p>{{blog.description}}</p>
                    </el-card>
                </el-timeline-item>

            </el-timeline>


              <!--分页-->
            <el-pagination  class="mpage"
                            background
                            layout="prev, pager, next"
                            :current-page="currentPage"
                            :page-size="pageSize"
                            :total="total"
                            @current-change=page
            >
            </el-pagination>
        </div>



    </div>
</template>

<script>
    import  Header from "../components/Header";
    export default {
        name: "Blogs.vue",
        components:{Header},
        data(){
            /*定义的是页面参数，如果不绑定就默认的来，一般发起请求拿到后端的参数，双向绑定值*/
            return{
                /*返回值定义*/
                blogs:{},
                currentPage:1,
                total:0,
                pageSize:5
            }
        },
        methods:{
            /*分页方法  currentPage 当前页 */
            page(currentPage){
                const  _this = this
                _this.$axios.get("/blogs?currentPage=" + currentPage).then(res=>{
                    console.log(res)

                    console.log(this.$moment(this.createTime).format('YYYY-MM-DD HH:mm:ss'));
                    /*将后端获取的值赋值,将records的内容赋值给blogs*/
                    _this.blogs = res.data.data.records
                    /*当前页*/
                    _this.currentPage = res.data.data.current
                    /*总数*/
                    _this.total = res.data.data.total
                    /*页大小*/
                    _this.pageSize = res.data.data.size
                })
            }
        },
        /*页面开始就渲染*/
        created() {
            this.page(1)
        }
    }
</script>

<style scoped>
    .mpage{
        margin: 0 auto;
        text-align:center;
    }

</style>
