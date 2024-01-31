package com.emt.pdgo.next.data.serial;

/**
 * 接收下位机发布消息/数据
 */
public class ReceivePublicDataBean2<T> {

    public ReceivePublicBean<T> publish;
    /*** md5校验值 **/
    public String sign;

}

