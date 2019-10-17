安装:
    下载nginx-1.8.0.tar.gz安装包;
    解压 tar -zxvf nginx-1.8.0.tar.gz
    到解压目录下执行 ./configure --prefix=/usr/local/nginx(路径为nginx安装目录)
    依次执行make ; make install;
    完事;
    
启动:
    [root@hadoop-01 sbin]# ./nginx
关闭:
    [root@hadoop-01 sbin]# ./nginx -s stop
重启:
    [root@hadoop-01 sbin]# ./nginx -s reload
检查配置是否正确:
    nginx -t
    