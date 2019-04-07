/**
 * Bootstrap 时间选择控件
 * @author billjiang  qq:475572229
 * @created 2018/7/24
 *
 */
(function ($, window, document, undefined) {
    'use strict';

    var pluginName = 'calendar';

    //入口方法
    $.fn[pluginName] = function (options) {
        var self = $(this);
        if (this == null)
            return null;
        var data = this.data(pluginName);
        if (!data) {
            data = new BaseCalendar(this, options);
            self.data(pluginName, data);
        }
        return data;
    };


    var BaseCalendar = function (element, options) {
        this.$element = $(element);
        this.options = $.extend(true, {}, this.default, options);
        this.init();
    }

    //默认配置
    BaseCalendar.prototype.default = {
        header: ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期天'],
        days: 10,
        startDate: null,
        endDate: null,
        countCss: 'green',
        maleCss: 'blue',
        femaleCss: 'yellow',
        gender: null,
        readonly:false
    }

    //结构模板
    BaseCalendar.prototype.template = {
        thead: '  <thead>\n' +
        '            <tr>\n' +
        '                <td>{0}</td>' +
        '                <td>{1}</td>' +
        '                <td>{2}</td>' +
        '                <td>{3}</td>' +
        '                <td>{4}</td>' +
        '                <td>{5}</td>' +
        '                <td>{6}</td>' +
        '            </tr>' +
        '            </thead>',
        tbody: '<tbody></tbody>',
        row_progress: ' <div class="row">' +
        '        <div class="col-sm-8">' +
        '        <div class="progress progress-xs">' +
        '        <div class="progress-bar progress-bar-{0}" style="width: {1}%">' +
        '        </div>' +
        '        </div>' +
        '        </div>' +
        '        <div class="col-sm-4">' +
        '        <span class="text text-info">{2}/{3}</span>' +
        '        </div>' +
        '        </div>',
        row_date: '<div class="row date">{0}</div>'
    }

    //初始化
    BaseCalendar.prototype.init = function () {
        this.$element.html("");
        this.builder(this.options.data);
    }

    //使用模板搭建页面结构
    BaseCalendar.prototype.builder = function (data) {
        var thead = $(this.template.thead.format(this.options.header[0], this.options.header[1],
            this.options.header[2], this.options.header[3], this.options.header[4],
            this.options.header[5], this.options.header[6]));
        this.$element.append(thead);
        var tbody = $(this.template.tbody);
        var datas = this.loadData();
        var len = datas.length;
        var index = 0;
        var self = this;
        for (var row = 0; row < len / 7; row++) {
            var tr = $("<tr style='cursor:pointer'></tr>");
            for (var col = 0; col < 7; col++) {
                var item = datas[index];
                var td = $("<td></td>");
                if (item.isHoliday == 0) {
                    var ratio_count = parseInt(100 * item.personCurCount / item.personCount);
                    var row_count = $(this.template.row_progress.format(this.options.countCss, ratio_count, item.personCurCount, item.personCount));
                    td.append(row_count);
                    var ratio_male = parseInt(100 * item.maleCurCount / item.maleCount);
                    var row_male = $(this.template.row_progress.format(this.options.maleCss, ratio_male, item.maleCurCount, item.maleCount));
                    td.append(row_male);
                    var ratio_female = parseInt(100 * item.femaleCurCount / item.femaleCount);
                    var row_female = $(this.template.row_progress.format(this.options.femaleCss, ratio_female, item.femaleCurCount, item.femaleCount));
                    td.append(row_female);
                    var row_count_date = $(this.template.row_date.format(formatDate(item.currentDate, 'yyyy-mm-dd')));
                    td.append(row_count_date)
                    td.attr("title", "点击预约该日期");
                    td.click(function () {
                        self.selectDate(this);
                    })
                } else {
                    var row_count_desc = $(this.template.row_date.format(item.description));
                    td.append(row_count_desc)
                    var row_count_date = $(this.template.row_date.format(formatDate(item.currentDate, 'yyyy-mm-dd')));
                    td.append(row_count_date)
                    td.addClass("disableDate");
                    td.attr("title", "不可预约");
                }

                tr.append(td);
                index++;
            }
            tbody.append(tr);
        }
        this.$element.append(tbody);

    };

    BaseCalendar.prototype.selectDate = function (obj) {
        //清除掉原来
        var td = this.$element.find("td.active");
        if (td) {
            var cdate = td.find(".date:last").text();
            td.removeClass("active");
            $("#tr_" + cdate).remove();
        }
        var date = $(obj).find(".date:last").text().trim();
        $(obj).addClass("active");
        var datas = this.loadTimeData(date);
        this.buildTimeTable(datas, date, obj);
        this.date = date;
        if (this.options.datecallback) {
            this.options.datecallback.call(this, this.date);
        }
    }


    BaseCalendar.prototype.loadTimeData = function (date) {
        var datas;
        ajaxPost(basePath + "/holiday/getTimeVos", {dateStr: date}, function (result) {
            datas = result;
        })
        return datas;
    }

    BaseCalendar.prototype.timeTemplate = {
        td_title: '<td>{0}</td>',
        td_data: ' <td clsss="dateTime" title="点击选中该时间段">' +
        '        <div class="row">' +
        '        <div class="col-sm-8">' +
        '        <div class="progress progress-xs">' +
        '        <div class="progress-bar progress-bar-{0}" style="width: {1}%">' +
        '        </div>' +
        '        </div>' +
        '        </div>' +
        '        <div class="col-sm-4">' +
        '        <span class="text text-info">{2}/{3}</span>' +
        '        </div>' +
        '        </div>' +
        '        <div class="row time">{4}</div>' +
        '    </td>',
        tr: '<tr style="cursor:pointer"></tr>'
    }

    /**
     * 生成时间段选择表格
     * @param datas 时间段数据
     * @param date 当前时间
     * @param obj 选中TD
     */
    BaseCalendar.prototype.buildTimeTable = function (datas, date, obj) {
        var tr = $(this.timeTemplate.tr);
        tr.attr("id", "tr_" + date)
        var td = $("<td colspan='7'></td>");
        var table = $('<table style="width: 100%;height:100%" class="timeTable"></table>');
        var lenObj = this.getMaxLenObj(datas);
        var maxLen = lenObj.sw > lenObj.xw ? lenObj.sw : lenObj.xw;
        var s_tr = $(this.timeTemplate.tr);
        var x_tr = $(this.timeTemplate.tr);
        var s_td_title = $(this.timeTemplate.td_title.format("上午"));
        var x_td_title = $(this.timeTemplate.td_title.format("下午"));
        s_td_title.addClass("disableTitle");
        x_td_title.addClass("disableTitle");
        s_tr.append(s_td_title);
        var self = this;
        for (var i = 0; i < maxLen; i++) {
            if (i < lenObj.sw) {
                if (datas[i].enabled == "1"||self.options.readonly) {
                    var dataObj = this.getItemData(datas[i]);
                    var s_td_data = $(this.timeTemplate.td_data.format(dataObj.bgCss, dataObj.ratio, dataObj.curAmount, dataObj.amount, dataObj.timeName));
                    s_tr.append(s_td_data);
                    s_td_data.click(function () {
                        self.selectTime(this);
                    })
                } else {
                    s_tr.append($("<td class='disableTime'></td>"));
                }
            } else {
                s_tr.append($("<td class='disableTime'></td>"));
            }
        }
        table.append(s_tr);
        x_tr.append(x_td_title);
        for (var i = 0; i < maxLen; i++) {
            if (i < lenObj.xw) {
                if (datas[lenObj.sw + i].enabled == "1"||self.options.readonly) {
                    var dataObj = this.getItemData(datas[lenObj.sw + i]);
                    var x_td_data = $(this.timeTemplate.td_data.format(dataObj.bgCss, dataObj.ratio, dataObj.curAmount, dataObj.amount, dataObj.timeName));
                    x_td_data.click(function () {
                        self.selectTime(this);
                    })
                    x_tr.append(x_td_data);
                } else {
                    x_tr.append($("<td class='disableTime'></td>"));
                }
            } else {
                x_tr.append($("<td class='disableTime'></td>"));
            }
        }
        table.append(x_tr);
        td.append(table);
        tr.append(td);
        $(obj).parent().after(tr);
    }


    BaseCalendar.prototype.selectTime = function (obj) {
        var curTime = $(obj).find("div.time").text().trim();
        this.time = curTime;
        $(".timeTable td.dateTime").removeClass("actived");
        $(obj).addClass("actived");
        if (this.options.timecallback) {
            this.options.timecallback.call(this, this.date + " " + this.time);
        }
    }


    BaseCalendar.prototype.getItemData = function (item) {
        var bgCss, ratio, curAmount, amount, timeName;
        if (this.options.gender == '1') {
            bgCss = this.options.maleCss;
            ratio = parseInt(100 * item.maleCurAmount / item.maleAmount);
            curAmount = item.maleCurAmount;
            amount = item.maleAmount;
        } else if (this.options.gender == '0') {
            bgCss = this.options.femaleCss;
            ratio = parseInt(100 * item.femaleCurAmount / item.femaleAmount);
            curAmount = item.femaleCurAmount;
            amount = item.femaleAmount;
        } else {
            bgCss = this.options.countCss;
            ratio = parseInt(100 * item.curAmount / item.amount);
            curAmount = item.curAmount;
            amount = item.amount;
        }
        timeName = item.name;
        var dataObj = {};
        dataObj["bgCss"] = bgCss;
        dataObj["ratio"] = ratio;
        dataObj["curAmount"] = curAmount;
        dataObj["amount"] = amount;
        dataObj["timeName"] = timeName;
        return dataObj;
    }

    BaseCalendar.prototype.getMaxLenObj = function (datas) {
        var lenObj = {};
        lenObj["sw"] = 0;
        lenObj["xw"] = 0;
        for (var i = 0; i < datas.length; i++) {
            if (datas[i].type == 0) {
                lenObj["sw"]++;
            } else {
                lenObj["xw"]++;
            }
        }
        return lenObj;
    }

    BaseCalendar.prototype.loadData = function () {
        if (this.options.data)
            return this.options.data;
        var param = {};
        param.days = this.options.days;
        param.startDate = this.options.startDate;
        param.endDate = this.options.endDate;
        var self = this;

        ajaxPost(basePath + "/holiday/getList", param, function (result) {
            self.options.data = result;
        });
        var firstDates = this.getFirstToStartDates(this.options.data[0].currentDate);
        var lastDates = this.getEndToLastDates(this.options.data[this.options.data.length - 1].currentDate);
        self.options.data = firstDates.concat(self.options.data).concat(lastDates);
        return self.options.data;
    }

    BaseCalendar.prototype.getFirstToStartDates = function (startDate) {
        startDate = new Date(startDate);
        var week = startDate.getDay() == 0 ? 7 : startDate.getDay();
        week = week - 1;
        var firstDate = new Date(startDate);
        firstDate.setDate(firstDate.getDate() - week);
        var retArr = [];
        while (firstDate.getTime() < startDate.getTime()) {
            var obj = {};
            obj.currentDate = firstDate.getTime();
            obj.holiday = "1";
            obj.description = "已过期";
            firstDate.setDate(firstDate.getDate() + 1);
            retArr.push(obj);
        }
        return retArr;
    }

    BaseCalendar.prototype.getEndToLastDates = function (endDate) {
        endDate = new Date(endDate);
        var week = endDate.getDay() == 0 ? 7 : endDate.getDay();
        var lastDate = new Date(endDate);
        lastDate.setDate(lastDate.getDate() + 7 - week);
        var retArr = [];
        while (endDate.getTime() < lastDate.getTime()) {
            var obj = {};
            endDate.setDate(endDate.getDate() + 1);
            obj.currentDate = endDate.getTime();
            obj.holiday = "1";
            obj.description = "尚未开始";
            retArr.push(obj);
        }
        return retArr;
    }


    BaseCalendar.prototype.refresh = function () {
        this.options.data=null;
        this.init();
    }

    BaseCalendar.prototype.resetParam = function (options) {
        this.options.startDate = options.startDate;
        this.options.endDate = options.endDate;
        if (options.gender != undefined)
            this.options.gender = options.gender;
    }

    String.prototype.format = function () {
        if (arguments.length == 0) return this;
        for (var s = this, i = 0; i < arguments.length; i++)
            s = s.replace(new RegExp("\\{" + i + "\\}", "g"), arguments[i]);
        return s;
    };


})(jQuery, window, document)
