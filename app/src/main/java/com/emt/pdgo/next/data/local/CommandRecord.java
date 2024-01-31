package com.emt.pdgo.next.data.local;

import com.emt.pdgo.next.data.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.data.local
 * @ClassName: CommandRecord
 * @Description: 上下位机交互指令本地记录表
 * @Author: chenjh
 * @CreateDate: 2020/1/19 10:36 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/1/19 10:36 AM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
@Table(database = AppDatabase.class)
public class CommandRecord extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    public int id;

    @Column(name = "phone")
    public String phone;//用户手机号

    @Column(name = "uuid")
    public String uuid;//

    @Column(name = "time")
    public String time;//时间

    @Column(name = "command")
    public String command;//指令

    @Column(name = "type")
    public int type;//指令的类型 1：上位机发送的指令; 2：下位机发送的指令

}
