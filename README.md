# COLA & DDD 的微服务示例工程

## 业务简介

&emsp;&emsp;模拟一个简单的电商下单流程，用户注册并登录系统后添加商品生成订单，在支付操作时尝试扣减库存，库存满足订单需求时通知支付模块生成支付流水单，并以定时任务形式模拟第三方支付扣款。

&emsp;&emsp;虽然业务逻辑稍显简单，本着以小见大、见微知著来实践体会分布式微服务采取 COLA 结合 DDD 形式和传统 MVC 结构之间的差异，以求得到 COLA、DDD 的设计思想。

## 工程结构

* msia-ddd-users 用户服务
* msia-ddd-products 商品库存服务
* msia-ddd-payment 账户支付服务
* msia-ddd-orders 订单服务
* msia-ddd-gateway 应用网关服务
* msia-ddd-core 核心模块
* msia-ddd-commons 通用模块
* msia-components 一些基础设施组件模块

## 工程启动

### 前置依赖

* Docker
  * 启动 Docker，执行 `docker-compose up -d` 启动 Nacos、RabbitMQ  中间件

* MySQL
  * 执行 `doc/sql` 目录下的数据库脚本，完成微服务所需的数据库表的创建
* Nacos
  * 在 Nacos 网页控制台的配置管理页面，选择导入 `doc/nacos` 下的服务配置
* RabbitMQ

### 服务启动

&emsp;&emsp;以 `prod` 的 profile 启动如下服务：

* msia-ddd-gateway
* msia-ddd-users-bootstrap
* msia-ddd-products-bootstrap
* msia-ddd-orders-bootstrap
* msia-ddd-payment-bootstrap

> 单个服务调试时，以 `dev` 形式读取本地配置文件方式启动，可无需启动 Nacos

### 接口测试

* Postman
  * 在 Postman 中，使用 Import 导入 `doc/postman` 目录下的 API 集合，即可完成登录、下单、支付、查询订单基本流程测试

### 

## 

