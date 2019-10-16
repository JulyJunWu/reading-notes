作者 : 周立

版本:
    ● Spring Boot 1.4.3.RELEASE
    ● Spring Cloud Camden SR4


一个归档包 (例如war格式)包含所有功能的应用程序，通常称为单体应用。而架构单体应用的方法论，就是单体应用架构。

单体应用存在的一-些问题:
    ● 复杂性高: 代码量繁多,模块繁杂,增加或修改bug问题重重,整个项目复杂性非常高
    ● 可靠性差:某个应用Bug,例如死循环、OOM等，可能会导致整个应用的崩溃。
    ● 扩展能力受限:单体应用只能作为一个整体进行扩展，无法根据业务模块的需要进行伸缩。例如，应用中有的模块是计算密集型的，它需要强劲的CPU;有的模块则
是IO密集型的，需要更大的内存。由于这些模块部署在一起，不得不在硬件的选择做出妥协。
    ● 阻碍技术创新:单体应用往往使用统的技术平台或方案解决所有的问题，团队中的每个成员都必须使用相同的开发语言和框架，要想引人新框架或新技术平台会非
常困难。如，一个使用 Struts 2构建的、有100 万行代码的单体应用， 如果想要换用Spring MVC,毫无疑问切换的成本是非常高的。

什么是微服务:
    微服务架构风格是一种将个单一应用程序开发为一组小型服务的方法，每个服务运行在自己的进程中，服务间通信采用轻量级通信机制(通常用HTTP资源API)。
这些服务围绕业务能力构建并且可通过全自动部署机制独立部署。这些服务共用一个最小型的集中式的管理，服务可用不同的语言开发，使用不同的数据存储技术。
    微服务架构应具备以下特性:
        ● 每个微服务可独立运行在自己的进程里。
        ● 一系列独立运行的微服务共同构建起整个系统。
        ● 每个服务为独立的业务开发，一个微服务只关注某个特定的功能，例如订单管理、用户管理等。
        ● 微服务之间通过-些轻量的通信机制进行通信，例如通过RESTful API进行调用。
        ● 可以使用不同的语言与数据存储技术。
        ● 全自动的部署机制。
        
微服务架构有如下优点。
    ● 易于开发和维护:一个微服务只会关注个特定的业务功能，所以它业务清晰、代码量较少。开发和维护单个微服务相对简单。而整个应用是由若千个微服务构建而
成的，所以整个应用也会被维持在一个可控状态。
    ● 单个微服务启动较快:单个微服务代码量较少，所以启动会比较快。
    ● 局部修改容易部署:单体应用只要有修改，就得重新部署整个应用，微服务解决了这样的问题。一般来说，对某个微服务进行修改，只需要重新部署这个服务即可。
    ● 技术栈不受限:在微服务架构中，可以结合项业务及团队的特点，合理地选择技术栈。例如某些服务可使用关系型数据库MySQL;某些微服务有图形计算的需求，可
以使用Neo4j;甚至可根据需要,部分微服务使用Java开发，部分微服务使用Nodejs开发。
    ● 按需伸缩:可根据需求，实现细粒度的扩展。例如，系统中的某个微服务遇到了瓶颈，可以结合这个微服务的业务特点，增加内存、升级CPU或者是增加节点。

使用微服务架构面临的挑战:
    ● 运维要求较高:更多的服务意味着更多的运维投人。在单体架构中，只需要保证一个应用的正常运行。而在微服务中，需要保证几十甚至几百个服务的正常运行与协
作，这给运维带来了很大的挑战。
    ● 分布式固有的复杂性:使用微服务构建的是分布式系统。对于一个分布式系统，系统容错、网络延迟、分布式事务等都会带来巨大的挑战。
    ● 接口调整成本高:微服务之间通过接口进行通信。如果修改某一一个微服务的API,可能所有使用了该接口的微服务都需要做调整。
    ● 重复劳动:很多服务可能都会使用到相同的功能，而这个功能并没有达到分解为一一个微服务的程度，这个时候，可能各个服务都会开发这功能， 从而导致代码重复。

