DWR说明文档

1. UserService
====

(1)`String register(String uname, String email, String telephone, String password);`

   + return: 用户物理id，为空说明注册失败
   + uname: 用户名
   + email: 邮箱
   + telephone: 电话
   + password: 密码

(2)`public boolean login(String email, String password, HttpSession session)`
+ 用户登录
+ return: 登录是否成功
+ email: 邮箱
+ password: 用户密码
+ session: 当前session

(3) `public boolean editSubscribe(String subscribeSid, String fromCurrencyCid, String toCurrencyCid, String sname, double min, double max, boolean isEnable, boolean isOnce, boolean isSendEmail, boolean isSendSms)`
+ 修改订阅
+ return 是否修改成功 true成功 false失败
+ subscribeSid 订阅id
+ fromCurrencyCid from货币id
+ toCurrencyCid to货币id
+ sname 订阅名称
+ min 最小值
+ max 最大值
+ isEnable 是否激活
+ isOnce 是否只通知一次
+ isSendEmail 是否发送邮件
+ isSendSms 是否发送短信

(4) `public boolean deleteSuscribe(String subscribeSid)`
+ 删除订阅
+ return 是否删除成功
+ subscribeSid 订阅id

(5) `public String addSubscribe(String sname, double min, double max, boolean isEnable, boolean isOnce, boolean isSendEmail, boolean isSendSms, String from, String to, String userId)`
+ 修改订阅
+ return 是否修改成功 成功则返回id 错误则返回为空
+ subscribeSid 订阅id
+ fromCurrencyCid from货币id
+ toCurrencyCid to货币id
+ sname 订阅名称
+ min 最小值
+ max 最大值
+ isEnable 是否激活
+ isOnce 是否只通知一次
+ isSendEmail 是否发送邮件
+ isSendSms 是否发送短信
+ uid 用户id

(6) `public List<SubscribeBean> getSubscribes(HttpSession session)`
+ 获取当前用户所有订阅
+ return 当前用户订阅的List
+ session 当前session

(7) `public UserBean checkSession(HttpSession session)`
+ 检测当前session
+ return 是否有session 有则返回当前用户 无则返回null


2. CurrencyeService


(1) `public List<Currency> getCurrencyList()`
+ 获取当前所有货币列表
+ return 所有货币信息列表



3. RateService====

(1) `public ChartData getHistoryRate(String start, String end, String fromCurrencyId, String toCurrencyId)`
+ 返回两个货币历史比值
+ return ChartData
+ start 开始日期
+ end 结束日期
+ fromCurrencyId from货币id
+ tourrencyId to货币id

(2) `public double getCurrentRate(String fromCurrencyCid, String toCurrencyCid)`
+ 获取两个货币当前时间的比值
+ return double比值
+ fromCurrencyId from货币id
+ tourrencyId to货币id

(3)`public ChartData getSpecificRate`
+获取某一种货币兑美元的比值
+ return ChartData
+ start 开始日期
+ end 结束日期
+ currencyId 货币Id
