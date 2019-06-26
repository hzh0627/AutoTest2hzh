package com.tester.utils;

import com.alibaba.fastjson.JSONObject;
import com.tester.config.TestConfig;
import com.tester.log.LogControler;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestUtil {
    final static LogControler Log = LogControler.getLogger(HttpRequestUtil.class);
    /**
     * post请求的获取结果方法
     * @param url 接口对应的路径
     * @param param 传JSONObject参数（JSONObject param = new JSONObject();）
     * @return
     * @throws IOException
     */
    public static String getResultForPost(String url,JSONObject param) throws IOException {
        //下边的代码为写完接口的测试代码
        HttpPost post = new HttpPost(url);
        //设置请求头信息 设置header
        post.setHeader("content-type","application/json");
        //将参数信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        //声明一个对象来进行响应结果的存储
        String result;
        HttpResponse response = null;
        //执行post方法
        response = TestConfig.defaultHttpClient.execute(post);
        //获取响应结果
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        //将cookies信息存储起来
        //TestConfig.store = TestConfig.defaultHttpClient.getCookieStore();
        Log.info("返回的结果是 " + result);
        return result;
    }

    /**
     * 封装PSOT请求方法
     * @param url 接口对应的路径
     * @param obj 传一个对象
     * @return 返回响应结果
     * @throws IOException
     */
    public static <T> String getResultForPost2(String url,T obj) throws IOException {
        //下边的代码为写完接口的测试代码
        HttpPost post = new HttpPost(url);
        Map<String, Object> fieldMap = getModelFeignIds(obj);
        JSONObject param = new JSONObject();
        for (String fieldName : fieldMap.keySet()) {
            if (fieldName.equals("companyId")){
                param.put(fieldName, fieldMap.get(fieldName));
            }else{
                //fieldName.toLowerCase()将fieldName转化成小写
                param.put(fieldName.toLowerCase(), fieldMap.get(fieldName));
            }
        }
        //设置请求头信息 设置header
        post.setHeader("content-type","application/json");
        //将参数信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        //声明一个对象来进行响应结果的存储
        String result;
        HttpResponse response = null;
        //执行post方法
        response = TestConfig.defaultHttpClient.execute(post);
        //获取响应结果
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        Log.info("返回的结果是 " + result);
        return result;
    }

    /**
     * 返回一个Bean对象的所有属性值
     * @param obj 对象
     * @return
     */

    public static Map<String, Object> getModelFeignIds (Object obj){
        Map<String, Object> objectMap = new HashMap<String, Object>();
        for (Field field : obj.getClass().getDeclaredFields()){
            if(field.getName() != null){
                Object attr = getAttributeValues(obj, field.getName());
                if(attr != null) {
                    objectMap.put(field.getName(), attr);
                }
            }
        }
        return objectMap;
    }
    public static Object getAttributeValues(Object obj, String fieldName) {
        if (obj == null || fieldName == null || fieldName.length() == 0) {
            return null;
        }
        try {
            final String first = String.valueOf(fieldName.charAt(0));
            final String upFirst = first.toUpperCase();
            final String get = "get" + fieldName.replaceFirst(first, upFirst);
            final Method method = obj.getClass().getMethod(get);
            return method.invoke(obj);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 获取响应状态码，常用来和TestBase中定义的状态码常量去测试断言使用
     * @param response
     * @return 返回int类型状态码
     */
    public static int getStatusCode (HttpResponse response) {
        int statusCode = response.getStatusLine().getStatusCode();
        Log.info("解析，得到响应状态码:"+ statusCode);
        return statusCode;
    }

    /**
     *  封装带请求头的POST请求
     * @param url 接口对应的路径
     * @param obj 实体对象
     * @param headermap 带请求头
     * @param <T>
     * @return 返回响应对象
     * @throws IOException
     */
    public static <T> HttpResponse getResponse(String url,T obj,Map<String,String> headermap) throws IOException {
        HttpPost post = new HttpPost(url);
        Map<String, Object> fieldMap = getModelFeignIds(obj);
        JSONObject param = new JSONObject();
        for (String fieldName : fieldMap.keySet()) {
            param.put(fieldName, fieldMap.get(fieldName));
        }
        //设置请求头信息 设置header,加载请求头到httppost对象
        for(Map.Entry<String, String> entry : headermap.entrySet()) {
            post.setHeader(entry.getKey(), entry.getValue());
        }
        //将参数信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        HttpResponse response = null;
        //执行post方法
        response = TestConfig.defaultHttpClient.execute(post);
        Log.info("返回的响应对象是 " + response);
        return response;
    }

    /**
     * Put方法
     * @param url
     * @param obj
     * @param brandid
     * @param headermap
     * @param <T>
     * @return
     * @throws IOException
     */
    public static  <T> HttpResponse getResponsePut(String url,T obj,Long brandid,Map<String,String> headermap) throws IOException {
        //创建一个HttpPut的请求对象
        HttpPut httpput = new HttpPut(url + brandid +"?" +"usrId=394&companyId=239");
        Map<String, Object> fieldMap = getModelFeignIds(obj);
        JSONObject params = new JSONObject();
        for (String fieldName : fieldMap.keySet()) {
            params.put(fieldName, fieldMap.get(fieldName));
        }
        //加载请求头到httpget对象
        for (Map.Entry<String, String> entry : headermap.entrySet()) {
            httpput.addHeader(entry.getKey(), entry.getValue());
        }
        //将参数信息添加到方法中
        StringEntity entity = new StringEntity(params.toString(),"utf-8");
        httpput.setEntity(entity);
        //执行请求,相当于postman上点击发送按钮，然后赋值给HttpResponse对象接收
        HttpResponse httpResponse = TestConfig.defaultHttpClient.execute(httpput);
        return httpResponse;
    }

    //Get 请求方法（带参数和请求头信息）
    public static  HttpResponse get(String url,String params, Map<String, String> headermap) throws IOException {
        //创建一个HttpGet的请求对象
        HttpGet httpget = new HttpGet(url + "&" + "ids="+params);
        //加载请求头到httpget对象
        for (Map.Entry<String, String> entry : headermap.entrySet()) {
            httpget.addHeader(entry.getKey(), entry.getValue());
        }
//        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        //执行请求,相当于postman上点击发送按钮，然后赋值给HttpResponse对象接收
        HttpResponse httpResponse = TestConfig.defaultHttpClient.execute(httpget);
        return httpResponse;
    }

    /**
     * 删除方法
     * @param url
     * @param params
     * @param headermap
     * @return
     * @throws IOException
     */
    public static  HttpResponse getResponseDelete(String url, String params, Map<String, String> headermap) throws IOException {
        //创建一个HttpGet的请求对象
        HttpDelete httpDelete = new HttpDelete(url + "&" + "ids="+params);
        //加载请求头到httpget对象
        for (Map.Entry<String, String> entry : headermap.entrySet()) {
            httpDelete.addHeader(entry.getKey(), entry.getValue());
        }
        //执行请求,相当于postman上点击发送按钮，然后赋值给HttpResponse对象接收
        HttpResponse httpResponse = TestConfig.defaultHttpClient.execute(httpDelete);
        return httpResponse;
    }

}
