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

    @Test(dependsOnGroups = "loginTrue",description = "����Ʒ��id����Ʒ����Ϣ")
    public void updateBrandInfo() throws IOException, InterruptedException {
        SqlSession session = DatabaseUtil.getSqlSession();
        UpdateBrandInfoCase updateBrandInfoCase = new UpdateBrandInfoCase();
        updateBrandInfoCase.setId(38915L);
        updateBrandInfoCase.setBrandCode("hzhzhuoshini111");
        updateBrandInfoCase.setBrandName("׿ʫ��hzh111");

        //��ȡ�����Ľ��
        int result = getResult(updateBrandInfoCase);

        //��ȡ���º�Ľ���������ݿ���ң�
        Thread.sleep(2000);
        Brand brand = session.selectOne("getUpdateBrandInfo",updateBrandInfoCase);
        System.out.println(brand.toString());
        //�ж����ݿ��Ƿ����޸ĺ�����Ʒ����Ϣ
        Assert.assertNotNull(brand);
        //�ж��Ƿ����޸ĳɹ�������ɹ��ͷ���1
        Assert.assertNotNull(result);
    }


    private int getResult(UpdateBrandInfoCase updateBrandInfoCase) throws IOException {
        Log.info("�޸�Ʒ��������ʼִ��=========");
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type","application/json");
        headers.put("Authorization","Bearer "+TestConfig.Token);
        String result;
        Long brandId = updateBrandInfoCase.getId();
        HttpResponse response =HttpRequestUtil.getResponsePut(TestConfig.updateBrandInfoUrl,updateBrandInfoCase,brandId,headers);
        //��ȡ��Ӧ���
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        return Integer.parseInt(result);
    }

}
