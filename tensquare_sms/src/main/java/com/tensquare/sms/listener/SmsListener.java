package com.tensquare.sms.listener;

import com.aliyuncs.exceptions.ClientException;
import com.tensquare.sms.util.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 短信监听类
 */
@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String templateCode;//模板编号

    @Value("${aliyun.sms.sign_name}")
    private String signName;//签名

    /**
     *  发送短信
     * @param message
     */
    @RabbitHandler
    public void sendSms(Map<String,String> message){
        try {
            smsUtil.sendSms(message.get("mobile"),templateCode,signName,"{\"code\":"+ message.get("code") +"}");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

}