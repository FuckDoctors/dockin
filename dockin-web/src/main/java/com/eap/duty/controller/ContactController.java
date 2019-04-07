package com.eap.duty.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 通讯录
 *
 * @author billjiang 475572229@qq.com
 * @create 18-7-12
 */
@Controller
@RequestMapping(value = "/contact")
public class ContactController {

    @RequestMapping(value = "/home")
    private String contactPage() {
        return "contact/contact_home";
    }

    @RequestMapping(value = "/member")
    private String contactAdd() {
        return "contact/contact_member";
    }

    @RequestMapping(value = "/group")
    private String groupAdd() {
        return "contact/contact_group";
    }

    @RequestMapping(value = "/join")
    private String groupJoin() {
        return "contact/contact_join";
    }
}
