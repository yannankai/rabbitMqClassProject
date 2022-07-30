package com.example.front_back.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatWith {
    private String from;
    private String to;

    public void sort(){
        String s1 = this.from;
        String s2 = this.to;
        if (s1.compareToIgnoreCase(s2)<0){
            this.from = s2;
            this.to  = s1;
        } //from 字母大序
    }
}
