package com.eap.report.pojo;

import java.util.List;

/**
 * @author billjiang 475572229@qq.com
 * @create 18-9-12
 */
public class Expression {

    public List<Parameter> parameters;

    public String code;

    public static class Parameter {

        public String label;

        public String value;

        public String code;

    }

    public static class Result {

        public String label;

        public String value;

        public String code;

        public Integer digits;

    }
}
