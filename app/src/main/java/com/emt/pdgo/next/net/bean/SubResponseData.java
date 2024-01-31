package com.emt.pdgo.next.net.bean;

/**
 * 返回json
 *
 * @author chenjh
 * @date 2018/11/27 16:51
 */
public class SubResponseData<T> {

    public String code;
    public String msg;
    public T info;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T result) {
        this.info = result;
    }

    /***
     *
     * @return
     */
    public boolean isSuccess() {
        return code.equals("10000");
    }
}
