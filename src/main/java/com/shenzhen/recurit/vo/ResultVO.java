package com.shenzhen.recurit.vo;

import com.shenzhen.recurit.enums.ReqturnEnum;

import java.io.Serializable;

public class ResultVO implements Serializable {
        // 结果标记(true:执行成功 false:执行失败)
        private Boolean flag;
        // 消息状态码
        private Integer code;
        // 消息
        private String msg;
        // 返回数据
        private Object data;

        private ResultVO(Boolean flag, Integer code, String msg, Object data) {
            this.flag = flag;
            this.code = code;
            this.msg = msg;
            this.data = data;
        }

        /**
         * 响应成功
         * @return Result
         */
        public static ResultVO success(){
            return new ResultVO(true, ReqturnEnum.SUCCESS.getCode(), ReqturnEnum.SUCCESS.getValue(),null);
        }

        /**
         * 响应成功(带返回数据)
         * @param data 返回数据
         * @return Result
         */
        public static ResultVO success(Object data){
            return new ResultVO(true, ReqturnEnum.SUCCESS.getCode(), ReqturnEnum.SUCCESS.getValue(),data);
        }

        /**
         * 响应成功(带返回数据)
         * @param msg 返回数据
         * @return Result
         */
        public static ResultVO success(String  msg){
            return new ResultVO(true, ReqturnEnum.SUCCESS.getCode(),msg,null);
        }

        /**
         * 响应成功(带返回数据)
         * @param msg
         * @param data 返回数据
         * @return Result
         */
        public static ResultVO success(String  msg,Object data){
            return new ResultVO(true, ReqturnEnum.SUCCESS.getCode(),msg,data);
        }


        /**
         * 默认返回302
         * @return Result
         */
        public static ResultVO error( ){
            return new ResultVO(false, ReqturnEnum.DEFAULT_302.getCode(), ReqturnEnum.DEFAULT_302.getValue(),null);
        }

        /**
         * 响应错误(不带状态码,带消息)
         * @return Result
         */
        public static ResultVO error(String msg){
            return new ResultVO(false, ReqturnEnum.DEFAULT_302.getCode(),msg,null);
        }

        /**
         * 响应错误(带错误码,消息提醒)
         * @return
         */
        public static ResultVO errorMsg(Integer code,String msg){
            return new ResultVO(false,code,msg,null);
        }


        public Boolean getFlag() {
            return flag;
        }

        public void setFlag(Boolean flag) {
            this.flag = flag;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

}
