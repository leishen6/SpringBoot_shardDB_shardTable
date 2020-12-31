## 项目信息：

此项目是基于<font color=blue>**SpringBoot**</font>的，并在其基础上设计了<font color=blue> **RestFul** </font>风格的接口 。除此之外，还整合了其它的功能：

1.  整合了**swagger**生成在线的接口文档，并且实现统一格式的响应及异常情况处理；
2.  集成了 **PageHelper** 分页插件，支持更加友好的分页查询；
3.  整合了 **Mybatis** 、**Redis**，以及 详细的 日志配置；
4.  整合了 **Sharding-JDBC** ，实现了 分库分表，并且在分库分表中实现了自定义的分片算法; 一致性Hash算法，易于扩容；
5.  添加了 **单元测试**，使用Spring提供的RestTemplate调用RestFul风格的API接口；
6.  整合了 **quartz 定时任务框架** ，并进行了封装，只需在构建完定时任务Job类后，在 **application-quartz.properties** 配置文件中进行简单配置即可；
7.  实现了 **reids 分布式锁** ，当项目部署集群时，使用分布式锁来避免多台节点的定时任务重复执行；
8.  使用 **redis key 过期机制** 实现了定时任务，注意：此定时任务执行时存在延时的，如果对时间不敏感可以尝试使用；



## 参考资料：

1.  本项目的Swagger在线文档打开地址：http://localhost:8083/swagger-ui.html#/
2.  SpringBoot项目整体结构搭建 ：https://www.cnblogs.com/xuwujing/p/8260935.html 
3.  SpringBoot项目整合swagger(这只是一种整合方式) ：https://www.jianshu.com/p/d6424d98b02e
4.  SpringBoot项目整合PageHelper分页插件：https://www.cnblogs.com/DawnCHENXI/p/9221653.html
5.  SpringBoot项目简单的日志配置：https://www.cnblogs.com/bigdataZJ/p/springboot-log.html
6.  SpringBoot项目集成Redis：https://www.jianshu.com/p/fd65156ff630
7.  SpringBoot项目集成Sharding-JDBC：https://shardingsphere.apache.org/document/legacy/3.x/document/cn/manual/
8.  SpringBoot项目集成Quartz：https://blog.csdn.net/upxiaofeng/article/details/79415108



## 博主信息：

1.  CSDN博客：https://blog.csdn.net/feichitianxia 
2.  掘金博客：https://juejin.im/user/5c67b8046fb9a049a7125a58/posts
3.  思否博客：https://segmentfault.com/u/muzilei_5e72d30d2c9fb/articles
4.  GitHub地址：https://github.com/leishen6?tab=repositories 
5.  开源中国：https://my.oschina.net/u/4216693
6.  个人网站地址：https://leishen6.github.io/
7.  微信公众号：木子雷
