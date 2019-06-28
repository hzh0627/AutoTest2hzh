package com.tester.cases;

import com.tester.config.TestConfig;
import com.tester.model.Brand;
import com.tester.model.GetBrandListCase;
import com.tester.utils.DatabaseUtil;
import com.tester.utils.HttpRequestUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetBrandInfoListTest {



    @Test(dependsOnGroups="loginTrue",description = "获取品牌名称为卓诗尼的品牌信息")
    public void getBrandListInfo() throws IOException, InterruptedException {

        SqlSession session = DatabaseUtil.getSqlSession();
        GetBrandListCase getBrandListCase = new GetBrandListCase();
        List<Long> lists = new ArrayList<Long>();
        lists.add(38915L);
        lists.add(38916L);
        getBrandListCase.setIds(lists);

        //下边为写完接口的代码
        JSONArray resultJson = getJsonResult(getBrandListCase);
        /**
         *
         */
        Thread.sleep(2000);
        List<Brand> brandList = session.selectList("getBrandList",getBrandListCase);
        for(Brand b : brandList){
            System.out.println("list获取的brand:"+b.toString());
        }
        JSONArray brandListJson = new JSONArray(brandList);

        Assert.assertEquals(brandListJson.length(),resultJson.length());
        for(int i = 0;i<resultJson.length();i++){
            JSONObject expect = (JSONObject) resultJson.get(i);
            JSONObject actual = (JSONObject) brandListJson.get(i);
            Assert.assertEquals(expect.toString(), actual.toString());
        }

    }

    private JSONArray getJsonResult(GetBrandListCase getBrandListCase) throws IOException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type","application/json");
        headers.put("Authorization","Bearer "+TestConfig.Token);
        String result;
        List<Long> list =getBrandListCase.getIds();
        String params = StringUtils.join(list, ",");
        HttpResponse response =HttpRequestUtil.get(TestConfig.getBrandListUrl,params,headers);
        //获取响应结果
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        JSONArray jsonArray = new JSONArray(result);
        System.out.println("调用接口list result:"+result);

        return jsonArray;

    }

}
