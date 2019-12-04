netty 使用4.1.10 不要使用5 , 5已经废弃了

netty也可以作为一个servlet服务器

webSocket是解决HTTP无状态的问题,也就是说webSocket是一个长连接; 构建于HTTP协议之上的升级协议;


Google ProtoBuf:
    使用protobuf编译器生成代码 , 注意:千万不要去修改生成的代码!
    protoc --java_out=./ ./Student.proto    

RPC : Remote Produce Call , 远程调用过程 , 很多RCP框架都是跨语言的;


学习新技术: 直接去官方网站

P17 - P31暂时不看

字节流的输入流和输出流的基础是inputStream和outputStream这两个抽象类,字节流的输入和输出都是这两个的子类实现的;
字符流是JAVA1.1后新增加的以字符为单位进行输入和输出处理的流,字符流的基础是抽象类Reader和Writer(最底层还是字节流|);

I/O流的链接
    文件 -> 文件流(FileInputStream) -> 增加一个缓冲(BufferInputStream) -> 增加读取基本类型的功能(DataInputStream) -> 数据 ;
    数据 -> 输出基本数据类型(DataOutputStream) -> 缓冲(BufferInputStream) -> FileOutputStream 写入到文件中
    
装饰模式:
    装饰类与被装饰类实现同一个接口(感觉有点像代理模式) , 扩展功能 , 避免越来越多的子类
java.io最为核心的概念是流(stream),面向流的编程.在java.io中,一个流要么是输入流,要么是输出流,不可能同时既是输出流又是输入流;
java.nio拥有3个核心概念,Selector,Channel,Buffer;
    在NIO中,我们是面向块(block)或是缓冲区(buffer)编程的.Buffer本身就是一块内存,底层实现上,实际是个数组.数据的读写都是通过Buffer来实现的;
    除数组外,Buffer还提供了对于数据的结构化访问方式,并且可以追踪到系统的读写过程;
    基本数据类都有各自对于的Buffer类型,如IntBuffer,LongBuffer;
    Channel 指的是可以向其写入数据或是从中读取数据的对象,类似于java.io中的stream;
    所有数据的读写都是通过Buffer来进行的,永远不会出现直接向channel写入数据或是读取数据的情况;
    与stream不同的是,Channel是双向的,一个流可能是InputStream或是OutputStream,Channel打开后则可以进行读取,写入;
    
  Buffer : position limit capacity  
    mark 和 reset成双出现
    0 <= mark <= position <= limit <= capacity (还有个offset很少用,默认0)
    clear : 恢复成最初始状态
    flip : 读写转换, 就是将 position赋给limit , 然后position变成0 , mark变为-1
    堆内/堆外内存 Heap / Direct
    

Buffer
    address表示堆外地址(只有直接缓冲才有,DirectorBuffer)
    HeadBuffer 字节数组在堆内, 与IO打交道的时候需要将堆内中的字节数组拷贝一份到操作系统中,然后操作系统中才能与IO打交道;
    DirectBuffer省去了拷贝堆内字节到操作系统(0拷贝),而是直接在操作系统开辟一块空间与IO交互;
    
使用windows telnet ip port 只能发送一个字符, 进步命令行使用ctrl + ]就可以发送完整的字符串 , send 内容 


编码:
    ASCII 美国信息标准交换标准代码
        7bit来表示一个字符,共计可以表示128字符;
    ISO-8859-1
        8bit表示一个字符,即用一个字节(byte) 来表示一个字符,共计可以表示256个字符
    gb2313(国标)
        两个字节表示一个汉字
    gbk (对gb2313的扩展)   
    gb18030 
    unicode 采用两个字节来表示一个字符
    UTF Unicode Translation Format
        unicode是一种编码方式, UTF是一种存储方式;UTF-8是Unicode的实现方式之一;
    UTF-8 变成字节表示形式
     一般来说,UTF-8会通过3个字符来表示一个中文;
    BOM
    
零拷贝机制 : 操作系统提供了那么就有;操作系统不提供,那么无济于事;
    用户空间 内核空间 硬件
    
