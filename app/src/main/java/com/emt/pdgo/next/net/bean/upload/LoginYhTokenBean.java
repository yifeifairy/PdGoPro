package com.emt.pdgo.next.net.bean.upload;


/**
 * 获取医护后台token
 *
 * @author chenjh
 * @date 2022/10/08 10:38
 */
public class LoginYhTokenBean {

//    public String token = "";

    private String msg;
    private String token;
    private String code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
