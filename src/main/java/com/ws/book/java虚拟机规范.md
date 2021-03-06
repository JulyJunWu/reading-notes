Java 虚拟机与 Java 语言并没有必然的联系，它只与特定的二进制文件格式——Class文件格式所关联，
Class文件中包含了Java 虚拟机指令集（或者称为字节码、Bytecodes）和符号表，还有一些其他辅助信息。

无论何时，当我们提及某个类或接口的包是 java 或者它的子包（java.*），那就意味着这个
类或接口是由引导类加载器进行加载的;

编译后被 Java 虚拟机所执行的代码使用了一种平台中立（不依赖于特定硬件及操作系统的）
的二进制格式来表示，并且经常（但并非绝对）以文件的形式存储，因此这种格式被称为 Class
文件格式

Java 虚拟机所支持的原始数据类型包括了数值类型（Numeric Types）、布尔类型（BooleanType）和 returnAddress 类型三类。
   returnAddress 类型：
        表示一条字节码指令的操作码（Opcode）。在所有的虚拟机支持的原始类型之中，只有returnAddress 类型是不能直接 Java 语言的数据类型对应起来的。
   returnAddress 类型和值
        returnAddress 类型会被 Java 虚拟机的 jsr、ret 和 jsr_w 指令所使用。
        returnAddress 类型的值指向一条虚拟机指令的操作码。与前面介绍的那些数值类的原始类型不同，
returnAddress 类型在 Java 语言之中并不存在相应的类型，也无法在程序运行期间更改
    整形类型取值范围:
        对于 byte 类型，取值范围是从-128 至 127（-2^7 至 2^7 -1），包括-128 和 127。
        对于 short 类型，取值范围是从−32768 至 32767（-2^15 至 2^15 -1），包括−32768和32767
        对于 int 类型，取值范围是从−2147483648 至 2147483647（-2^31 至 2^31 -1），包括−2147483648 和 2147483647。
        对于 long 类型，取值范围是从−9223372036854775808 至 9223372036854775807（-2^63 至 2^63 -1），包括−9223372036854775808 和 9223372036854775807。
        对于 char 类型，取值范围是从 0 至 65535，包括 0 和 65535。
    boolean 类型
        虽然 Java 虚拟机定义了 boolean 这种数据类型，但是只对它提供了非常有限的支持。在Java 虚拟机中没有任何供 boolean值专用的字节码指令，
在 Java 语言之中涉及到 boolean类型值的运算，在编译之后都使用Java 虚拟机中的 int 数据类型来代替。
        Java 虚拟机直接支持 boolean 类型的数组，虚拟机的newarray 指令可以创建这种数组。
        boolean 的数组类型的访问与修改共用 byte 类型数组的 baload 和 bastore 指令

引用类型与值:
    Java 虚拟机中有三种引用类型：类类型（Class Types）、数组类型（Array Types）和接口类型（Interface Types）。
    这些引用类型的值分别由类实例、数组实例和实现了某个接口的类实例或数组实例动态创建。        
    在引用类型的值中还有一个特殊的值：null，当一个引用不指向任何对象的时候，它的值就用null 来表示。一个为 null 的引用，
在没有上下文的情况下不具备任何实际的类型，但是有具体上下文时它可转型为任意的引用类型。引用类型的默认值就是 null。
    Java 虚拟机规范并没有规定null在虚拟机实现中应当怎样编码表示。
    
局部变量表
    每个栈帧内部都包含一组称为局部变量表（Local Variables）的变量列表。栈帧中局部变量表的长度由编译期决定，并且存储于类和接口的二进制表示之中，
既通过方法的Code 属性保存及提供给栈帧使用。    
    一个局部变量可以保存一个类型为 boolean、byte、char、short、float、reference和 returnAddress 的数据，两个局部变量可以保存一个类型为long和double的数据。
    局部变量使用索引来进行定位访问，第一个局部变量的索引值为零，局部变量的索引值是从零至小于局部变量表最大容量的所有整数。
    
操作码助记符中都有特殊的字符来表明专门为哪种数据类型服务：i 代表对 int 类型的数据操作，l 代表 long，s 代表 short，b 代表 byte，
c 代表 char，f 代表 float，d 代表 double，a 代表 reference。也有一些指令的助记符中没有明确的指明操作类型的字母;

