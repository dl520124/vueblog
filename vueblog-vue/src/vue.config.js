/*module.exports = {
    devServer: {
        proxy: {
            '/api': {
                target: 'http://localhost:8082/', //接口域名java的端口
                changeOrigin: true,             //是否跨域
                ws: true,                       //是否代理 websockets
                secure: true,                   //是否https接口
                pathRewrite: {                  //路径重置
                    '^/api': ''
                }
            }
        }
    }
};*/
module.exports = {
    publicPath: '/'
}
