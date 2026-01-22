package com.os10.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseMessage<T> {
    private int code;
    private String msg;
    private T data;

    /**
     * 成功返回
     * @param data
     * @return
     */
    public static <T> BaseMessage<T> success(T data){
        return new BaseMessage(0,"执行成功",data);
    }

    /**
     * 失败返回
     * @param code
     * @param msg
     * @return
     */
    public static <T> BaseMessage<T> error(int code,String msg){
        return new BaseMessage(code,msg,null);
    }

    public static <T> BaseMessage<T> error(String msg){
        return new BaseMessage(-1, msg,null);
    }

    public static <T> BaseMessage<T> error(String msg, Object data){
        return new BaseMessage(-1, msg,data);
    }

    public static <T> BaseMessage<T> success(String msg){
        return new BaseMessage(0,msg,null);
    }

    public static <T> BaseMessage<T> success(String msg, Object data){
        return new BaseMessage(0,msg,data);
    }

    /**
     * 失败返回
     * @param code
     * @param msg
     * @return
     */
    public static  <T> BaseMessage<T> error(int code,String msg,Object data){
        return new BaseMessage(code,msg,data);
    }


    /**
     * 未输入必填参数
     * @return
     */
    public static  BaseMessage paramsEmpty(){
        return new BaseMessage(-1,"请输入必填参数",false);
    }

    /**
     * 系统异常
     * @return
     */
    public static  <T> BaseMessage<T> exception(T msg){
        return new BaseMessage<T>(-1,"您的系统出现异常",msg);
    }


    /**
     * 系统异常
     * @return
     */
    public static  <T> BaseMessage<T> exception(int code,String msg){
        return new BaseMessage<T>(code,msg,null);
    }


    public static <T> BaseMessage<T> auto(boolean status,String successMsg,String errorMsg){
        return status? success(successMsg) :error(errorMsg);
    }

    public static <T> BaseMessage<T> auto(boolean status,String successMsg,String errorMsg, Object data){
        return status? success(successMsg, data) :error(errorMsg, data);
    }
}
