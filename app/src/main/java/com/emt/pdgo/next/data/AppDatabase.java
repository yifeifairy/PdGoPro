package com.emt.pdgo.next.data;


import com.raizlabs.android.dbflow.annotation.Database;

/**
 *
 * @ProjectName:
 * @Package:        com.emt.pdgo.next.data
 * @ClassName:      AppDatabase
 * @Description:    新建数据库
 * @Author:         chenjh
 * @CreateDate:     2019/12/3 4:07 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/12/3 4:07 PM
 * @UpdateRemark:   更新内容
 * @Version:        1.0
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public abstract class AppDatabase {
    //数据库名称
    public static final String NAME = "pdgo_next";
    //数据库版本号
    public static final int VERSION = 2;
}