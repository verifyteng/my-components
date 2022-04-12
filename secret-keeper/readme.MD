# 简介
面对越来越多的账户信息,人脑记忆显得吃力,一般我们习惯于把账户抄写下来或者以明文文件的形式保存在硬盘上,但是这是很不安全的。如果能够把明文文件进行加密,那么我们只需要记住一个密钥，
就可以既保证信息的安全性，又减少了我们记忆的压力。
# 使用方式
## 示例
### 查看帮助
java -jar secret-keeper.jar -h
### 加密示例
java -jar secret-keeper.jar -r /tmp/myfile -w /tmp/myfile -m e -p 123456
### 解密查看示例
java -jar secret-keeper.jar -r /tmp/myfile -c -m d -p 123456
### 解密文件并覆盖密文示例
java -jar secret-keeper.jar -r /tmp/myfile -w /tmp/myfile -m d -p 123456
### 直接在密文后追加明文记录示例
java -jar secret-keeper.jar -s "这是追加的一行内容" -w /tmp/myfile -m ea -p 123456

# 运行环境
需要安装jdk1.8及以上版本

# 注意点
密钥永远不会被保存,只有加密文件的人知道,一旦密钥丢弃,则文件永远无法解密