微服务设计原则:
    ● 单一职责原则
    单一职责原则指的是一个单元(类、方法或者服务等)只应关注整个系统功能中单独、有界限的部分。单一职责原则可以帮助我们更优雅地开发、更敏捷地交付。
单职责原则是SOLID原则之一.
    ● 服务自治原则
    服务自治是指每个微服务应具备独立的业务能力、依赖与运行环境。在微服务架构中，服务是独立的业务单元，应该与其他服务高度解耦。每个微服务从开发、测试、
构建、部署，都应当可以独立运行，而不应该依赖其他的服务。
    ● 轻量级通信机制
    微服务之间应该通过轻量级的通信机制进行交互。笔者认为，轻量级的通信机制应具备两点:首先是它的体量较轻;其次是它应该是跨语言、跨平台的。例如我们所
熟悉的REST协议，就是一个典型的 “轻量级通信机制";而例如Java的RMI则协议就不大符合轻量级通信机制的要求，因为它绑定了Java 语言。
微服务架构中，常用的协议有REST、AMQP、STOMP、 MQTT等。
    ● 微服务粒度
    微服务的粒度是难点，也常常是争论的焦点。应当使用合理的粒度划分微服务，而不是一味地把服务做小。代码量的多少不能作为微服务划分的依据，因为不同的微
服务本身的业务复杂性不同，代码量也不同。在微服务的设计阶段，就应确定其边界。微服务之间应相对独立并保持松耦合。

Spring Cloud简介:
    SpringCloud是在SpringBoot基础上构建的，用于快速构建分布式系统的通用模式的工具集。Spring Cloud有以下特点:
      ● 约定优于配置。
      ● 适用于各种环境。开发、部署在PC Server或各种云环境(例如阿里云、AWS等)均可。
      ● 隐藏了组件的复杂性，并提供声明式、无xml的配置方式。
      ● 开箱即用，快速启动。
      ● 轻量级的组件。Spring Cloud整合的组件大多比较轻量。例如Eureka、 Zul,等等，都是各自领域轻量级的实现。
      ● 组件丰富，功能齐全。Spring Cloud为微服务架构提供了非常完整的支持。例如，配置管理、服务发现、断路器、微服务网关等。
      ● 选型中立、丰富。例如，Spring Cloud支持使用Eureka、Zookeeper 或Consul实现服务发现。
      ● 灵活。Spring Cloud的组成部分是解耦的，开发人员可按需灵活挑选技术选型。


@SpringBootApplication是一个组合注解，它整合了@Configuration、@EnableAutoCon-figuration和@ComponentScan注解，
并开启了Spring Boot程序的组件扫描和自动配置功能。在开发Spring Boot程序的过程中，常常会组合使用@Configuration、
@Enable-AutoConfiguration和@ComponentScan等注解，所以Spring Boot提供了@SpringootApp-lication,来简化开发。

Spring Boot Actuator:
    Actuator提供了很多监控端点。可使用http://{ip}:(port}/{endpoint}的形式访问这些端点，从而了解应用程序的运行状况。
    Actuator提供的端点，如下。
          端点                   描述                                                             HTTP方法
        autoconfg       显示自动配置的信息                                                          GET
        beans           显示应用程序上下文所有的Spring bean                                          GET
        confgprops      显示所有@ConfigurationProperties的配置属性列表                               GET
        dump            显示线程活动的快照                                                           GET 
        env             显示应用的环境变量                                                           GET
        health          显示应用程序的健康指标，这些值由HealthIndicator的实现类提供                       GET
        info            显示应用的信息，可使用info.属性自定义info端点公开的数据                           GET
        mappings        显示所有的URI路径                                                            GET
        metrics         显示应用的度量标准信息                                                        GET
        shutdown        关闭应用(默认情况下不启用，如需启用，需设置endpoints shutdownPOSTenabled-true)    POST
        trace           显示跟踪信息(默认情况F为最近100个HTTP请求)                                      GET

