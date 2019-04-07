(function ($, window, document, undefined) {
    window.BaseUtil = {
        IDCard: {
            getAge: function (identityCard) {
                var len = (identityCard + "").length;
                if (len == 0) {
                    return 0;
                } else {
                    if ((len != 15) && (len != 18))//身份证号码只能为15位或18位其它不合法
                    {
                        return 0;
                    }
                }
                var strBirthday = "";
                if (len == 18)//处理18位的身份证号码从号码中得到生日和性别代码
                {
                    strBirthday = identityCard.substr(6, 4) + "/" + identityCard.substr(10, 2) + "/" + identityCard.substr(12, 2);
                }
                if (len == 15) {
                    strBirthday = "19" + identityCard.substr(6, 2) + "/" + identityCard.substr(8, 2) + "/" + identityCard.substr(10, 2);
                }
                //时间字符串里，必须是“/”
                var birthDate = new Date(strBirthday);
                var nowDateTime = new Date();
                var age = nowDateTime.getFullYear() - birthDate.getFullYear();
                //再考虑月、天的因素;.getMonth()获取的是从0开始的，这里进行比较，不需要加1
                if (nowDateTime.getMonth() < birthDate.getMonth() || (nowDateTime.getMonth() == birthDate.getMonth() && nowDateTime.getDate() < birthDate.getDate())) {
                    age--;
                }
                return age;
            },
            getSex: function (identityCard) {
                var sexno, sex
                if (identityCard.length == 18) {
                    sexno = identityCard.substring(16, 17)
                } else if (identityCard.length == 15) {
                    sexno = identityCard.substring(14, 15)
                } else {
                    alert("错误的身份证号码，请核对！")
                    return false
                }
                var tempid = sexno % 2;
                if (tempid == 0) {
                    sex = '0'
                } else {
                    sex = '1'
                }
                return sex
            }
        },
        Status: {
            SAVE: 0,
            //审核拒绝
            REJECT: 10,
            //可进行下次预约 约了没去，下次再约
            NEXT: 20,
            //登记提交 待审核
            APPOVE: 30,
            //审核通过 可预约
            RESER: 40,
            //已预约
            RESERED: 50,
            //已体检
            CHECKUP: 60
        }
    }


})(jQuery, window, document);