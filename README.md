# star外卖系统后端

# 项目结构
| 模块 | 说明 | 技术                  |
|-----|-----|---------------------|
| mp-weixin | 微信小程序客户端源码 | WeChat Mini Program |
| nginx-1.20.2 | 前端页面与静态资源部署 | vue Nginx           |
| star-server | 后端服务接口与业务逻辑 | Spring Boot         |

# 项目运行说明

## 配置阿里云 OSS
在系统环境变量中配置 **阿里云 AccessKey** 账号和密码。

配置方法参考：  
https://hopoz.github.io/阿里云OSS配置/

---

## 配置 MySQL 数据库
创建数据库并修改后端配置。

数据库信息示例：

```yaml
port: 3306
database: star_take_out
username: test
password: test123
```

确保本地 MySQL 服务已启动。

---

## 配置redis数据库
确保本地 Redis 服务已启动，默认端口为 6379，在application-dev.yml中修改 Redis 连接配置

---

## 启动后端服务

运行 Spring Boot 启动类：

star-server/src/main/java/com/star/StarApplication.java

启动成功后，后端接口服务默认运行在：

http://localhost:8080

---

## 启动前端页面

进入目录 nginx-1.20.2

双击运行 nginx.exe

启动完成后，在浏览器访问：http://localhost

即可进入前端页面。

--- 

## 运行微信小程序

打开 微信开发者工具,导入项目目录：mp-weixin

配置 AppID（测试号或正式 AppID）,编译运行即可。

---