服务提供者、服务消费者、服务发现组件三者关系大致如下:
    1.各个微服务在启动时，将自己的网络地址等信息注册到服务发现组件中，服务发现组件会存储这些信息。
    2.服务消费者可从服务发现组件查询服务提供者的网络地址，并使用该地址调用服务提供者的接口。
    3.各个微服务与服务发现组件使用一定机制(例如心跳)通信。服务发现组件如长时间无法与某微服务实列通信，就会注销该实例。
    4.微服务网络地址发生变更(例如实例增减或者IP端口发生变化等)时，会重新注册到服务发现组件。使用这种方式，服务消费者就无须人工修改提供者的网络地址了。

服务发现组件应具备以下功能:
    ● 服务注册表:是服务发现组件的核心，它用来记录各个微服务的信息，例如微服务的名称、IP、端口等。服务注册表提供查询API和管理API,查询API用于查询可用
的微服务实例，管理API用于服务的注册和注销。
    ● 服务注册与服务发现:服务注册是指微服务在启动时，将自己的信息注册到服务发现组件上的过程。服务发现是指查询可用微服务列表及其网络地址的机制。
    ● 服务检查:服务发现组件使用定机制定时 检测已注册的服务，如发现某实例长时间无法访问，就会从服务注册表中移除该实例。

Eureka:
    Eureka是Netlix开源的服务发现组件，本身是个基f REST的服务。它包含Server和Client两部分。Spring Cloud将它集成在子项目Spring Cloud Netlix中，
从而实现微服务的注册与发现。
    Eureka 包含两个组件: Eureka Server和Eureka Client,它们的作用如下:
      ● Eureka Server提供服务发现的能力，各个微服务启动时，会向Eureka Server注册自己的信息(例如IP、端口、微服务名称等), Eureka Server会存储这些信息
      ● Eureka Client是个Java客户端，用于简化与Eureka Server的交互。
      ● 微服务启动后，会周期性(默认30秒)地向EurekaServer发送心跳以续约自己的“租期”
      ● 如果Eureka Server在一定时间内没有接收到某个微服务实例的心跳，Eureka Server将会注销该实例(默认90秒)。
      ● 默认情况下，Eureka Server同时也是Eureka Client。 多个Eureka Server实例，互相之间通过复制的方式，来实现服务注册表中数据的同步。
      ● Eureka Client会缓存服务注册表中的信息。这种方式有一定的优势首先， 微服务无须每次请求都查询Eureka Server,从而降低了Eureka Server的压力;
      其次，即使EurekaServer所有节点都宕掉，服务消费者依然可以使用缓存中的信息找到服务提供者并完成调用。
    Eureka通过心跳检查、客户端缓存等机制，提高了系统的灵活性、可伸缩性和可用性。
    @EnableEurekaClient注解可以替代@EnableDiscoveryClient。在Spring Cloud中，服务发现组件有多种选择，例如Zookeeper、Consul等。
    @EnableDiscoveryClient 为各种服务组件提供了支持，该注解是spring cloud-commons项目的注解，是一个高度的抽象;而@EnableEurekaClient 表明是
Eureka的Client,该注解是spring cloud-netflix项目中的注解，只能与Eureka一起工作。当Eureka在项目的classpath中时，两个注解没有区别。

Eureka的元数据有两种，分别是标准元数据和自定义元数据。
    标准元数据指的是主机名、IP地址、端口号、状态页和健康检查等信息，这些信息都会被发布在服务注册表中，用于服务之间的调用。
    自定义元数据可以使用eureka.instance.metadata-map配置，这些元数据可以在远程客户端中访问，但一般不会改变客户端的行为，除非客户端知道该元数据的含义。

Eureka的自我保护模式:
    默认情况下，如果Eureka Server在一定时间内没有 接收到某个微服务实例的心跳，EurekaServer将会注销该实例(默认90秒)。但是当网络分区故障发生时，微服务与Eureka
