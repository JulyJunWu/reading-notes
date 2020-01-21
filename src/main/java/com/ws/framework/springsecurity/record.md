Spring Security
原理: 基于Servlet的Filter过滤器链实现,同时自身再次构建一个Filter链
DelegatingFilterProxy
	Filter delegate(FilterChainProxy); 
		HttpFirewall firewall; 职责1.防火墙:验证请求方法(GET,POST)是否允许,默认所有的都允许; 2.url合法性验证,是否含有非法字符; 3.ip/host过滤
		List<SecurityFilterChain> filterChains; 自身又构造了一个类似FilterChain的功能,添加多个过滤器在里面对作用在请求上

spring security 自身重新了构建一个FilterChain(VirtualFilterChain),同时也添加了自身所需要的Filter,作用在请求上,当Spring security自身的Filter完毕后,重新调用Servlet 的FilterChain继续过滤下一个servlet的filter
SecurityFilterChain	
	UsernamePasswordAuthenticationFilter: 认证
		DaoAuthenticationProvider
	FilterSecurityInterceptor: 授权
		AccessDecisionManager" 投票是否允许访问资源

