# 项目说明 dockin

  - `dockin-framework` 为项目基础框架包，包括系统安全、系统管理、系统配置等框架级别代码
  - `dockin-web` 为业务框架包，业务相关的代码在该工程下，依赖  `dockin-framework`包
 
# 技术选型

  - 前端 adminlte+bootstrap 组件集
  - 后端 spring mvc+ spring+ hibernate
  - 权限与安全 shiro
  - 数据库mysql
  - 任务调度 quartz
  - 页面渲染 freeMarker
  - 缓存 redis
  
# 项目部署 
   
   - 客户端安装 git，注册gitlab账号，管理员授权后，使用git clone命令下载代码
   - 客户端安装redis，并设置redis密码
   - 客户端安装mysql， 并且还原数据库，数据库脚本位于doc文件夹下的 `dockin.sql`
   - 在dockin-framework目录下运行 mvn install 生成dockin-framework-0.0.1.jar包
   - 修改jdbc.properties中数据库连接配置
   - 修改setting.properties 中缓存连接配置
   - 在dockin-web下启动项目,启动后的地址是 [http://localhost:8081/dockin-web/login](http://localhost:8081/dockin-web/login)
