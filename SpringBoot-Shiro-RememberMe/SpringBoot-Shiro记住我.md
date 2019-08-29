## SpringBoot整合Shiro-Remember-记住我

当用户成功登录后，关闭浏览器然后再打开浏览器访问http://localhost:8080/index，页面会跳转到登录页，之前的登录因为浏览器的关闭已经失效。

紧接上一篇教程SpringBoot整合Shiro-Authentication-加密认证，新增rememberMe功能。

### 1.修改ShiroConfig

添加cookie对象和cookie管理器并注入SecurityManager

```java
    /**
     * 使用CookieRememberMeManager
     * @return CookieRememberMeManager
     */
    public CookieRememberMeManager cookieRememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager =  new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(cookie());//使用simplecookie注入cookie
        return cookieRememberMeManager;
    }
    /**
     * 注入自定义realm
     * @return SecurityManager
     */
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm());//注入自定义Realm
        securityManager.setRememberMeManager(cookieRememberMeManager());//注入RealmManager
        return securityManager;
    }
```

### 2.修改User对象添加rememberMe属性

```java
public class User implements Serializable {

  private long id;
  private String username;
  private String password;
  private boolean rememberMe;
  //get/set 省略
}
```

### 3.修改Login.html，添加checkbox传递rememberMe值

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" th:href="@{/css/login.css}">
    <title>login</title>
</head>
<body>
<div id="app">
    <div class="login">
        <div class="panel panel-default">
            <div class="panel-heading">登录</div>
            <div class="panel-body">
                <form class="form-horizontal" action="/login" method="post">
                    <div class="form-group">
                        <label for="username" class="col-sm-2 control-label">用户名</label>
                        <div class="col-sm-10">
                            <input type="username" class="form-control" id="username" th:name="username" placeholder="请输入用户名">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="password" class="col-sm-2 control-label">密码</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="password" th:name="password" placeholder="请输入密码">
                        </div>
                    </div>
                    <label for="rememberMe">
                        记住我：<input type="checkbox" name="rememberMe" id="rememberMe">
                    </label>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="submit"  class="btn btn-default">登录</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->
<script src="https://cdn.jsdelivr.net/npm/jquery@1.12.4/dist/jquery.min.js"></script>
<!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"></script>
</body>
</html>
```

### 4.修改UserController给token设置rememberMe

```java
    @PostMapping("/login")
    public String login(User user) throws Exception{
        logger.info("开始认证...");
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        token.setRememberMe(user.isRememberMe());
        subject.login(token);//登录认证
        logger.info("结束认证...");
        return "redirect:/index";
    }
```



### 5.shiro核心

新建shrio配置和自定义Realm，以及md5加密处理类。

##### 1.自定义Realm

此处的自定义Realm只重写认证方法，授权等详情请看SpringBoot-Shiro-Authorization

```java
**
 * @ClassName: CustomRealm
 * @Description: 自定义Realm
 * @author zhangtainyi
 * @date 2019/8/27 15:29
 *
 */
public class CustomRealm extends AuthorizingRealm {

    private Logger log = LoggerFactory.getLogger(CustomRealm.class);
    @Resource
    private UserService userService;


    //登录认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.从主体传过来的授权信息中，获取用户名
        String usernmae = (String) authenticationToken.getPrincipal();
        //2.通过用户名到数据库中获取用户数据
        User user = userService.findByUsername(usernmae);
        if(user == null){
            throw new UnknownAccountException("用户名或密码错误");
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(usernmae, user.getPassword(), getName());//构建用户认证信息
        //如果算法加盐了，就需要在返回authenticationInfo对象前设置盐
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(user.getUsername()));
        return authenticationInfo;

    }
    //获取权限
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }
}
```

##### 2.自定义Shiro config

```java
@Configuration
public class ShiroConfig {
    //将自定义realm让spring管理
    @Bean
    public CustomRealm customRealm(){
        CustomRealm customRealm = new CustomRealm();
        //添加算法和迭代
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher("md5");
        matcher.setHashIterations(1);
        customRealm.setCredentialsMatcher(matcher);
        return customRealm;
    }
    //注入自定义realm
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm());
        return securityManager;
    }
    //配置shiro的web过滤器,是shiro的核心配置,shiro的所有功能都基于这个对象
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");//设置登陆页面
        shiroFilterFactoryBean.setSuccessUrl("/index");//设置成功跳转页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");//设置无权限返回页面
		//过滤器链
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }
}
```

- 自定义Realm注入spring容器，添加算法和迭代
- 将自定义Realm注入SecurityManager，并将SecurityManager交由spring管理
- 配置shiro的web过滤器,是shiro的核心配置,shiro的所有功能都基于这个对象，将SecurityManager注入
- 配置shiro的web过滤器的基本配置

##### 3.修改UserController的login方法

```java
    @PostMapping("/login")
    public String login(User user) throws Exception{
        logger.info("开始认证...");
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        token.setRememberMe(user.isRememberMe());
        subject.login(token);//登录认证
        logger.info("结束认证...");
        return "redirect:/index";
    }

```

### 6.测试

启动springboot，进入登录页输入正确账号密码。

结果跳转到index页面,页面获取到用户名并显示。

关闭浏览器，再次打开浏览器，访问http://localhost:8080/index不需要再次登录。

