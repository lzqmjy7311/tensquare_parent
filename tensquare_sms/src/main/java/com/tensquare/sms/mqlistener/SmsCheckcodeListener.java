package com.tensquare.sms.mqlistener;

import com.aliyuncs.exceptions.ClientException;
import com.tensquare.sms.util.SmsUtils;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 手机短信验证码的mq监听
 */
@Component
public class SmsCheckcodeListener {

    //方法1：直接注入自定义的常量
    //签名
    @Value("${aliyun.sms.sign_name}")
    private String signName;
    //模版编号
    @Value("${aliyun.sms.template_code}")
    private String templateCode;

    //方法2：注入环境对象，可用户获取环境常量
    @Autowired
    private Environment environment;

    /**
     * 处理短信验证码
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "tensquare.sms.queue", durable = "true"),
            exchange = @Exchange(value = "tensquare.sms.exchange", ignoreDeclarationExceptions = "true"),
            key = {"sms.verify.code"}))
    public void handleSmsCheckcode(Map<String, String> message) {

        System.out.println("手机号：" + message.get("mobile"));
        System.out.println("验证码：" + message.get("checkcode"));

        final String mobile = message.get("mobile");
        final String checkcode = message.get("checkcode");

        String templateParam = "{\"code\":\"" + checkcode + "\"}";
        String outId = "123456";

        //通过环境对象获取自定义常量
        String accessKeyId = environment.getProperty("aliyun.sms.accessKeyId");
        String accessKeySecret = environment.getProperty("aliyun.sms.accessKeySecret");

        if (message == null || message.size() == 0) {
            // 放弃处理
            return;
        }
        // 执行'发送短信'
        try {
            SmsUtils.sendSms(mobile, signName, templateCode, templateParam, outId, accessKeyId, accessKeySecret);
        } catch (ClientException e) {
            System.out.println("发送失败");
            e.printStackTrace();
        }
    }
}
