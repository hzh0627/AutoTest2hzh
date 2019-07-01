package com.tester.cases;

import com.tester.config.TestConfig;
import com.tester.log.LogControler;
import com.tester.model.Brand;
import com.tester.model.DeleteBrandInfoCase;
import com.tester.utils.DatabaseUtil;
import com.tester.utils.HttpRequestUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteBrandInfoTest {
    final static LogControler Log = LogControler.getLogger(DeleteBrandInfoTest.class);

    @Test(dependsOnGroups = "loginTrue",description = "根据ID删除品牌")
    public void deleteBrand() throws IOException, InterruptedException {
        SqlSession session = DatabaseUtil.getSqlSession();
        DeleteBrandInfoCase deleteBrandInfoCase = new DeleteBrandInfoCase();
        List<Long> lists = new ArrayList<Long>();
        //lists.add(38915L);
        lists.add(38916L);
        // lists.add(38919L);
        deleteBrandInfoCase.setIds(lists);

        //请求删除接口返回的结果
        int result = getResult(deleteBrandInfoCase);
        //数据库查询出来的结果
        Thread.sleep(2000);
        Brand brand = session.selectOne("deleteBrandInfo",deleteBrandInfoCase);
        // System.out.println(brand.toString());

        //
        Assert.assertNull(brand);
        Assert.assertNotNull(result);
    }


    private int getResult(DeleteBrandInfoCase deleteBrandInfoCase) throws IOException {
        Log.info("删除品牌用例开始执行=========");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type","application/json");
        headers.put("Authorization","Bearer "+TestConfig.Token);
        String result;
        List<Long> list =deleteBrandInfoCase.getIds();
        String params = StringUtils.join(list, ",");
        HttpResponse response =HttpRequestUtil.getResponseDelete(TestConfig.deleteBrandInfoUrl,params,headers);
        //获取响应结果
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        return Integer.parseInt(result);
    }

}
