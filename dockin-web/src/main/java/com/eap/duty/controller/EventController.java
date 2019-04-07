package com.eap.duty.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author billjiang 475572229@qq.com
 * @create 18-7-8
 */
@Controller
@RequestMapping("/event")
public class EventController {
    // 无线巡防
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    private String list() {
        return "event/event_list";
    }

    //任务派发
    @RequestMapping(value = "/task/assign", method = RequestMethod.GET)
    private String taskAssign(String userIds, Model model) {
        List<Map<String, String>> users = new ArrayList<>();
        if (StringUtils.isNotEmpty(userIds)) {
            String[] arr = userIds.split(",");
            int i = 0;
            for (String s : arr) {
                Map<String, String> userMap = new HashMap<>();
                userMap.put("avatar", "/images/user" + (++i) + ".png");
                userMap.put("userName", s);
                userMap.put("userId", s);
                users.add(userMap);
            }
        }
        model.addAttribute("users", users);
        return "event/event_task_assign";
    }

    // 事件新增
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    private String add() {
        return "event/event_add";
    }

    //进展反馈
    @RequestMapping(value = "/feedback/{id}", method = RequestMethod.GET)
    private String feedBack(@PathVariable("id") String id, Model model) {
        //TODO 根据id查询到事件
        return "event/event_feedback";
    }

    //案件反馈
    @RequestMapping(value = "/report/{id}", method = RequestMethod.GET)
    private String report(@PathVariable("id") String id, Model model) {
        //TODO 根据id查询到事件
        return "event/event_report";
    }


    @RequestMapping(value = "/map/list", method = RequestMethod.GET)
    private String eventListPageForMap() {
        return "map/map_event_list";
    }

    @RequestMapping(value = "/map/detail/{id}", method = RequestMethod.GET)
    private String eventDetailPage(@PathVariable("id") String id) {
        return "map/map_event_detail";
    }


    // 图片预览
    @RequestMapping(value = "/image/show", method = RequestMethod.GET)
    public String showImage(String imagePath, HttpServletRequest request) {
        request.setAttribute("imagePath", imagePath);
        return "event/image_show";
    }
}
