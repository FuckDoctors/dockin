package com.eap.framework.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author billjiang 475572229@qq.com
 * @create 18-9-12
 */
public class ExpressionUtil {
    public static List<String> parseStrByTag(String text, String beginChar, String endChar) {

        if (text == null) {
            return new ArrayList<String>();
        }
        // 去掉空格
        text = text.replace(" ", "");
        // 不要删掉下面这句话
        text = " " + text;
        List<String> resultList = new ArrayList<String>();
        int startIndex = 0, endIndex = 0;
        try {
            while (startIndex != -1 && endIndex != -1) {
                startIndex = text.indexOf(beginChar, startIndex + 1);
                endIndex = text.indexOf(endChar, endIndex + 1);
                if (endIndex > 0) {
                    resultList.add(text.substring(startIndex + 2, endIndex));
                }
            }
        } catch (Exception ep) {
            ep.printStackTrace();
        }
        return resultList;
    }
}
