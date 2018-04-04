package com.neo.mapper;

import com.neo.entity.DoctorServiceEntity;
import com.neo.entity.PurchasedServiceEntity;
import com.neo.entity.ServiceEntity;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ServiceMapper {
    //通过服务id列表返回服务
    @Select("select  * from service where id=#{id}")
     ServiceEntity findById(Integer id);
    //通过已购买的服务id列表返回服务
    @Select("select  * from purchased_service where id=#{id}")
    PurchasedServiceEntity findBoughtById(Integer id);
    // 返回医生所有的服务包
    @Select("select a.id,a.doctor_phone,a.service_id,b.name,b.price,b.count,b.duration,b.content,b.kind, a.added_time,a.added_status,a.delete_status,b.status from doctor_service a left join service b on a.service_id=b.id where a.doctor_phone = #{phone} and a.delete_status=0 and b.delete_status=0 and a.added_status=1 and b.status=1 order by time asc")
    List<DoctorServiceEntity> findDoctorService(String phone);
    // 返回所有的服务包
    @Select("select * from service where delete_status=0 and status = 1 order by time asc")
    List<ServiceEntity> findAllService();
}
