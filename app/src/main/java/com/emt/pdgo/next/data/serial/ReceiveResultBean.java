package com.emt.pdgo.next.data.serial;

/**
 * 接收下位机的应答信息
 */
public class ReceiveResultBean<T> {

    public String topic;

    public int code;

    public String msg;

    public T data;

}
