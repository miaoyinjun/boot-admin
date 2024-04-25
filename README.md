企业级快速开发平台，前后端分离设计，基于SpringBoot2.x、SpringCloud、Spring Security，MyBatis-plus，Vue，工作流，在线代码生成器，支持单体服务与微服务之间灵活切换、Docker，帮助开发者节省80%的重复工作，更专注业务，节省开发成本，百分百开源
### 体验地址：
[http://104.207.153.101](http://104.207.153.101/)<br />**登录**：账号：demo，密码：123456
### 技术文档：
[https://www.yuque.com/miaoyj/nsln4r](https://www.yuque.com/miaoyj/nsln4r)
### 项目介绍

1. **基础功能**：用户、角色、菜单、部门、岗位、数据字典、任务调度、工作流、版本管理
2. **微服务**：支持单体服务与微服务之间灵活切换，灰度发布、流量控制、熔断降级
3. **在线代码生成器**：单表生成前后端代码
4. **操作日志**：注解，追溯数据修改历史
5. **查询过滤器**：SQL自动动态拼装
6. **数据权限**：菜单级、按钮级、数据行级、数据列级、列表和表单字段级
7. **字段验证**：借助强大的注解验证和异常捕捉实现（非空、数字、日期、手机号、邮件等）
8. **文件服务**：集成本地存储、七牛云，minio，可自由扩展
9. **数据库**：支持读写分离、分库分表
10. **接口定义**：统一restful风格，完整的出入参格式定义、集成knife4j在线接口文档，JWT token安全验证
11. **系统监控**：在线用户、操作日志、服务器JVM监控、SQL监控、spring-boot-admin应用监控，Redis，最新git提交
12. **权限控制**：RBAC基于角色控制访问
13. **对外接口安全**：通过应用标识、签名、出入参密文、限流、白名单传输方式，保证接口数据安全
14. **账号/密码策略**：账号锁定、账号过期、密码过期、密码复杂度定义
15. **工具类**：集成hutool、短信发送、邮件发送、Excel导出
16. **前端crud组件**，实现页面的分页查询、新增、修改、删除、导出
17. **数据库版本控制**：Liquibase管理sql脚本
18. **开发规范**：阿里代码开发规范
19. **最佳实践**：提供本地开发、测试、生产、Docker部署文档
### 技术栈
| **技术** | **名称** | **说明** |
| --- | --- | --- |
| springBoot | springBoot框架 |  |
| spring-cloud-alibaba | 微服务 |  |
| spring Security | 安全框架 | 权限认证 |
| mybatis plus | mybatis增强 | 增强对数据库操作工具 |
| flowable | 工作流 | <br /> |
| Druid | 数据库连接池 | 提供监控 |
| knife4j | swagger接口文档增强 |  |
| MapStruct | Bean映射工具 |  |
| jetCache | 通用缓存框架 |  |
| liquibase | 管理数据库变化工具 | 跟踪,管理SQL脚本 |
| p6spy | SQL日志打印工具 |  |
| spring-boot-admin | 管理和监控SpringBoot应用程序 |  |
| jasypt | 配置文件加密 |  |
| lombok | 生成POJO的getter/setter |  |
| hutool | 工具类库 |  |
| logback | 日志框架 |  |
| xxl-job | 分布式定时框架 |  |
| jgitflow-maven-plugin | 简化实现git flow工作流程插件 |  |
| git-commit-id-plugin | git commit信息收集插件 | maven打jar包时带上gitommit信息 |
| maven-javadoc-plugin | javadoc插件 | 检查，填充部分注释信息 |
| screw-maven-plugin | 数据库文档生成插件 |  |
| springloaded | 热部署插件 |  |
| Vue | 前端框架 |  |
| Variant Form | 前端-低代码表单 |  |

### 模块说明
> jjche-boot-ui -- 前端

> jjche-boot-server -- 单体启动入口

> jjche-cloud-server -- 微服务启动入口
>> jjche-cloud-nacos -- 注册中心
>>
>> jjche-cloud-gateway -- 网关服务
>>
>> jjche-cloud-system -- 系统服务
>>
>> jjche-cloud-bpm -- 工作流服务
>>
>> jjche-cloud-demo -- 示例服务
>>
>> jjche-cloud-file -- 文件服务
>>
>> jjche-cloud-monitor -- 监控服务
>>
>> jjche-cloud-sentinel -- Sentinel流控熔断
>>
>> jjche-cloud-xxljob -- 分布式定时服务
>>
> jjche-boot-modules  -- 业务模块
>> jjche-boot-module-system  -- 系统模块
>> 
>> jjche-boot-module-bpm -- 工作流模块
>> 
>> jjche-boot-module-demo -- 示例模块

> jjche-boot-framework -- 框架
>> jjche-boot-common -- 通用
>> 
>> jjche-boot-starters -- 基础组件
>>
>>> jjche-boot-cache-starter -- 缓存
>>> jjche-boot-cat-starter -- CAT监控
>>>
>>> jjche-boot-core-starter -- 核心
>>>
>>> jjche-boot-filter-starter -- 安全过滤器
>>>
>>> jjche-boot-flowable-starter -- 工作流
>>>
>>> jjche-boot-log-starter -- 日志
>>>
>>> jjche-boot-minio-starter -- Minio存储
>>>
>>> jjche-boot-mybatis-starter -- mybatis定义
>>>
>>> jjche-boot-sba-starter -- spring-boot-admin增强
>>>
>>> jjche-boot-security-starter -- 安全
>>>
>>> jjche-boot-sentinel-dashboard-starter -- sentinel控制台
>>>
>>> jjche-boot-serialize-starter -- 序列化
>>>
>>> jjche-boot-shardingsphere-starter -- 分库分表
>>>
>>> jjche-boot-starter -- 组件集成
>>>
>>> jjche-boot-swagger-starter -- API文档
>>>
>>> jjche-boot-system-api-starter -- 系统核心接口定义
>>>
>>> jjche-boot-userdetail-starter -- 默认安全认证定义
>>>
>>> jjche-boot-xxl-job-starter -- xxljob客户端
>>>
>> jjche-cloud-starters -- 微服务组件
>>> jjche-cloud-gray-starter -- 灰度发布
>>>
>>> jjche-cloud-starter -- 微服务组件集成
>>>
>>> jjche-cloud-system-api-starter -- 系统核心接口定义


![](https://miaoyinjun.gitee.io/jjche-boot-book/assets/1712541760561-443af786-2530-4b85-8d8f-fab41f359de0.png)<br />![](https://miaoyinjun.gitee.io/jjche-boot-book/assets/1712541760710-09c08b5b-a4d4-495d-98be-afa6b7699703.png)<br />![](https://miaoyinjun.gitee.io/jjche-boot-book/assets/1712541760644-97bcd508-1a2b-4b8c-b9dd-2097824ba593.png)<br />![](https://miaoyinjun.gitee.io/jjche-boot-book/assets/1712546838923-e86d0acf-4e0e-443e-b631-23d5593a1ff5.png)<br />![](https://miaoyinjun.gitee.io/jjche-boot-book/assets/1712546927982-7548e330-0be9-48db-a41d-399ddb082cff.png)<br />![](https://miaoyinjun.gitee.io/jjche-boot-book/assets/1712541760840-3a24f014-dafa-45c0-8d9c-203e74ca386f.png)<br />![](https://miaoyinjun.gitee.io/jjche-boot-book/assets/1712541760847-12c78ba9-53ab-4d1a-9374-a0553cd819e8.png)<br />![](https://miaoyinjun.gitee.io/jjche-boot-book/assets/1712541762285-dc56b2f0-0b79-4a93-bdd6-4450ded30252.png)<br />![](https://miaoyinjun.gitee.io/jjche-boot-book/assets/1712541762262-cade073b-ac8c-49ca-82a8-9346a8705c66.png)<br />![](https://miaoyinjun.gitee.io/jjche-boot-book/assets/1712541762246-b8ef2825-32b3-4d81-bc81-15b676bfa637.png)<br />![](https://miaoyinjun.gitee.io/jjche-boot-book/assets/1712541762310-46f6333e-bcad-4200-a5c6-f73ef1caf8fa.png)<br />![](https://miaoyinjun.gitee.io/jjche-boot-book/assets/1712541763240-6f4236e3-f9c0-4efb-9b63-b80ad108945e.png)<br />![](https://miaoyinjun.gitee.io/jjche-boot-book/assets/1712541763377-2e4edc62-b924-4cbc-8636-22f5217ea596.png)<br />![](https://miaoyinjun.gitee.io/jjche-boot-book/assets/1712541763463-a8541f38-11a6-443c-b4ab-e929f2a4f912.png)<br />![](https://miaoyinjun.gitee.io/jjche-boot-book/assets/1712541763490-70000301-fbe8-4a07-93f2-a770c2801734.png)
## 特别鸣谢

1. 感谢[eladmin](https://github.com/elunez/eladmin) 允许在基础业务功能上进行扩展
2. 感谢[mouzt](https://github.com/mouzt/mzt-biz-log/) 提供的日志组件
3. 引入了[JEECG/jeecg-boot](https://gitee.com/jeecg/jeecg-boot)部分代码
4. 引入[芋道源码/ruoyi-vue-pro](https://gitee.com/zhijiantianya/ruoyi-vue-pro)工作流组件
## 支持
如果您喜欢该项目，请给项目**点亮⭐️**，让更多的开发者看到<br />QQ交流群：**604384111**<br />如果您还想支持一下，请作者喝杯 **咖啡**吧🤓

|                       支付宝                       |                             微信                             |
| :------------------------------------------------: | :----------------------------------------------------------: |
| ![1640670237773__01](https://miaoyinjun.gitee.io/jjche-boot-book/assets/1640670237773__01.jpg) | ![mm_facetoface_collect_qrcode_1640667560388__01__01](https://miaoyinjun.gitee.io/jjche-boot-book/assets/mm_facetoface_collect_qrcode_1640667560388__01__01.png) |
