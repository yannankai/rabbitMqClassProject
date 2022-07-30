package com.example.front_back.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration //全局配置类
public class WebConfig  implements WebMvcConfigurer {
    //重写一个方法,专用于跨域处理
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")  //允许所有端口号来通讯，如果只要8080则写为  http://localhost:8080
                .allowCredentials(true)//允许携带信息
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH","OPTIONS") //允许的请求方法
                .maxAge(3600); //最大应时间
    }
}
