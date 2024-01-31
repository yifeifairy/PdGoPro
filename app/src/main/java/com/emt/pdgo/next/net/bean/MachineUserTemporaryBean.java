package com.emt.pdgo.next.net.bean;

/**
 * 机器临时关联用户信息
 */
public class MachineUserTemporaryBean {

    public String msg;

    public String code;

    public UserInfoBean info;

    /***
     *
     * @return
     */
    public boolean isSuccess() {
        return code.equals("10000");
    }

}
