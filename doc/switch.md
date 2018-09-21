1. 集群节点任务分配降级开关  
GET http://localhost:8080/tnt-bms-admin/systatus/switchTaskAssign?isTurnOff=false|true  

2. 刷新集群节点任务分配  
GET http://localhost:8080/tnt-bms-admin/systatus/flushTaskAssign

3. 刷新接入配置缓存
GET http://localhost:8080/tnt-bms-admin/config/access/reloadCache

4. 清理无效TOKEN的推送数据
GET http://localhost:8080/tnt-bms-admin/data/clearMsgToken/{tokenId}

5. 调整pusher线程参数
GET http://localhost:8080/tnt-bms-admin/data/changeParams?fastWorkThreads=500&slowWorkThreads=300

#### 分销推送管理系统账号
查询账号：guest/123456