Server之间无法正常通信，以上行为可能变得非常危险了因为微服务本身其实是健康的，此时本不应该注销这个微服务。
    Eureka通过“自我保护模式”来解决这个问题一当 Eureka Server节点在短时间内丢失过多客户端时(可能发生了网络分区故障),那么这个节点就会进入自我保护模式。一旦进入该模式
Eureka Server就会保护服务注册表中的信息，不再删除服务注册表中的数据(也就是不会注销任何微服务)。当网络故障恢复后，该Eureka Server节点会自动退出自我保护模式。
    自我保护模式是一种应对网络 异常的安全保护措施。它的架构哲学是宁可同时保留所有微服务(健康的微服务和不健康的微服务都会保留),也不盲目注销任何健康的微服务。
使用自我保护模式，可以让Eureka集群更加的健壮、稳定。
    可以使用eureka.server.enable-self-preservation=false 禁用自我保护模式。

Eureka的健康检查:
    Status=UP,表示应用程序状态正常。应用状态还有其他取值，例如DOWN、OUT _OF SERVICE、UNKNOWN等。只有标记为“UP”的微服务会被请求。
    Eureka Server与Eureka Client之间使用心跳机制来确定Eureka Client的状态，默认情况下，服务器端与客户端的心跳保持正常，应用程序就会始终保持“UP”状态。
以上机制并不能完全反映应用程序的状态。如,微服务与Eureka Server之间的心跳正常，Eureka Server认为该微服务“UP";然而，该微服务的数据源发生了问题(例如因为网络抖动，
连不上数据源)，根本无法正常工作。Spring Boot Actuator提供了/health端点，该端点可展示应用程序的健康信息。
    那么如何才能将该端点中的健康状态传播到Eureka Server呢?
    启用Eureka的健康检查。这样，应用程序就会将自己的健康状态传播到Eureka Server。微服务配置开启如下:
        eureka.client.healthcheck.enabled = true
    某些场景下，可能希望更细粒度地控制健康检查,此时可实现com.netflix.appinfo.HealthCheckHandler接口。

使用Ribbon实现客户端侧负载均衡:
    Ribbon是Netflix发布的负载均衡器，它有助于控制HTTP和TCP客户端的行为。为Ribbon配置服务提供者地址列表后，Ribbon就可基于某种负载均衡算法，自动地帮助
服务消费者去请求。Ribbon 默认为我们提供了很多的负载均衡算法，如轮询、随机等。也可为Ribbon实现自定义的负载均衡算法。
    在Spring Cloud中，当Ribbon与Eureka配合使用时，Ribbon可自动从Eureka Server获取服务提供者地址列表，并基于负载均衡算法，请求其中一个服务提供者实例。
    
   当Ribbon和Eureka配合使用时，会自动将虚拟主机名映射成微服务的网络地址。
   虚拟主机名与虚拟IP非常类似，可将其简单理解成为提供者的服务名称，因为在默认情况下,虚拟主机名和服务名称是一致的。也可使用配置属性eureka.instance.virtual-host-name或
者eureka.instance.secure-virtual-host.name指定虚拟主机名。

Feign简介:
    Feign是Netflix开发的声明式、模板化的HTTP客户端，其灵感来自Retrofit、JAXRS-2.0以及WebSocket。Feign 可帮助我们更加便捷、优雅地调用HTTP API 
在Spring Cloud中，使用Feign非常简单一创建 一个接口，并在接口上添加些注解，代码就完成了。Feign 支持多种注解，例如Feign自带的注解或者JAX RS注解等。
    Spring Cloud对Feign 进行了增强，使Feign支持了Spring MVC注解，并整合了Ribbon和Eureka,从而让Feign的使用更加方便。

