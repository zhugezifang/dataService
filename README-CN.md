# dataService
![](https://gitee.com/ZhuGeZiFang/dataService/badge/star.svg)
![](https://gitee.com/ZhuGeZiFang/dataService/badge/fork.svg?theme=gvp)
![](https://img.shields.io/github/stars/zhugezifang/dataService.svg?logo=GitHub)
![](https://img.shields.io/github/forks/zhugezifang/dataService.svg?logo=GitHub)
![](https://img.shields.io/github/watchers/zhugezifang/dataService.svg?logo=GitHub)
![](https://img.shields.io/github/license/zhugezifang/dataService.svg)
![](https://img.shields.io/github/v/release/zhugezifang/dataService?label=latest&style=flat-square)

[![EN doc](https://img.shields.io/badge/document-English-blue.svg)](README.md)
[![CN doc](https://img.shields.io/badge/文档-中文版-blue.svg)](README-CN.md)

#### 项目在线体验地址
https://b53c9def027279b16719914b13ec7ac6-app.1024paas.com/login

#### 介绍
dataService 旨在提供全面的数据服务及共享能力，统一管理面向内外部的API服务。

能够将数据表快速生成数据API，或将已有API快速注册至本平台进行统一管理与发布。

dataService可实现API服务一键发布，实现低成本、易上手、安全稳定的数据共享与开放，接口开发者只需关注API本身查询逻辑

#### 功能介绍
(1)低代码配置，只需要写少量sql 即可实现api服务

(2)实现如下三个统一：

a.数据服务统一化：接口不同QPS和RT，不同的接口服务(HTTP、RPC、文件传输等)，即：OneAPI

b.存储解析统一化，一套语言支持多种数据存储接入，即：OneSQL

c.数据模型统一化，支持多种数据源接入, 即:OneModel


#### 软件架构

![image](https://user-images.githubusercontent.com/28300167/215253954-e94851c6-65b9-4ab5-a7b8-9e71a4765be9.png)


技术栈:

后端：Spring boot + Mybatis

数据库:MySQL

#### 系统流程图

![image](https://user-images.githubusercontent.com/28300167/215254320-21a90c64-e336-4c59-8743-eeada2b8a486.png)

![image](https://user-images.githubusercontent.com/28300167/215254356-a2968fbb-f82f-4147-b491-254ae4d31ed8.png)


#### 系统功能演示
主页
![image](https://user-images.githubusercontent.com/28300167/215254586-8d38b001-3a24-4f2b-858f-9f4b434f24aa.png)
数据源管理
![image](https://user-images.githubusercontent.com/28300167/215254616-a0278626-0338-4369-a709-9fc750ed8b27.png)
新增api服务
![image](https://user-images.githubusercontent.com/28300167/215254679-71c934ba-e491-4394-9fb8-5fcd998023f4.png)
测试api服务
![image](https://user-images.githubusercontent.com/28300167/215254700-cbc9c17d-ccc0-4e52-9ade-95e2823587a0.png)

#### 系统运行
系统运行环境要求：

java jdk8

mysql 5.7.36

运行配置

(1)将sql目录中的sql文件进行运行在数据库，创建库和表

(2)在根目录下面运行 mvn clean package -Dmaven.test.skip=true,使用admin/target 目录下的dataSerivce.jar

(3)修改数据库配置信息 application.yml

(4)运行 java -jar -Dspring.config.location=application.yml dataService.jar (application.yml和jar在同一目录下)

(5)访问 http://127.0.0.1/ 即可 (账号:admin 密码:admin123)

#### Contributors
|Alias |Github |Email |
|:-- |:-- |:-- |
|zhugezifang|[zhugezifang](https://github.com/zhugezifang)|xiaoqiu2017wy@164.com|
|shigongxing|[shigongxing](https://github.com/shigongxing)|940947367@qq.com|

#### 技术交流
![image](https://user-images.githubusercontent.com/28300167/207255900-152d6834-9602-4ada-91ca-ad9906d89bf8.png)


#### 致谢
感谢ruoyi 提供前端服务
感谢1024code(https://1024code.com)提供服务部署资源