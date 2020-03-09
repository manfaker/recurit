package com.shenzhen.recurit.vo;

import com.shenzhen.recurit.enums.ReturnEnum;
import lombok.Data;

import java.io.Serializable;

@Data
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
            return new ResultVO(true, ReturnEnum.SUCCESS.getCode(), ReturnEnum.SUCCESS.getValue(),null);
        }

        /**
         * 响应成功(带返回数据)
         * @param data 返回数据
         * @return Result
         */
        public static ResultVO success(Object data){
            return new ResultVO(true, ReturnEnum.SUCCESS.getCode(), ReturnEnum.SUCCESS.getValue(),data);
        }

        /**
         * 响应成功(带返回数据)
         * @param msg 返回数据
         * @return Result
         */
        public static ResultVO success(String  msg){
            return new ResultVO(true, ReturnEnum.SUCCESS.getCode(),msg,null);
        }

        /**
         * 响应成功(带返回数据)
         * @param msg
         * @param data 返回数据
         * @return Result
         */
        public static ResultVO success(String  msg,Object data){
            return new ResultVO(true, ReturnEnum.SUCCESS.getCode(),msg,data);
        }


        /**
         * 默认返回301sss
         * @return Result
         */
        public static ResultVO error( ){
            return new ResultVO(false, ReturnEnum.DEFAULT_301.getCode(), ReturnEnum.DEFAULT_302.getValue(),null);
        }

        /**
         * 响应错误(不带状态码,带消息)
         * @return Result
         */
        public static ResultVO error(String msg){
            return new ResultVO(false, ReturnEnum.DEFAULT_301.getCode(),msg,null);
        }

        /**
         * 响应错误(带错误码,消息提醒)
         * @return
         */
        public static ResultVO error(Integer code,String msg){
            return new ResultVO(false,code,msg,null);
        }


}
