package com.emt.pdgo.next.data.serial.receive;


import com.emt.pdgo.next.data.serial.ReceivePublicBean;

public class ReceivePublicDeviceBean2 {

    public ReceivePublicBean<ReceivePublicBean<ReceiveDeviceBean>> publish;
    /*** md5校验值 **/
    public String sign;

}

// class ReceivePublicBean2<T> {
//
//    public String topic;
//
//    public String msg;
//
//    public T data;
//
//
//}