Feign对压缩的支持
    一些场景下，可能需要对请求或响应进行压缩，此时可使用以下属性启用Feign的压缩功能。
    feign.compression.request.enabled=true
    feign.compression.response.enabled-true
    对于请求的压缩，Feign 还提供了更为详细的设置，例如:
    feign.compression.request.enabled=true
    feign.compression.request.mime-types=text/xmt,application/xml,application/json
    feign.compression.request.min-request-size=2048
    其中，feign. compression. request .mime types用 于支持的媒体 类型列表，默认是text/xml application/xml以及application/json。
    feign.compression.request.min-request-size 用于设置请求的最小阈值，默认是2048 


使用Hystrix实现微服务的容错处理:
    若服务提供者响应非常缓慢，那么消费者对提供者的请求就会被强制等待，直到提供者响应或超时。在高负载场景下，如果不作任何处理，此类问题可能会导致服务消费者
的资源耗竭甚至整个系统的崩溃。
    我们常把“基础服务故障”导致“级联故障”的现象称为雪崩效应。雪崩效应描述的是提供者不可用导致消费者不可用，并将不可用逐渐放大的过程。
    要想防止雪崩效应，必须有一个强大的容错机制。该容错机制需实现以下两点:
       1.为网络请求设置超时
        必须为网络请求设置超时。正常情况下，-个远程调用一般在几t毫秒内就能得到响应了。如果依赖的服务不可用或者网络有问题，那么响应时间就会变得很长(几十秒)。
        通常情况下，一次远程调用对应着个线程/进程。如果响应太慢，这个线程/进程就得不到释放。而线程/进程又对应着系统资源，如果得不到释放的线程/进程越积越
多，资源就会逐渐被耗尽，最终导致服务的不可用。
        因此，必须为每个网络请求设置超时，让资源尽快释放。
       2.使用断路器模式(断路器就把它理解成家里的电路的断路器,当电流过大时,切断电流,就是常说的跳闸)
        断路器可理解为对容易导致错误的操作的代理。这种代理能够统计一段时间内调用失败的次数，并决定是正常请求依赖的服务还是直接返回。
        断路器可以实现快速失败，如果它在段时间内检测到许多类似的错误(例如超时),就会在之后的一段时间内，强迫对该服务的调用快速失败，即不再请求所依赖的服
务。这样，应用程序就无须再浪费CPU时间去等待长时间的超时。
        断路器也可自动诊断依赖的服务是否已经恢复正常。如果发现依赖的服务已经恢复正常，那么就会恢复请求该服务。使用这种方式，就可以实现微服务的“自我修
复”,当依赖的服务不正常时打开断路器时快速失败，从而防止雪崩效应;当发现依赖的服务恢复正常时，又会恢复请求。

  断路器状态转换:
      正常情况下，断路器关闭，可正常清求依赖的服务。
      当段时间内， 请求失败率达到一定阈值(例如错误率达到 50%，或100 次/分钟等),断路器就会打开。此时，不会再去清求依赖的服务;
      断路器打开一段时间后，会自动进入“半开”状态。此时，断路器可允许一个请求访问依赖的服务。如果该请求能够调用成功，则关闭断路器;否则继续保持打开状态。

Hystrix简介:
    Hystrix是由Netlix开源的个延迟和容错库，用于隔离访问远程系统、 服务或者第三方库，防止级联失败，从而提升系统的可用性与容错性。
    Hystrix 主要通过以下几点实现延迟和容错:
        包裹请求: 使用HystrixCommand (或HystrixObservableCommand )包裹对 依赖的调用逻辑，每个命令在独立线程中执行。这使用到了设计模式中的“命令模式。
        跳闸机制:当某服务的错误率超过一定阈值时，Hystrix 可以自动或者手动跳闸，停止请求该服务段时间。
        资源隔离: Hystrix 为每个依赖都维护了一个小型的线程池(或者信号量)。如果该线程池已满，发往该依赖的请求就被立即拒绝，而不是排队等候，从而加速失败判定。
        监控: Hystrix可以近乎实时地监控运行指标和配置的变化，例如成功、失败、超时、以及被拒绝的请求等。
        回退机制:当请求失败、超时、被拒绝，或当断路器打开时，执行回退逻辑。回退逻辑可由开发人员自行提供，例如返回一个缺省值。
        自我修复:断路器打开一段时间后，会自动进入“半开”状态。断路器打开、关闭，半开的逻辑转换;

