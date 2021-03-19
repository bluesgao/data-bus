# 部署步骤
## 目标数据库
1，用户权限设置
GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ,LOCK TABLES ON . TO 'user' IDENTIFIED BY 'password';）
2，设置binlog为row模式

## canal配置
1，cd /$CANAL_HOME/conf
2，修改canal.properties文件（设置mq连接信息）
3，新建目录，并新建instance.properties文件
a.被监听库的连接配置
b.被监听表配置
c.binlog发送到的mq的主题。

## 应用配置
1，设置mq消费配置。
2，设置规则文件配置。
