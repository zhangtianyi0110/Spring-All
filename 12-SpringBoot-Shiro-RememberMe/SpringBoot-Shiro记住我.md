## SpringBoot整合Shiro-Remember-记住我

当用户成功登录后，关闭浏览器然后再打开浏览器访问http://localhost:8080/index，页面会跳转到登录页，之前的登录因为浏览器的关闭已经失效。

紧接上一篇教程SpringBoot整合Shiro-Authentication-加密认证，新增rememberMe功能。

### 1.修改ShiroConfig

添加cookie对象和cookie管理器并注入SecurityManager

```java
      /**
     * @return cookie对象
     */
    public SimpleCookie cookie(){
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setMaxAge(24*60*1000);//设置失效时间一天
        return cookie;
    }
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
     * 注入RememberMeManager
     * @return SecurityManager
     */
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm());//注入自定义Realm
        securityManager.setRememberMeManager(cookieRememberMeManager());//注入RememberMeManager
        return securityManager;
    }
```

最后修改权限配置，将ShiroFilterFactoryBean的`filterChainDefinitionMap.put("/**", "authc");`更改为`filterChainDefinitionMap.put("/**", "user");`。`user`指的是用户认证通过或者配置了Remember Me记住用户登录状态后可访问。

```java
        //filterChainDefinitionMap.put("/**", "authc");
        // user指的是用户认证通过或者配置了Remember Me记住用户登录状态后可访问。
        filterChainDefinitionMap.put("/**", "user");
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
                <form class="form-horizontal" οnsubmit="return false;">
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
                    <div class="form-group">
                        <label for="rememberMe" class="col-sm-3 control-label">记住我：</label>
                        <div class="col-sm-9">
                            <input type="checkbox" class="form-control" name="rememberMe" id="rememberMe">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="button" onclick="login()" class="btn btn-default">登录</button>
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


<script th:inline="javascript" type="text/javascript">
    var ctxPath  = [[${#httpServletRequest.getContextPath()}]];

    function login() {
        var username = $("#username").val();
        var password = $("#password").val();
        var rememberMe = $("#rememberMe").val();
        $.ajax({
            type: "post",
            url: ctxPath + "login",
            data: {username,password,rememberMe},
            dataType:"json",
            success:function (result) {
                if(result.code==200){
                    alert(result.msg);
                    window.location.href = ctxPath + 'index'
                }else{
                    alert(result.msg);
                }
            }
        })
    }
</script>
</body>
</html>
```

### 4.修改UserController给token设置rememberMe

```java
    @PostMapping("/login")
    @ResponseBody
    public ResponseData login(User user) throws Exception{
        logger.info("开始认证...");
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        token.setRememberMe(user.isRememberMe());
        subject.login(token);//登录认证
        logger.info("结束认证...");
        return ResponseUtil.success("登录成功");
    }
```

### 5.测试

启动springboot，进入登录页输入正确账号密码。

结果跳转到index页面,页面获取到用户名并显示。

关闭浏览器，再次打开浏览器，访问http://localhost:8080/index不需要再次登录。