断路器的状态也会暴露在Actuator 提供的/health端点中，这样就可以直观地了解断路器的状态。
执行回退逻辑并不代表断路器已经打开,这是因为失败率还没有达到阈值(默认是5秒内20次失败)。请求失败、超时、被拒绝以及断路器打开时等都会执行回退逻辑。

Hystrix的隔离策略有两种:分别是线 程隔离和信号量隔离。
    THREAD(线程隔离):使用该方式，HystrixCommand将会在单独的线程上执行，并发请求受线程池中的线程数量的限制，
    SEMAPHORE(信号量隔离):使用该方式，HystrixCommand将会在调用线程上执行，开销相对较小，并发请求受到信号量个数的限制。
    Hystrix中默认并且推荐使用线程隔离( THREAD),因为这种方式有一个除网络超时以外的额外保护层。
    一般来说，只有当调用负载非常高时(例如每个实例每秒调用数百次)才需要使用信号量隔离，因为这种场景下使用THREAD开销会比较高。信号量隔离般仅适用于非网
络调用的隔离。
    可使用execution.isolation.strategy=THREAD 属性指定隔离策略。

feign整合Hystrix,Spring Cloud默认已为Feign整合了Hystrix,只须使用@FeignClient注解的fallback属性即可
通过@FeignClient注解的FallbackFactory检查回退原因,实现FallbackFactory接口即可;
@FeignClient注解的属性fallback和fallbackFactory二选一,前者不提供错误异常,后者提供错误异常;

Feign全局禁用Hystrix。只须在application.yml中配置feign.hystrix.enabled=false即可。

