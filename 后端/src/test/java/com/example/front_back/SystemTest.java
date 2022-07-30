package com.example.front_back;

import com.example.front_back.controller.SystemInfo;
import com.example.front_back.model.SysExchangeInfo;
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
