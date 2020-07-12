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
                
            
    