###1.整合ssm
1)拷jar包
2)日志文件log4j和数据库配置文件
3)创建mybatis-config.xml（右击--新建--小鸟标志）


### 登陆校验
    之前使用的是Filter来实现了登陆校验功能，如果未登录那么跳转到登陆页面
    现在需要使用shiro框架来实现，怎么实现？
    原理基本一致，shiro提供了一个过滤器DelegatingFilterProxy，我们只需要进行相应的配置