package com.tester.cases;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tester.config.TestConfig;
import com.tester.log.LogControler;
import com.tester.model.InterfaceName;
import com.tester.model.LoginCase;
import com.tester.utils.ConfigFile;
import com.tester.utils.DatabaseUtil;
import com.tester.utils.HttpRequestUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
public class LoginTest {
    final static LogControler Log = LogControler.getLogger(LoginTest.class);


    @BeforeTest(groups = "loginTrue",description = "测试准备工作,获取HttpClient对象")
    public void beforeTest(){
        TestConfig.defaultHttpClient = new DefaultHttpClient();
        TestConfig.loginUrl = ConfigFile.getUrl(InterfaceName.LOGIN);
        TestConfig.addBrandUrl = ConfigFile.getUrl(InterfaceName.ADDBRAND);
        TestConfig.getBrandListUrl = ConfigFile.getUrl(InterfaceName.GETBRANDLIST);
        TestConfig.updateBrandInfoUrl = ConfigFile.getUrl(InterfaceName.UPDATEBRANDINFO);
        TestConfig.deleteBrandInfoUrl = ConfigFile.getUrl(InterfaceName.DELETEBRANDINFO);
    }




    @Test(groups = "loginTrue",description = "用户成功登陆接口")
    public void loginTrue() throws IOException {
        Log.info("开始执行用例...");

        SqlSession session = DatabaseUtil.getSqlSessionCase();
        //从数据库读取用例
        LoginCase loginCase = session.selectOne("loginCase",1);


        //下边的代码为写完接口的测试代码
        String result=HttpRequestUtil.getResultForPost2(TestConfig.loginUrl,loginCase);
        //String result = getResult(loginCase);
        //将返回的响应结果字符串转化成json对象
        //JSONObject resultJson = new JSONObject(result);
        JSONObject resultJson = JSON.parseObject(result);
      //JSONObject jsonx = JSON.parseObject(resultJson.getString("info"));
        Log.info("执行JSON解析，解析的内容是 " + resultJson);
        //将获取到的token存储起来
        String token = (String) resultJson.get("access_token");
        TestConfig.Token=token;
        Log.info("token信息为" + token);
        Log.info("接口内容响应断言");
        //处理结果，就是判断返回结果是否符合预期
       // Assert.assertEquals(loginCase.getExpected(),result);
        Log.info("用例执行结束...");

    }

    @Test(description = "用户登陆失败接口")
    public void loginFalse() throws IOException {
        //涉及到数据库时用到以下两条代码
        SqlSession session = DatabaseUtil.getSqlSessionCase();
        //从数据库读取用例
        LoginCase loginCase = session.selectOne("loginCase",2);

        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);
        //下边的代码为写完接口的测试代码
        String result = getResult(loginCase);
        JSONObject resultJson = JSON.parseObject(result);
        //获取到返回结果中的错误编号
        Object Code =  resultJson.get("errorCode");
        //将Object类型转换为int类型
        int errorCode = Integer.parseInt(String.valueOf(Code));
        String errorMsg = (String) resultJson.get("errorMsg");
        //处理结果，就是判断返回结果是否符合预期
        Assert.assertEquals(880303014,errorCode);
        Assert.assertEquals("用户名或密码错误",errorMsg);
        //Assert.assertEquals(loginCase.getExpected(),result);
    }
    private String getResult(LoginCase loginCase) throws IOException {
        //下边的代码为写完接口的测试代码
        HttpPost post = new HttpPost(TestConfig.loginUrl);
        JSONObject param = new JSONObject();
        param.put("username",loginCase.getUserName());
        param.put("password",loginCase.getPassword());
        param.put("client_secret",loginCase.getClient_secret());
        param.put("client_id",loginCase.getClient_id());
        param.put("companyId",loginCase.getCompanyId());
        //设置请求头信息 设置header
        post.setHeader("content-type","application/json");
        //将参数信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        //声明一个对象来进行响应结果的存储
        String result;
        HttpResponse response = null;
        try {
            //执行post方法
            response = TestConfig.defaultHttpClient.execute(post);
        }catch (IOException e){
            e.printStackTrace();
        }

        //获取响应结果
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println("结果为--------------"+result);
        //将cookies信息存储起来
        //TestConfig.store = TestConfig.defaultHttpClient.getCookieStore();
        return result;
    }


}
