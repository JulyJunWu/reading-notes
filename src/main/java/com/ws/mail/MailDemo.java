package com.ws.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @Description:
 * @Date: 2019/9/29 0029 18:07
 * spring内置邮件功能 demo
 */
public class MailDemo {

    @org.junit.Test
    public void test() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setDefaultEncoding("utf-8");
        mailSender.setHost("smtp.qq.com");
        //邮箱 xxxxxx@qq.com
        String from = "xxxxxx@qq.com";
        mailSender.setUsername(from);
        //QQ邮箱需要开通授权码 http://service.mail.qq.com/cgi-bin/help?subtype=1&&id=28&&no=1001256
        mailSender.setPassword("授权码");

        //构建发送的信息
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo("666668888@qq.com");
        //邮件内容
        message.setText("测试发送邮件");
        //邮件标题
        message.setSubject("Hello world");
        //发送
        mailSender.send(message);
    }
}
