package com.example.front_back.controller;

import com.example.front_back.mapper.ExchangeInfoMapper;
import com.example.front_back.model.SysExchangeInfo;
import com.example.front_back.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.util.List;

@RestController
@RequestMapping("/sys")
@EnableScheduling
public class SystemInfo {

    @Autowired
    private ExchangeInfoMapper exchangeInfoMapper;

    @GetMapping("/exchange")
    @Scheduled(cron = "0 0/3 * * * ?")// 每小时的第0分0秒开始，每三分钟触发一次
    public List<SysExchangeInfo> getExchangeInfo(){
        String url="http://127.0.0.1:15672/api/exchanges";
        List<String> list= HttpClientUtil.getMQJSONArray(url);
        exchangeInfoMapper.insertInfo(list.size(),Long.toString(System.currentTimeMillis()));
        List<SysExchangeInfo> ret = exchangeInfoMapper.getExchangeInfo();
        System.out.println("系统写一次exchange备份");
        return ret;
    }

}
