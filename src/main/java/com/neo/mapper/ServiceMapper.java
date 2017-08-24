package com.neo.mapper;

import com.neo.entity.ServiceEntity;
import org.apache.ibatis.annotations.Select;

public interface ServiceMapper {
    //通过服务id列表返回服务
    @Select("select  * from service where id=#{id}")
     ServiceEntity findById(Integer id);
}
