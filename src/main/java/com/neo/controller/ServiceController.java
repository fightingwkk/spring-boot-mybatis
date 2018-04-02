package com.neo.controller;

import com.neo.entity.PurchasedServiceEntity;
import com.neo.entity.ServiceEntity;
import com.neo.mapper.ServiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/service")
public class ServiceController {
    @Autowired
    private ServiceMapper serviceMapper;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //通过服务id列表返回服务列表
    @RequestMapping(value="/findbylist")
    public List<ServiceEntity> findServiceList(@RequestBody List<Integer> idlist){
        try{
            List<ServiceEntity> servicelist = new ArrayList<>();
            for (Integer id :idlist) {
                servicelist.add(serviceMapper.findById(id));
            }
            logger.info("成功通过服务id列表返回服务列表");
            return servicelist;
        }catch (Exception e){
            logger.error("通过服务id列表返回服务列表失败：",e.getMessage());
            return null;
        }
    }
    //通过服务id返回服务
    @RequestMapping(value="/findbyid")
    public ServiceEntity findService(@RequestBody Integer id){
        try{
            ServiceEntity serviceEntity = serviceMapper.findById(id);
            logger.info("成功通过服务id列表返回服务列表");
            return serviceEntity;
        }catch (Exception e){
            logger.error("通过服务id返回服务失败：",e.getMessage());
            return null;
        }
    }
    //通过已购买的服务id返回服务
    @RequestMapping(value="/findboughtbyid")
    public PurchasedServiceEntity findBoughtService(@RequestBody Integer id){
        try{
            PurchasedServiceEntity purchasedServiceEntity = serviceMapper.findBoughtById(id);
            logger.info("成功通过已购买的服务id列表返回服务列表");
            return purchasedServiceEntity;
        }catch (Exception e){
            logger.error("通过已购买的服务id返回服务失败：",e.getMessage());
            return null;
        }
    }
}
