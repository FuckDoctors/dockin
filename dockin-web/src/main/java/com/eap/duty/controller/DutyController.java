package com.eap.duty.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author billjiang 475572229@qq.com
 * @create 18-7-9
 * @description 值班安排
 */
@Controller
@RequestMapping(value = "/duty")
public class DutyController {

    @RequestMapping(value = "/home")
    private String homePage(){
        return "duty/duty_home";
    }

    @RequestMapping(value="/set")
    private String settingPage(){
        return "duty/duty_set";
    }

    @RequestMapping(value="/add")
    private String dutyAdd(){
        return "duty/duty_add";
    }

    @RequestMapping(value="/time")
    private String timeAdd(){
        return "duty/duty_time";
    }

}
