package com.ws.jmx;


import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Set;

/**
 * 测试从JMX中取数据
 */
public class TestGetDataFromJmx {

    public static void main(String[] args) throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        // 如果不知道是哪个,那就取所有的数据
        Set<ObjectInstance> mBeans = server.queryMBeans(new ObjectName(""), null);
        for (ObjectInstance o : mBeans) {
            System.out.println(o.getClassName() + o.getObjectName().getDomain());
            // 可以拿到对应mxbean的所有属性
            MBeanInfo mBeanInfo = server.getMBeanInfo(o.getObjectName());
            // 拿到所有字段的描述
            MBeanAttributeInfo[] attributeInfos = mBeanInfo.getAttributes();
            String[] allName = new String[attributeInfos.length];
            int index = 0;
            for (MBeanAttributeInfo m : attributeInfos) {
                allName[index++] = m.getName();
            }
            // 取到对应的属性名称和属性值
            AttributeList attributes = server.getAttributes(o.getObjectName(), allName);
            //打印属性和属性值
            List<Attribute> asList = attributes.asList();
            for (Attribute attribute : asList) {
                System.out.println(attribute.getName() + ":" + attribute.getValue());
            }
        }
    }

}
