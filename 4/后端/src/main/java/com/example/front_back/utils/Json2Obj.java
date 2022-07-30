package com.example.front_back.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.bcel.classfile.JavaClass;


public class Json2Obj {
    public static Object json2OBJ(String str,Class javaClass){
        str = str.replaceAll("\\\\","");
        str = str.substring(1,str.length()-1);
        System.out.println(str);
        return JSONObject.parseObject(str).toJavaObject(javaClass);
    }

    public static String obj2Json(Object o){
        return JSON.toJSONString(JSONObject.toJSON(o));
    }

}