Hystrix的监控:
    添加spring-boot-starter-actuator依赖,就可使用/hystrix.stream端点(http://ip:port/hystrix.stream)获得Hystrix的监控信息了。

使用Zul构建微服务网关:

  客户端直接与各个微服务通信，会有以下的问题:
    ●客户端会多次请求不同的微服务，增加了客户端的复杂性。
    ●存在跨域请求，在一定场景下处理相对复杂。
    ●认证复杂，每个服务都需要独立认证。
    ●难以重构，随着项目的迭代，可能需要重新划分微服务。例如,可能将多个服务合并成一个或者将一个服务拆分成多个。如果客户端直接与微服务通信，那么重构将
会很难实施。
    ●某些微服务可能使用了防火墙/浏览器不友好的协议，直接访问会有-定的困难。
  以上问题可借助微服务网关解决。微服务网关是介于客户端和服务器端之间的中间层,所有的外部请求都会先经过微服务网关。
                     ↗  用户服务
        用户->服务网关 ->  订单服务
                     ↘  库存服务
  如上，微服务网关封装了应用程序的内部结构，客户端只须跟网关交互，而无须直接调用特定微服务的接口。这样，开发就可以得到简化。不仅如此，使用微服务网关还有以
下优点:
  ● 易于监控。可在微服务网关收集监控数据并将其推送到外部系统进行分析。
  ● 易于认证。可在微服务网关上进行认证，然后再将请求转发到后端的微服务，而无须在每个微服务中进行认证。
  ● 减少了客户端与各个微服务之间的交互次数。

Zuul简介:
   Zuul是Netflix开源的微服务网关，它可以和Eureka、Ribbon、Hystrix 等组件配合使用。
   Zuul的核心是一系列的过滤器， 这些过滤器可以完成以下功能。
      ● 身份认证与安全:识别每个资源的验证要求，并拒绝那些与要求不符的请求。
      ● 审查与监控:在边缘位置追踪有意义的数据和统计结果，从而带来精确的生产视图。
      ● 动态路由:动态地将请求路由到不同的后端集群。
      ● 压力测试:逐渐增加指向集群的流量，以了解性能。
      ● 负载分配:为每种负载类型分配对应容量，并弃用超出限定值的请求。
      ● 静态响应处理:在边缘位置直接建立部分响应，从而避免其转发到内部集群。
      ● 多区域弹性:跨越AWS Region进行请求路由，旨在实现ELB ( Elastic Load Balancing )使用的多样化，以及让系统的边缘更贴近系统的使用者。
  Spring Cloud对Zuul进行了整合与增强。目前,Zuul使用的默认HTTP客户端是Apache HTTP Client,也可以使用RestClient或者okhttp3.0kHttpClient;
如果想要使用RestClient,可以设置rbbon.restclient.enabled=true;想要使用okhttp3.0kHttpClient,可以设置ribbon.okhttp.enabled=true。
    默认情况下，Zuul 会代理所有注册到Eureka Server的微服务，并且Zuul的路由规则如下:
    http://ZUUL_ HOST:ZUUL PORT/微服务在Eureka上的serviceId/** 会被转发到serviceId对应的微服务。
    访问http://ZUUL_ HOST:ZUUL PORT/routes 可以看到zuul所能转发的微服务列表;

自定义zuul配置:
   1.自定义指定微服务 的访问路径。
    配置zuul.routes.指定微服务的serviceId=指定路径即可。例如:
    zuul.routes.microservice-provider-user=/user/**
    这样设置microservice-provider-user微服务就会被映射到/user/**路径。
   2.忽略指定微服务。
    忽略服务非常简单，可以使用zuul.ignored-services配置需要忽略的服务，多个用逗号分隔。使用 '*' 忽略所有微服务例如:
    zuul.ignored-services=serviceId1,serviceId2
   3.使用正则表达式指定Zuul的路由匹配规则,借助PatternServiceRouteMapper,实现从微服务到映射路由的正则配置。.
   4.忽略某些路径,更细粒度的路由控制。例如，想让Zul代理某个微服务，同时又想保护该微服务的某些敏感路径。此时，
可使用ignored-Patterns,指定忽略的正则。例如:
    zuul.ignoredPatterns=/**/admin/** #忽略所有包含/admin/的路径

header传播/禁止: 说白了就是需要zuul转发到目标微服务的头部
    使用zuul.sensitive-headers=Cookie,Set-Cookie,Authorization 全局指定敏感Header(传播头部);
    使用zuul.ignored-headers: header1,header2 禁止某些头部传播;
    可以更细粒度为每个微服务指定需要传播的header;
    默认情况下,zuul.ignored-headers是空值，但如果Spring Security在项目的classpath中，那么zuul.ignored-headers的默认值就是
Pragma ,Cache-Control,X-Frame -Options,X-Content-Type-0ptions,X-XSS-Protection,Expires。所以，当Spring Security在项目的classpath中，
同时又需要使用下游微服务的Spring Security的Header时，可以将zuul.ignoreSecurity-Headers设置为false。
    
zuul上传文件:
    对于小文件( 1M以内)上传，无须任何处理，即可正常上传。对于大文件( 10M以上)上传，需要为上传路径添加/zuul前缀,否则报错(因为断路器问题)。也可使用zuul.servlet-path 自定义前缀。
        
zuul过滤器:
    Zuul大部分功能都是通过过滤器来实现的。Zuul中定义了4种标准过滤器类型，这些过滤器类型对应于请求的典型生命周期。
    ● PRE:这种过滤器在请求被路由之前调用。可利用这种过滤器实现身份验证、在集群中选择请求的微服务、记录调试信息等。
    ● ROUTING:这种过滤器将请求路由到微服务。这种过滤器用于构建发送给微服务的请求，并使用Apache HttpClient或Netfilx Ribbon请求微服务。
    ● POST:这种过滤器在路由到微服务以后执行。(应该是调用完微服务返回后)这种过滤器可用来为响应添加标准的HTTP Header、收集统计信息和指标、将响应从微服务发送给客户端等。
    ● ERROR:在其他阶段发生错误时执行该过滤器。
    除了默认的过滤器类型，Zuul 还允许创建自定义的过滤器类型。


    
    

    