package com.example.front_back.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {
//    private static CloseableHttpClient client;
//    public HttpClient(){
//        client = HttpClientBuilder.create().build();
//    }
//
//    public CloseableHttpResponse httpGet(String url){
//        HttpGet httpGet = new HttpGet(url);
//        CloseableHttpResponse response = null;
//        try {
//            response = client.execute(httpGet);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return response;
//    }

 //要使用basic的http认证
    private final static String USERNAME = "guest";
    private final static String PASSWORD = "guest";

    /**
     * 获得CloseableHttpClient对象，通过basic认证。
     * @param
     * @param
     * @return
     */
    public static CloseableHttpClient getBasicHttpClient() {
     // 创建HttpClientBuilder
     HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
     // 设置BasicAuth
     CredentialsProvider provider = new BasicCredentialsProvider();
     AuthScope scope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
     UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(USERNAME,PASSWORD);
     provider.setCredentials(scope, credentials);
     httpClientBuilder.setDefaultCredentialsProvider(provider);
     return httpClientBuilder.build();
 }
    public static void closeAll(CloseableHttpResponse response,CloseableHttpClient pClient){
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            pClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据API获得相关的MQ信息
     * @param url
     * @return
     */
    public static List<String> getMQJSONArray(String url) {
        HttpGet httpPost = new HttpGet(url);
        CloseableHttpClient pClient=getBasicHttpClient();
        CloseableHttpResponse response = null;
        List<String> res = null;
        try {
            response = pClient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                String string = EntityUtils.toString(response.getEntity());
                // 返回来是一个集合，去掉开始的“[”和结尾的“]”;
//                String substring = string.substring(1, string.length() - 1);
                JSONArray obj = JSONArray.parseArray(string);
                List<String> list = JSONObject.parseArray(obj.toJSONString(),String.class);
                res = list;
            } else {
                System.out.println("请求返回:" + state + "(" + url + ")");
            }
        }catch (Exception e){
            e.printStackTrace();
          //  logger.error("地址url:"+url+"，异常："+e.toString());
        } finally {
            closeAll(response,pClient);
        }
        return res;
    }


}
