package com.example.front_back;

import com.alibaba.fastjson.JSONArray;
import com.example.front_back.controller.SystemInfo;
import com.example.front_back.model.SysExchangeInfo;
import com.example.front_back.translate.HttpGet;
import com.example.front_back.utils.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SystemTest {

    @Autowired
    SystemInfo systemInfo;

    @Test
    void getExcahnge(){


        List<SysExchangeInfo> l  = systemInfo.getExchangeInfo();
        System.out.println(l);
        //JSONArray js = new JSONArray();

    }



}
