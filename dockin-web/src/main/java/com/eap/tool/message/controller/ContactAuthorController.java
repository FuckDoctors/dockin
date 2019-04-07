package com.eap.tool.message.controller;

import com.eap.framework.base.pojo.Result;
import com.eap.framework.message.SimpleMailSender;
import com.eap.framework.message.entity.SimpleMail;
import com.eap.framework.utils.PropertiesUtil;
import com.eap.tool.message.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by billJiang on 2016/12/27.
 * e-mail:jrn1012@petrochina.com.cn qq:475572229
 * 联系作者 发送邮件
 */
@Controller
@RequestMapping(value = "/message")
public class ContactAuthorController {

    @Resource
    private MessageService messageService;

    @RequestMapping(value = "/mail/edit")
    private String editMail() {

        return "tool/message/mail_edit";
    }


    @RequestMapping(value = "/mail/save")
    @ResponseBody
    private Result saveMail(SimpleMail mail) {
        messageService.save(mail);
        SimpleMailSender mailSender = new SimpleMailSender();
        try {
            mailSender.send(PropertiesUtil.getValue("mail.to"), mail.getMailType()+"->"+mail.getSubject(), mail.getContent()+"<br/>来自："+mail.getFromUser());
            return new Result(true);
        } catch (Exception ex) {
            return new Result(false, "保存失败，失败原因：" + ex.getMessage().toString());
        }
    }
}
