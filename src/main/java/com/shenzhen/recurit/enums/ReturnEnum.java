package com.shenzhen.recurit.enums;

public enum ReqturnEnum {

        DEFAULT_300(300,"失败返回300"),
        DEFAULT_301(301,"失败返回301"),
        DEFAULT_302(302,"失败返回302"),
        DEFAULT_400(400,"失败返回400"),
        DEFAULT_401(401,"失败返回401"),
        DEFAULT_402(402,"失败返回402"),
        DEFAULT_500(500,"失败返回500"),
        DEFAULT_501(501,"失败返回501"),
        DEFAULT_502(502,"失败返回502"),
        DEFAULT_503(503,"失败返回503"),
        SUCCESS(200,"失败成功");




        private Integer code;
        private String value;

        private ReqturnEnum(Integer code, String value){
            this.code=code;
            this.value=value;
        }
        public Integer getCode() {
            return this.code;
        }

        public String getValue() {
            return this.value;
        }
}
