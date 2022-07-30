package com.example.front_back.mapper;

import com.example.front_back.model.SysExchangeInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExchangeInfoMapper {

    @Insert("insert into exchange_info values(#{ExchangeNum},#{timeStamp});")
    int insertInfo(int ExchangeNum,String timeStamp);

    @Select("select * from exchange_info")
    List<SysExchangeInfo> getExchangeInfo();

}
