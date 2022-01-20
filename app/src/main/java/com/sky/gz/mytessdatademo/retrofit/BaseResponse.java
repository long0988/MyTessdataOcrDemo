package com.sky.gz.mytessdatademo.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * @author shiqilong
 * @date 2020/1/2
 * Description:
 */
public class BaseResponse<T> {
    private int code;
    private String msg;
    @SerializedName(value = "obj", alternate = "data")
    private T obj;
    private boolean succ;
    private String oper;//"检测版本更新"
    private String token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public boolean isSucc() {
        return succ;
    }

    public void setSucc(boolean succ) {
        this.succ = succ;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
