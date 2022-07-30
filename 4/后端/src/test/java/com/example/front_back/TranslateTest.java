package com.example.front_back;

import com.example.front_back.model.TranslateResult;
import com.example.front_back.translate.TransApi;
import com.example.front_back.utils.DecodeUnicode;
import com.example.front_back.utils.Json2Obj;
import com.example.front_back.utils.TranslateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

public class TranslateTest {
    private static final String APP_ID = "20220401001154441";
    private static final String SECURITY_KEY = "2SanZg9ahapNoc9IPg0i";


    @Test
    public void demoTest() throws JsonProcessingException {
        String query = "I like funny apple";
        System.out.println(TranslateUtil.translateEnToZh(query));
    }
}
