Java Agent 之 premain:
顾名思义: 在执行main方法之前执行premain方法(有点类似AOP)
使用:
    1.声明一个类含有premain名称的函数,如下:
        public static void premain(String args, Instrumentation ins) {...}
    2.在MANIFEST.MF中指定参数,如下:
        Premain-Class: com.ws.agent.PreMainAgent    
    3.准备好需要测试含有main方法的类
    4.打包:
        4.1:可以将premain类与main分开打包成jar
        4.2也可以将2个类一起打包
        MANIFEST.MF必须要有的参数:
            Main-Class: com.ws.agent.AgentTest(这个就是指定main方法)
            Premain-Class: com.ws.agent.PreMainAgent(这个是执行我们定义的premain类)
    5.执行命令(假设上述2个类是打包在一起!)
       无参数: java -javaagent my-agent.jar -jar my-agent.jar
       有参数: java -javaagent my-agent.jar=Hello -jar my-agent.jar
       注意: -javaagent一定要在-jar前面,否则无效
                
            
Java agent之 agentmain:
    含义: 在java程序启动之后,对正在运行的程序进行attach
    步骤:
        1.声明一个含有public static void agentmain(String args, Instrumentation instrumentation)函数的类
        2.在pom文件设置如下:
                <build>
                    <plugins>
                        <!--这个插件是为了测试java agent-->
                        <plugin>
                            <groupId>org.apache.maven.plugins</groupId>
                            <artifactId>maven-jar-plugin</artifactId>
                            <version>2.3.1</version>
                            <configuration>
                                <archive>
                                    <manifest>
                                        <addClasspath>true</addClasspath>
                                    </manifest>
                                    <manifestEntries>
                                        <!--让maven给我们在MANIFEST.MF文件中生成对应的参数-->
                                        <Agent-Class>com.ws.AgentMain</Agent-Class>
                                        <Premain-Class>com.ws.AgentMain</Premain-Class>
                                        <Can-Redefine-Classes>true</Can-Redefine-Classes>
                                        <Can-Retransform-Classes>true</Can-Retransform-Classes>
                                    </manifestEntries>
                                </archive>
                            </configuration>
                        </plugin>
                    </plugins>
                </build>
        3.获取对应程序PID,抵用VirtualMachine.attach(pid)得到 VirtualMachine对象
        注意:: 可能会出现VirtualMachine类不存在,那么如下做:
            <dependency>
                <groupId>com.sun</groupId>
                <artifactId>tools</artifactId>
                <version>1.8.0</version>
                <scope>system</scope>
                <!--需要修改为自己系统上的路径-->
                <systemPath>D:/jdk_1.8/lib/tools.jar</systemPath>
            </dependency>
         4.使用VirtualMachine对象进行load我们已经打好的agentmain.jar包;
         5.接下来JVM会调用agentmain函数,这样我们就可以获取Instrumentation实例
         6.使用Instrumentation实例加载我们需要重新加载的.class文件(必须内存里存在class对象):
            获取.class的字节数据,文件流读取
            Instrumentation.redefineClasses重点!!!


