(function ($, window, document, undefined) {
    window.render = {
        checkbox:function (value, type, rowObj) {
            return "<input type='checkbox' value='"+value+"' class='checkbox-js' style='cursor:pointer;margin:0px;'>";
        },
        //下拉框，
        select:function (val,rowObj,selector) {

            var sel = $(selector);
            if (sel.children().length > 0) {
                return false;
            }
            var blank_value = sel.data("blank-value");
            var blank_text = sel.data("blank-text");
            var is_blank = sel.data("blank") ? true : false;
            var dictCode=sel.data("code");
            var builder = function (data) {
                if (is_blank) {
                    if (!blank_value && !blank_text)
                        sel.append($('<option></option>'));
                    else if (!blank_text)
                        sel.append($("<option value='" + blank_value + "'></option>"));
                    else if (!blank_value)
                        sel.append($("<option>" + blank_text + "</option>"));
                    else
                        sel.append($("<option value='" + blank_value + "'>" + blank_text + "</option>"));
                }
                if (data && data.length > 0) {
                    var value = sel.data("value") ? sel.data("value") : "id";
                    var text = sel.data("text") ? sel.data("text") : "name";
                    for (var i = 0; i < data.length; i++) {
                        var option = $("<option value='" + data[i][value] + "'>" + data[i][text] + "</option>");
                        if(data[i][value]==val){
                            option = $("<option value='" + data[i][value] + "' selected>" + data[i][text] + "</option>");
                        }
                        sel.append(option);
                    }
                }

            }
            $dataSource.getDict(dictCode, builder);
            return sel.prop("outerHTML");
        }
    }




})(jQuery, window, document);