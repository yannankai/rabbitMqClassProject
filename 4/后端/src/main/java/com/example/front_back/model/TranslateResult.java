package com.example.front_back.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranslateResult {
    private String from;
    private String to;
    private List<Src_Dst> trans_result;
}
