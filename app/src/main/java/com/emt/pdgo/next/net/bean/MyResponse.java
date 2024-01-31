package com.emt.pdgo.next.net.bean;

/**
 * 返回json
 *
 * @author chenjh
 * @date 2018/11/27 16:51
 */
public class MyResponse<T> {

    public String status;
    public String message;
    public T data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String mstatus) {
        this.status = mstatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T result) {
        this.data = result;
    }

    /**
     * @return boolean
     */
    public boolean isSuccess() {
        return status.equals("200");
    }
}
