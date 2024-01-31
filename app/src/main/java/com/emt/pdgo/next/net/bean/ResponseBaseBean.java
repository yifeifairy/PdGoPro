package com.emt.pdgo.next.net.bean;

import java.io.Serializable;

/**
 * BaseBean
 *
 * @author chenjh
 * @date 2018/11/27 16:38
 */
public class ResponseBaseBean<T> implements Serializable {

    private static final long serialVersionUID = -1569296898641660272L;
    public String status;

    public String message;

    public T data;

}