### docker安装redis
docker pull redis:6.0.18  
docker run --name redis -p 6379:6379 -d redis/redis-stack-server:latest  
docker ps  

### docker安装mysql（注意把your_password替换成你的密码）
docker pull mysql  
docker run --name mysql-container -p 3306:3306 -e MYSQL_ROOT_PASSWORD=your_password -d mysql  
docker ps  

### docker安装nacos
docker pull nacos/nacos-server  
docker run --name nacos -p 8848:8848 -e MODE=standalone -d nacos/nacos-server  
docker ps  

### nacos访问地址（注意把localhost换成你的服务器ip）
http://localhost:8848/nacos