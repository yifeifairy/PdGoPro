package com.emt.pdgo.next.data;

import com.raizlabs.android.dbflow.annotation.Column;

import java.io.Serializable;

/**
 * Created by chenjh on 2019/11/12.
 */

public class CommandLogItem implements Serializable {


    public String phone;//用户手机号

    public String uuid;//

    public String time;//时间

    public String command;//指令

    @Column(name = "type")
    public int type;//指令的类型 1：上位机发送的指令; 2：下位机发送的指令

    public CommandLogItem(int type, String mTime, String mCommand) {
        this.type = type;
        this.time = mTime;
        this.command = mCommand;
    }


}