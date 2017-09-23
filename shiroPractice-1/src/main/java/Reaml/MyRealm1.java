package Reaml;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

/**
 * Created by Administrator on 2017/9/23.
 */
public class MyRealm1 implements Realm {
    public String getName() {
        return "MyRealm1";
    }

    public boolean supports(AuthenticationToken token) {
        //仅支持UsernamePasswordToken类型的Token
        return token instanceof UsernamePasswordToken;
    }

    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户名
        String username=(String)token.getPrincipal();
        //获取密码
        String password=new String((char[])token.getCredentials());

        if (!"lan@qq.com".equals(username)){
            throw new UnknownAccountException();
        }
        if(!"123".equals(password)){
            throw new IncorrectCredentialsException();
        }

        //如果身份认证验证成功，返回一个AuthenticationInfo实现；
        return new SimpleAuthenticationInfo(username,password,getName());
    }
}
