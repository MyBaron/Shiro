package LoginTest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Administrator on 2017/9/23.
 */
public class test {



    //通用化测试
    private void Simple(String ini){
        /**
         * 1、收集用户身份/凭证，即如用户名/密码；
         * 2、调用Subject.login进行登录，如果失败将得到相应的AuthenticationException异常，
         *     根据异常提示用户错误信息；否则登录成功；
         * 3、最后调用Subject.logout进行退出操作。
         *
         * 如上测试的几个问题：
         * 1、用户名/密码硬编码在ini配置文件，以后需要改成如数据库存储，且密码需要加密存储；
         * 2、用户身份Token可能不仅仅是用户名/密码，也可能还有其他的，如登录时允许用户名/邮箱/手机号同时登录。
         */


        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<SecurityManager> factory =
                new IniSecurityManagerFactory(ini);

        //2、得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();

        SecurityUtils.setSecurityManager(securityManager);

        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject=SecurityUtils.getSubject();
        UsernamePasswordToken token=new UsernamePasswordToken("lan","123");

        //4.登录，就是身份验证
        try {
            subject.login(token);
        }catch (AuthenticationException e){
            System.out.println("验证失败");
            return;

        }
        System.out.println(subject.getPrincipal().toString());
        //确定用户已经登录
        Assert.assertEquals(true,subject.isAuthenticated());

        //subject.logout();
    }


    @Test
    public void testshiro() {
        //测试一
        Simple("classpath:shiro.ini");

    }
    @Test
    public void testshiro2(){

        //测试二
        Simple("classpath:shiro-realm.ini");

    }
    @Test
    public void testshiro3(){

        //测试三 jdbc
        //表名和字段名不可以有错误
        Simple("classpath:shiro-jdbc-realm.ini");

    }
    @Test
    public void testshiro4(){
        //测试四 Authenticator及AuthenticationStrategy
        //测试 AllSuccessfulStrategy
        //MyRealm 类 :用户名/密码为lan/123 返回lan/123
        //MyReal1 类 : 用户名/密码为lan@qq.com/123 返回lan@qq.com/123
        //MyReal2 类 ：用户名/密码为lan/123  返回lan@qq.com/123

        Simple("classpath:shiro-authenticator-all-success.ini");
        Subject subject=SecurityUtils.getSubject();
        //得到一个身份集合，其包含了Realm验证成功的身份信息
        PrincipalCollection principal=subject.getPrincipals();
        System.out.println("身份信息的数量:"+principal.asList().size());
        for(Object o:principal.asList()){
            System.out.println("获取到的:"+o);
        }
    }





}
