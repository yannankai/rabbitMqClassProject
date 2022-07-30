package com.example.front_back.utils;

import com.example.front_back.model.TranslateResult;
import com.example.front_back.translate.TransApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TranslateUtil {
    //注册获得的APPID和密钥
    private static final String APP_ID = "20220401001154441";
    private static final String SECURITY_KEY = "2SanZg9ahapNoc9IPg0i";
    //将英文翻译为中文
    public static String translateEnToZh(String en) throws JsonProcessingException {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        System.out.println("要翻译的文本时:"+en);
        String resultJson=api.getTransResult(en, "en", "zh");
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("翻译res:"+resultJson);
        TranslateResult obj = mapper.readValue(resultJson, TranslateResult.class);
        return obj.getTrans_result().get(0).getDst();
    }
}
