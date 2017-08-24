package com.neo.controller;

import com.neo.entity.ServiceEntity;
import com.neo.mapper.ServiceMapper;
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


    //通过服务id列表返回服务列表
    @RequestMapping(value="/findbylist")
    public List<ServiceEntity> findService(@RequestBody List<Integer> idlist){
        List<ServiceEntity> servicelist = new ArrayList<>();
        for (Integer id :idlist) {
            servicelist.add(serviceMapper.findById(id));
        }
        return servicelist;
    }

}
