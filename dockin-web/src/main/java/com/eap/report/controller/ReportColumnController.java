package com.eap.report.controller;

import com.alibaba.fastjson.JSON;
import com.eap.framework.base.entity.Dict;
import com.eap.framework.base.pojo.Result;
import com.eap.framework.base.pojo.TreeNode;
import com.eap.framework.base.service.BaseService;
import com.eap.framework.utils.StrUtil;
import com.eap.framework.wxtools.util.StringUtils;
import com.eap.report.entity.ReportColumn;
import com.eap.report.entity.ReportEntity;
import com.eap.report.service.ReportColumnService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报表列管理控制器
 *
 * @author jrn
 * 2018-09-11 10:19:55由代码生成器自动生成
 */
@Controller
@RequestMapping("/report/column")
public class ReportColumnController {

    @Resource
    private BaseService baseService;

    @Resource
    private ReportColumnService reportColumnService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list() {
        return "report/column/column_list";
    }

    @RequestMapping(value = "/tree/{reportId}", method = RequestMethod.GET)
    public String tree(@PathVariable("reportId") String reportId, Model model) {
        model.addAttribute("reportId", reportId);
        return "report/column/column_tree";
    }


    @RequestMapping(value = "/treeData/{reportId}", method = RequestMethod.POST)
    @ResponseBody
    public List<TreeNode> getTreeData(@PathVariable("reportId") String reportId) {
        return reportColumnService.getTreeData(reportId);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(String id, HttpServletRequest request) {
        request.setAttribute("id", id);
        return "report/column/column_edit";
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(String id, HttpServletRequest request) {
        request.setAttribute("id", id);
        return "report/column/column_detail";
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ReportColumn get(@PathVariable("id") String id) {
        return baseService.get(ReportColumn.class, id);
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public Result save(String obj) {
        ReportColumn reportcolumn = JSON.parseObject(obj, ReportColumn.class);
        if (reportcolumn.getDataType() != null && StringUtils.isNotBlank(reportcolumn.getDataType().getId()))
            reportcolumn.setDataType(baseService.get(Dict.class, reportcolumn.getDataType().getId()));
        if (reportcolumn.getComponentType() != null && StringUtils.isNotBlank(reportcolumn.getComponentType().getId()))
            reportcolumn.setComponentType(baseService.get(Dict.class, reportcolumn.getComponentType().getId()));
        reportcolumn.setReport(baseService.get(ReportEntity.class, reportcolumn.getReport().getId()));
        if (StrUtil.isEmpty(reportcolumn.getId())) {
            baseService.save(reportcolumn);
        } else {
            reportcolumn.setUpdateDateTime(new Date());
            baseService.update(reportcolumn);
        }
        return new Result(true);
    }


    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id) {
        ReportColumn reportcolumn = this.get(id);
        try {
            baseService.delete(reportcolumn);
            return new Result();
        } catch (Exception e) {
            return new Result(false, "该数据已经被引用，不可删除");
        }
    }


    /**
     * 校验当前编码的唯一性
     *
     * @return
     */
    @RequestMapping(value = "/checkUnique", method = RequestMethod.POST)
    @ResponseBody
    public Map checkExist(String reportId,String code, String id) {
        Map<String, Boolean> map = new HashMap<>();
        ReportColumn reportColumn = reportColumnService.getColumnByCode(reportId,code);
        if (reportColumn == null) {
            map.put("valid", true);
        } else {
            if (!StrUtil.isEmpty(id) && reportColumn.getId().equals(id)) {
                map.put("valid", true);
            } else {
                map.put("valid", false);
            }
        }
        return map;
    }

    //校验表达式的正确性
    @RequestMapping(value = "/checkExpression", method = RequestMethod.POST)
    @ResponseBody
    public Result checkExpression(String reportId,String expression) {
        return reportColumnService.checkExpression(reportId,expression);
    }

    //字典编辑
    @RequestMapping(value = "/dict/{code}",method = RequestMethod.GET)
    public String dictEdit(@PathVariable("code") String code,Model model){
        model.addAttribute("parentCode",code);
        return "report/column/dict_edit";
    }


}
