package com.neo.controller;

import com.neo.entity.PurchasedServiceEntity;
import com.neo.mapper.PatientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
public class ScheduleController {
    @Autowired
    PatientMapper patientMapper;
    /*
  *生成消息
   */
    @Scheduled(cron="0 0 0 * * ?")
    public void generateMessage() {
        try {
            List<String> patientList = patientMapper.selectAllPatients();
            List<Integer> messageList = patientMapper.selectAllMessage();
            for (String wechat_id : patientList) {
                if (patientMapper.selectPurchasedServiceByWechatId(wechat_id) != null) {
                    String kind = patientMapper.selectById(wechat_id).getKind();
                    if (kind != null && (kind.equals("高危") || kind.equals("极高危"))) {
                        for (Integer id : messageList) {
                            patientMapper.insertMessage(wechat_id, id);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("定时生成消息发生错误:"+e.getMessage());
        }
    }

    //更新过期服务包
    @Scheduled(cron="0 0 0-23 * * ?")
    public void updatePurchasedServiceExpired() {
        try {
            List<PurchasedServiceEntity> purchasedServiceEntityList = patientMapper.selectAllPurchasedService();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Iterator<PurchasedServiceEntity> it = purchasedServiceEntityList.iterator(); it.hasNext(); ) {
                PurchasedServiceEntity purchasedServiceEntity = it.next();
                Date purchasedDay = sdf.parse(purchasedServiceEntity.getPurchased_time());
                Date expiredDay = new Date(purchasedDay.getTime()+Integer.parseInt(purchasedServiceEntity.getDuration())*24*60*60*1000L);
                Date today = new Date();
                if(today.after(expiredDay)){
                    patientMapper.updatePurchasedServiceExpired(purchasedServiceEntity.getId());
                }
            }
        } catch (Exception e) {
            System.out.println("更新过期服务包发生错误:"+e.getMessage());
        }
    }
}
