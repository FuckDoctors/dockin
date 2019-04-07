package com.eap.framework.utils;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * @author billjiang 475572229@qq.com
 * @create 18-9-12
 */
public class JEPUtil {
    public static String evaluateString(String str) {

        Context ctx = Context.enter();
        Scriptable scope = ctx.initStandardObjects();
        Object result;
        try {
            result = ctx.evaluateString(scope, str, null, 0, null);
        } catch (Exception ex) {
            System.out.println(str + "::::" + ex.getMessage().toString());
            return str;
        }
        return result.toString();
    }

    public static void main(String args[]) {

        /*
         * String str=
         * "Array.prototype.indexOf = function(val) {for (var i = 0; i < this.length; i++) {if (this[i] == val) return i;} return -1;}; Array.prototype.remove = function(val) { var index = this.indexOf(val); if (index > -1) { this.splice(index, 1);}};"
         * ;
         * str+="var array = [100, 200, 300]; array.remove(100);array.remove(300);"
         * ;
         */
        String str = "if(3>1){2+(4>0)?4:2}else{0}";
        System.out.println(evaluateString(str));

    }
}
