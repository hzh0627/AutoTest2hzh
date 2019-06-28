package com.tester.cases;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tester.config.TestConfig;
import com.tester.log.LogControler;
import com.tester.model.AddBrandCase;
import com.tester.model.Brand;
import com.tester.utils.DatabaseUtil;
import com.tester.utils.HttpRequestUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddBrandTest {
    final static LogControler Log = LogControler.getLogger(AddBrandTest.class);
    SqlSession session = null;
    @Test(dependsOnGroups = "loginTrue",description = "成功添加品牌接口")
    public void addBrand() throws IOException, InterruptedException {
        session = DatabaseUtil.getSqlSession();
     //  AddUserCase addUserCase = session.selectOne("addUserCase",1);
        AddBrandCase addBrandCase =new AddBrandCase();
        addBrandCase.setBrandCode("zuoshini21111");
        addBrandCase.setBrandName("卓诗尼1111");
        //下边的代码为写完接口的测试代码
        String result = getResult(addBrandCase);
         //查询数据库看品牌是否添加成功
        Thread.sleep(2000);
        Brand brand = session.selectOne("addBrand",addBrandCase);
        System.out.println(brand.toString());
        //处理结果，就是判断返回结果是否符合预期
        Assert.assertEquals(brand.getId(),Integer.parseInt(result));
        Log.info("品牌添加成功，数据已与数据库匹配成功！！！！");
    }

    @Test(dependsOnGroups = "loginTrue",description = "添加重复编号的品牌接口")
    public void addExistBrandCode() throws IOException, InterruptedException {
        session = DatabaseUtil.getSqlSession();
        AddBrandCase addBrandCase =new AddBrandCase();
        addBrandCase.setBrandCode("zuoshinihzh0000");
        addBrandCase.setBrandName("卓诗尼000");
        //下边的代码为写完接口的测试代码
        String result = getResult(addBrandCase);
        JSONObject resultJson = JSON.parseObject(result);
        //获取到返回结果中的错误编号
        Object Code =  resultJson.get("errorCode");
        //将Object类型转换为int类型
        int errorCode = Integer.parseInt(String.valueOf(Code));
        String errorMsg = (String) resultJson.get("errorMsg");
        //处理结果，就是判断返回结果是否符合预期
        Assert.assertEquals(1000001,errorCode);
        Assert.assertEquals("品牌编码已存在",errorMsg);
        Log.info("品牌编码已存在!!!!");
    }

    @Test(dependsOnGroups = "loginTrue",description = "添加品牌接口")
    public String getResult(AddBrandCase addBrandCase) throws IOException, InterruptedException {
        Log.info("添加品牌用例开始执行=========");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        headers.put("Authorization", "Bearer " + TestConfig.Token);
        String result;
        HttpResponse response = HttpRequestUtil.getResponse(TestConfig.addBrandUrl, addBrandCase, headers);
        //获取响应结果
        result = EntityUtils.toString(response.getEntity(), "utf-8");
        //int statusCode = HttpRequestUtil.getStatusCode(response);
//        Assert.assertEquals(statusCode,ErrorCodeConfig.RESPNSE_STATUS_CODE_200,"statusCode is not 200");
//        Assert.assertNotNull(Integer.parseInt(result));
        Log.info("添加品牌用例执行结束=========");
        return result;
    }





}
