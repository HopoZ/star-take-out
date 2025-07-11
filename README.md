# star外卖系统后端


# 运行
首先在系统环境变量中设置好阿里云AccessKey的账号和密码。参考[hopoz.github.io/阿里云OSS配置/](https://hopoz.github.io/%E9%98%BF%E9%87%8C%E4%BA%91OSS%E9%85%8D%E7%BD%AE/)
接着设置好这个MySQL数据库
```
    port: 3306
    database: star_take_out
    username: test
    password: test123
```
接着运行[后端服务](/star-server/src/main/java/com/star/StarApplication.java)
接着点击[nginx](/nginx-1.20.2/nginx.exe)运行前端页面，打开[](http://localhost:8080)即可。