算术指令用于对两个操作数栈上的值进行某种特定运算，并把结果重新存入到操作栈顶。大体上运算指令可以分为两种：
    1.对整型数据进行运算的指令
    2.对浮点型数据进行运算的指令
    无论是那种算术指令，都是使用Java虚拟机的数字类型的。数据没有直接支持byte、short、char和boolean 类型的算术指令，对于这些数据的运算，
 都是使用操作int类型的指令。
 
 jinfo : 查看正在运行的jvm的参数
    如: jinfo pid 查看所有参数
        jinfo -flag MetaspaceSize pid : 查看某个参数值 
        jinfo -flags pid : 查看JVM参数
 java -XX:+PrintFlagsInitial  查看初始JVM的参数
 java -XX:+PrintFlagsFinal    查看修改的JVM的参数       := 表示修改过的参数
 比例:  新生代:老年代 = 1 : 2
 -Xms                       初始堆大小
 -Xmx                       最大堆大小
 -Xss                       线程栈大小,根据平台而定,默认是1024K,但是JVM参数显示是0
 -Xmn                       年轻代大小
 -XX:MetaspaceSize          元空间大小
 -XX:+PrintGCDetails        打印GC日志
 -XX:SurvivorRatio          年轻代的比例, eden:from survivor:to survivor = 8 : 1 : 1 ,也就是说年轻代有可以使用的内存是9 : 1
 -XX:NewRatio               老年代的占比,剩余一份为新生代;默认值2,年轻代与老年代的比例,也就是1:2
 -XX:MaxTenuringThreshold   默认为15,该属性值必须是在0-15之间;存活对象进入老年代年龄(非绝对,如年轻代内存不足,大对象直接进老年代)
 注意:
    当-Xmn与-XX:NewRatio同时存在,则实际值以-Xmn参数为准;
    
 查看默认垃圾收集器: java -XX:+PrintCommandLineFlags -version
 JDK1.8默认使用的GC是 Parallel Scavenge(年轻代) + ParallelOld(老年代) : 就是参数 +XX:+UseParallelGC +XX:UseParallelOldGc
 推荐使用 ParNew + CMS 组合 , 当使用 -XX:+UseConcMarkSweepGC ,则默认使用的年轻代GC是 ParNew
 
 GC参数如下:
    -XX:+UseG1GC
    -XX:+UseSerialGC
    -XX:+UseSerialOldGC
    -XX:+UseParNewGc
    -XX:+UseConcMarkSweepGC
    -XX:+UseParallelGC
    -XX:+UseParallelOldGC
 年轻代GC:  都是基于复制算法
        Serial : 单线程串行 , STW
        ParallelScavenge : 并行,多线程版Serial,关注吞吐量,适用于科学计算
        ParNew : 多线程版Serial
 老年代GC:  
        SerialOld: 废弃 , 基于标记整理算法
        CMS:  基于标记清除算法, 减少STW时间,SerialOld做担保;
              四个阶段: 1.初始标记(STW) 2.并发标记(与用户线程并发执行) 3.重新标记(STW) 4.并发清除
        ParallelOld: 与年轻代ParallelScavenge配合使用
 以及横跨2个代的 G1(通用型)
 
 性能调忧应该看的几个命令Linux:
    uptime : 查看负载,就是查看load average指数
    top    :   整机
        load average : 1.4 0.9 0.41 : 代表 1 分钟,5分钟,15分钟的负载 , 三个数相加 / 3 * 100% > 60% 这说明负载过大 
    iostat : 磁盘IO
    ifstat : 网络IO
    df     : 硬盘
    vmstat : CPU
    free   : 内存
    
 cpu占用过高linux排查:
    1.top命令定位到占用过高cpu的java程序的pid
    2.定位到具体线程,命令: ps -mp pid -o THREAD,tid,time 定位到具体的线程id(10进制的)
    3.将10进制线程id转换为16进制(字母小写),得到16进制的tid
    4.使用jdk自带命令: jstack pid | grep tid -A60
    
 github操作:
    根据匹配度检索 : 
        关键词 in:name         : 搜索项目名包含xxx
        关键词 in:description  : 搜索描述中包含xxx
        关键词 in:readme       : 搜索readme中包含xxx
        组合使用: 关键词 in:name,readme
    根据star检索 :
        格式: 关键词 stars:>数字 , 比如: springBoot stars:>5000
    根据fork检索:
        格式: 关键词 forks:>数字 , 如 springBoot forks:>7000    
    组合使用fork和star 区间检索:
        格式: 关键词 forks/stars:数字..数字
              如 springBoot forks:100..200 -> 意为检索fork数量在100到200之间的含有springBoot的项目
              如 springBoot stars:1000..5000 forks:200..500 -> 意为检索star数在1000到5000之间,fork数在200到500之间的springBoot项目
    awesome检索: 精品项目    
        格式: awesome 关键词 , 如 awesome springBoot
    代码高亮显示一行:
        格式: 代码所在地址 + #L + 代码所在行数  ,如  https://xxxxxx/Hello.java#13
    代码高亮显示一段:
        格式: 代码所在地址 + #L + 开始行 + -L + 结束行 , 如  https://xxxxxx/Hello.java#13-L15
    英文t:
        在项目的根路径,就是code页面 ,按下小写字母t,代码会按照树形排列
    检索指定区域活跃用户:
        格式: 关键字(可无) location:地区(一般为拼音) , 如 location:FuJian
              在这基础上还可以限定语言,如 location:Fujian language:java
  github快捷方式使用地址: https://help.github.com/en/github/getting-started-with-github/keyboard-shortcuts
  
  