Reactor模式:
    netty整体架构是Reactor模式的
    boss监听 OP_ACCEPT事件,该事件一个客户端值触发一次
    work监听OP_READ和OP_WRITE事件,如果业务逻辑比较耗时,那么在handler的channelRead0中我们可能需要格外的线程池去执行我们的业务代码;
    netty中有一个对缓冲区预判的类,根据上一次记录对缓冲区(ByteBuffer)大小进行增大或减少(减少是根据前两次),缓冲区也是有最大值和最小值限制的,最小为64,默认为1024,最大为65536字节,可伸缩缓冲区类(AdaptiveRecvByteBufAllocator)
    对于缓冲区是否使用堆内存还是堆外内存,是根据是否有系统属性或者其他决定的,使用堆外内存其实底层还是通过ByteBuffer.allocateDirect(容量数)进行分配缓冲区大小的;
        如果是堆内存的话,底层其实是直接new一个byte数组(MaxMessageHandle.allocate方法可以看到缓冲区具体分配的细节);
    DefaultChannelConfig : Netty配置类
  
Reactor模式的角色构成(五种角色)
    1.Handle(句柄或是描述符):本质上表示一种资源是由操作系统提供的,该资源用于表示一个个的事件,比如说文件描述符或是针对网络编程中的Socket描述符.事件既可以来自于外部,也可以来自于内部;外部事件比如说客户端的连接请求,客户端发送过来的数据等;内部事件比如说操作系统产生的定时器事件等.它本质上就是一个文件描述符.Handle是事件产生的发源地;
    2.Synchronous Event Demultiplexer(同步事件分离器):它本身是一个系统调用,用于等待事件的发生(事件可能是一个,可能是多个).调用方在调用它的时候灰被阻塞,一直阻塞到同步事件分离器上有事件产生为止.对于Linux来说,同步事件分离器指的就是常用的I/O多路复用机制,比如说select,poll,epoll等.在Java NIO领域中,同步事件分离器对应的组件就是Selector;对应的阻塞方法就是select();
    3.Event Handler(事件处理器): 本身由多个回调方法构成.这些回调方法构成了与应用相关的对于某个事件的反馈机制.Netty相对于Java Nio来说,在事件处理器这个角色上进行了升级,它为我们提供了大量的回调方法.供我们在特定事件产生时实现相应的回调方法进行业务逻辑的处理;
    4.Concrete Event Handler(具体时间处理器):是事件处理器的实现.它本身实现了事件处理器所提供的各个回调方法,从而实现了特定于业务的逻辑.它本质上就是我们所编写的一个个的处理器实现;
    5.Initiation Dispatcher(初始分发器):实际上就是Reactor角色,它本身定义了一些规范,这些规范用于控制事件的调度方式,同时又提供了应用进行事件处理器的注册,删除等设施.它本身是整个事件处理器的核心所在,Initiation Dispatcher会通过同步事件分离器来等待事件的发生.一旦事件发生,Initiation Dispatcher首先会分离出每一个事件,然后调用事件处理器,最后调用相关的回调方法来处理这些事件;

Reactor模式的流程:
    1.当应用想Initiation Dispatcher注册具体的事件处理器时,应用会标识出该事件处理器希望Initiation Dispatcher在某个事件发生时向其通知的事件,该事件与Handler关联;
    2.Initiation Dispatcher会要求每个事件处理器向其传递内部的Handle,该Handle向操作系统标识了事件处理器;   
    3.当所有的事件处理器注册完毕后,应用汇调用handle_events方法来启动Initiation Dispatcher的事件循环.这时,Initiation Dispatcher 会将每个注册的事件处理器的Handle合并起来,并使用同步事件分离器等待这些事件的发生.比如说,TCP协议层会使用select同步事件分离器来等待客户端发送的数据到达连接的socket handle上;
    4.当与某个事件源对应的Handle变为ready状态时(比如说,TCP socket变为等待读状态)同步事件分离器就会通知Initiation Dispatcher.
    5.Initiation Dispatcher会触发事件处理器的回调方法,从而相应这个处于ready状态的Handle.当事件发生时,Initiation Dispatcher会将被事件源激活的Handle作为key来寻找并分发恰当的事件处理器回调方法; 
    
   Netty中所有的I/O操作都是异步的;
