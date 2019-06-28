package com.tester.cases;

import com.tester.config.TestConfig;
import com.tester.log.LogControler;
import com.tester.model.Brand;
import com.tester.model.UpdateBrandInfoCase;
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

public class UpdateBrandInfoTest {
    final static LogControler Log = LogControler.getLogger(UpdateBrandInfoTest.class);

    @Test(dependsOnGroups = "loginTrue",description = "根据品牌id更改品牌信息")
    public void updateBrandInfo() throws IOException, InterruptedException {
        SqlSession session = DatabaseUtil.getSqlSession();
        UpdateBrandInfoCase updateBrandInfoCase = new UpdateBrandInfoCase();
        updateBrandInfoCase.setId(38915L);
        updateBrandInfoCase.setBrandCode("hzhzhuoshini111");
        updateBrandInfoCase.setBrandName("卓诗尼hzh111");

        //获取请求后的结果
        int result = getResult(updateBrandInfoCase);

        //获取更新后的结果（从数据库查找）
        Thread.sleep(2000);
        Brand brand = session.selectOne("getUpdateBrandInfo",updateBrandInfoCase);
        System.out.println(brand.toString());
        //判断数据库是否有修改后的这个品牌信息
        Assert.assertNotNull(brand);
        //判断是否有修改成功，如果成功就返回1
        Assert.assertNotNull(result);
    }


    private int getResult(UpdateBrandInfoCase updateBrandInfoCase) throws IOException {
        Log.info("修改品牌用例开始执行=========");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type","application/json");
        headers.put("Authorization","Bearer "+TestConfig.Token);
        String result;
        Long brandId = updateBrandInfoCase.getId();
        HttpResponse response =HttpRequestUtil.getResponsePut(TestConfig.updateBrandInfoUrl,updateBrandInfoCase,brandId,headers);
        //获取响应结果
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        return Integer.parseInt(result);
    }

}
