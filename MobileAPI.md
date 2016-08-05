1. 用户第一次登录

 /user/login
 method: POST

parameter:

email 邮箱

password 密码

device_token 设备token

os 操作系统

did 设备uuid

response:

login_token

2. 二次登录

/user/login

method: PUT

parameter:
login_token 现有token

device_token 设备token

response:

login_token

3. 注册
/user/register

method: POST

parameter:
uname

email

telephone

password

response:

uid