Netty的管道流(ChannelPipeLine)是更加高级的过滤器模式,它将进出两者分离,请求进来只会有InBound(,read,实现了ChannelInboundHandler接口)处理,请求响应回去(write)只会由OutBound(实现了ChannelOutboundHandler接口)处理; 处理器处理的顺序如下:
    假设PipeLine有如下处理器:
         ChannelPipeline} p = new DefaultChannelPipeLine();
         p.addLast("1", new InboundHandlerA());
         p.addLast("2", new InboundHandlerB());
         p.addLast("3", new OutboundHandlerA());
         p.addLast("4", new OutboundHandlerB());
         p.addLast("5", new InboundOutboundHandlerX());
         那么最终请求进栈的顺序是 1 2 5
         请求返回的顺序是 5 4 3
         将事件(请求事件,比如读事件,写事件)传递给下一个处理器是通过ChannelHandlerContext;
         详细文档,请看ChannelPipeline源码
Netty不建议将耗时的业务逻辑在Handler中处理,因为这样会将netty的i/o线程blocked,所以netty提供了
    ChannelPipeline addLast(EventExecutorGroup group, ChannelHandler... handlers);
    用户可以构建一个线程池,将耗时的业务逻辑用线程池来进行提交运行;
    ChannelPipeline是线程安全的;
    ChannelHandlerContext:是一种链表形式结构,包含了next和pre的节点,所有在ChannelPipeline只需要一个Context就可以得到所有的Context;
    ChannelHandlerContext与ChannelHandler是一对一的关系,当一个Handler加入到Pipeline中,就会new 一个新的Context对象(Context包含Handler),随后加入在ChannelPipeline的ChannelHandlerContext倒数第二个位置上(tail.prv,tail是不会变得,新加入的Handler都是加入在tail直面的)
    ChannelPipeline严格意义来说未持有ChannelHandler对象,但是可以通过ChannelHandlerContext来获取ChannelHanlder;
    
   ChannelPipeline,ChannelHandlerContext,ChannelHandler三者关系:
        Pipeline持有Context对象,并且可以通过Context获取下一个或上一个Context(next,prev,其实就是链表结构);
        Context持有Handler对象,与之是一对一关系;
        大概如下::
       ChannelPipeline{
            ChannelHandlerContext context;
        }
       ChannelHandlerContext{
            ChannelHandlerContext next;
            ChannelHandlerContext prev;
            ChannelHandler handler;
        }
        
   ChannelInitializer接口只是提供对多个Handler进行包装;在Channel注册后,initChannel方法只会调用一次,就从PipeLine中移除;它的作业就是将initChannel的多个Handler与Chanel关联,当所有的Handler处理完毕后也就失去了作用,然后被移除(移除就是从ChannelHandlerContext这个双向链表中移除)
   在Netty4.0的版本中,Channel和ChannelHandlerContext各自拥有自己的AttributeMap(本质就是一个map存储),注意:是一个Context拥有一个AttributeMap熟悉,
   也就是说有多少个Handler就有多少个AttributeMap属性,比如:一个Channel,拥有10个Handler,那么久拥有10个Context,一共11个Attribute属性;
   在Netty新版本之后改变了这一做法,全部Handler各自的Attribute属性已经统一使用Channel的Attribute了,也就是一个Channel不管拥有多少个Handler,它只有一个Attribute,现在ChannelContextHandler.attr()底层就是调用channel.attr()的;  第一个是为了减少开发者的疑惑,第二个是为了避免内存的浪费;
    
  EventLoopGroup,EventLoop,Channel三者关系:
    1.一个EventLoopGroup包含一个或多个EventLoop;
    2.一个EventLoop在他的整个生命周期当中只会与唯一一个Thread进行绑定;
    3.所有由EventLoop所处理的I/O事件都将在与它所关联的Thread上进行的;
    4.一个channel在它的整个生命周期中只会注册到一个EventLoop;
    5.一个EventLoop在运行过程当中,会被分配给一个或多个Channel;
    
