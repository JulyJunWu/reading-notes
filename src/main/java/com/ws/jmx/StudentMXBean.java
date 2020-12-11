package com.ws.jmx;

/**
 * 声明一个 MBean接口(这是一个以固定后缀格式,还有可以是注解形式)
 * <p>
 * 注意如下:
 * 1.方法名必须为Mbean结尾
 * 2.set表示可写
 * 3.get表示可读
 */
public interface StudentMXBean {

    String getName();

    void setName(String name);

    int getAge();


}
