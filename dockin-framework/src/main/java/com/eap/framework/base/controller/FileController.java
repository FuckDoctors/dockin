package com.eap.framework.base.controller;

import com.eap.framework.base.entity.SysFile;
import com.eap.framework.base.service.BaseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by billJiang on 2017/3/5.
 * e-mail:475572229@qq.com  qq:475572229
 * 文件上传下载控制器,部分业务方法卸载UploaderController中
 */
@Controller
@RequestMapping(value="/file")
public class FileController {

    @Resource
    public BaseService baseService;

    @RequestMapping(value = "/get/{id}",method = RequestMethod.POST)
    @ResponseBody
    public SysFile get(@PathVariable("id") String id){
       return baseService.get(SysFile.class,id);
    }




}
