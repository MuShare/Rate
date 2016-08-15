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

4. favorite
/user/favorite
method: PUT
parameter:
    {"added":[],
     "deleted":[]
    }

5. /user/subscribe
method: POST
parameter:
boolean:
isEnable
isSendEmail
isSendSms
sname
isAbove

String:
from
to

double:
threshold


6. /user/subscribes
method: PUT

parameter:
{
"sid":[]
}

response:

isUpdated boolean

data:
createdOrUpdated:[subscribeBean]

deletedSubscribes:[sids]

rates:[{sid:rate}]


7. /user/add_feedback

method: POST

parameter:
(String)feedback

token(可选)

response:

fid

8. /user/upload_image

method: POST

parameter:

file: 文件

token

response:

文字信息

9. /user/avatar

method: GET (获取头像)

parameter:

int rev(版本)

token

response:

{
isUpdated: boolean

image:

rev:
}


10. /user/verfication_code (获取修改密码的验证码, 发到邮箱, 30分钟有效)

method: GET

parameter:

token

response:

文字信息


11. /user/change_password

method: POST

parameter:

password

vertification_code

token

response:

文字信息

12. /user/change_uname (修改用户名)

method: POST

parameter:

uname

token

response:

文字信息


