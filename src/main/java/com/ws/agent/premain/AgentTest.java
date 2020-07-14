package com.ws.agent.premain;

/**
 * @Description: 测试agent类
 * @Author: JulyJunWu
 * @Date: 2020/7/12 15:50
 *
 *
 *  pom配置如下:
 *                  <plugin>
 *                 <groupId>org.apache.maven.plugins</groupId>
 *                 <artifactId>maven-jar-plugin</artifactId>
 *                 <version>2.3.1</version>
 *                 <configuration>
 *                     <archive>
 *                         <manifest>
 *                             <addClasspath>true</addClasspath>
 *                         </manifest>
 *                         <manifestEntries>
 *                             <!--让maven给我们在MANIFEST.MF文件中生成对应的参数-->
 *                             <Premain-Class>com.ws.agent.PreMainAgent</Premain-Class>
 *                             <!--<Agentmain-Class>com.ws.agent.MyAgent</Agentmain-Class>-->
 *                             <!--设置main方法-->
 *                             <Main-Class>com.ws.agent.AgentTest</Main-Class>
 *                         </manifestEntries>
 *                     </archive>
 *                 </configuration>
 *             </plugin>
 */
public class AgentTest {
    public static void main(String[] args) {
        System.out.println("===========AgentTest start===========");
        System.out.println("测试AgentTest");
        if (args != null) {
            for (String a : args) {
                System.out.println(a);
            }
        }
        System.out.println("===========AgentTest end===========");
    }
}