JDK所提供的的Future接口只能通过手动方式检查结果,而这个操作时会阻塞的;
Netty则对ChannelFuture进行了增强,通过ChannelFutureListener以回调的方式来获取执行结果,去除手动检查阻塞的操作;值得注意的是:ChannelFutureListener的operationComplete方法是由I/O线程执行的,因此要注意的是不要在这里执行耗时操作,否则需要通过另外的线程或线程池来执行;
   Channel的writeAndFlush()与ChannelHandlerContext的writeAndFlush()比较:
   假设有Handler如下 : 1 2 3 4 5
     channel的则会调用最后的一个handler到第一个handler,则顺序为5 4 3 2 1(可以看看源码,源码是从tail开始);
     context(假设此处是在3的handler中调用writeAndFlush)则会调用 2 1 (源码是从当前context的prev开始的);
  
 ByteBuf : 类似于NIO中的ByteBuffer结构, 拥有 readIndex(读索引) <= writeIndex(写索引) <= capacity 属性; 
    判断是否堆上缓冲 : ByteBuf.hasArray();
 Netty的三种缓冲区:
    1.heap buffer;
    2.direct buffer;
    3.composite buffer (复合缓冲区,一种容器,可以存放heap和director buffer);
    
  HeapBuffer(堆缓冲区):
    这是最常用的类型,ByteBuf将数据存储到JVM的堆空间,并且将实际的数据存放到byte array中来实现;
    优点:由于数据是存储到JVM的堆中,因此可以快速的创建与快速的释放,并且它提供了直接访问内部字节数组的方法;
    缺点:每次读写数据时,都需要先将数据复制到直接缓冲区中再进行网络传输;
    
  DirectBuffer(直接缓冲区):
    在堆之外直接分配内存,直接缓冲区并不会占用堆的容量空间,因为它是由操作系统在本地进行的数据分配;
    优点:在使用Socket进行数据传递时,性能非常好,因为数据直接位于操作系统的本地内存中,所有不需要从JVM将数据拷贝至直接缓冲区,性能很好;
    缺点:因为Direct Buffer是直接在操作系统内存中的,所有内存空间的分配与释放要比堆空间更复杂,而且速度慢一些;
    Netty通过提供内存池来解决这个问题,直接缓冲区并不支持通过字节数组的方式进行访问数据;
  
 JDK的ByteBuffer与netty的ByteBuf差异:
    1.netty采用了读写索引分离策略(readIndex和writeIndex) , 一个初始化(里面未包含任何数据)的ByteBuf的readIndex和writeIndex都是为0;
    2.当读索引与写索引处于同一个位置时,再次读取则抛出IndexOutOfBoundException;
    3.对于ByteBuf任何读写操作都会单独维护读索引和写索引.最大的容量是Integer.MAX_VALUE;
    4.ByteBuf是自动扩容的;
  JDK的ByteBuffer的缺点:
    1. final byte [] hb ; 这是JDK的bYteBuffer对象中用于存储数据的对象声明,可以看到,其字节数组是被声明为final的,也就是说长度是固定不变的,不支持动态扩容;
     ,如果ByteBuffer空间不足,我们只能重新创建一个更大的的新ByteBuffer,将旧的ByteBuffer拷贝至新的ByteBuffer;
    2. ByteBuffer使用一个指针来标志位置信息,进行读写时就需要调用flip()
    
 计数器 : AtomicIntegerFieldUpdater  AtomicIntegerFieldUpdater.newUpdater(Person.class, "age");
     参数1 : 指定的泛型的class对象 , 参数二 : 需要更新的字段名称;
     使用前提要求 : 1.被更新的字段修饰符不能是 private
                  2.被更新的字段必须是int类型
                  3.被更新的字段必须是volatile修饰(理volatile 的可见性和禁止指令重排序)
     ByteBuf谁最后使用,那么就是谁释放
     ByteBuf的衍生ByteBuf是与父ByteBuf共享同一份地址的,因此是不会使引用计数增加, 创建衍生ByteBuf方法 : slice() , duplicate() , readSlice(int)
  
 自定义netty解码器(入栈处理器) 继承 ByteToMessageDecode , 解码时需要注意可读的长度是否足够!
 自定义netty编码器(出栈处理器)继承 MessageToByteEncode
 自定义编码器请看demo5(com.ws.netty.demo5);
 
 更好用的自定义编码器 , 继承 ReplayingDecoder (ByteToMessageDecode的抽象子类),我们可以不用关心读取的长度是否足够
 
 自定义协议(com.ws.netty.demo6)
    
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    







