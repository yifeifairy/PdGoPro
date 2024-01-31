package com.emt.pdgo.next.data;

import com.emt.pdgo.next.data.local.CommandRecord;
import com.emt.pdgo.next.data.local.TreatmentHistory;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.data
 * @ClassName: DbSource
 * @Description: 数据库操作接口
 * @Author: chenjh
 * @CreateDate: 2020/1/19 10:47 AM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/1/19 10:47 AM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public interface DbSource {

    Flowable<List<TreatmentHistory>> getAllTreatmentHistory();

    Flowable<List<CommandRecord>> getAllCommandRecord();

    Flowable<Boolean> insertNewCommandRecord(int type, String time, String command);

    Flowable<Boolean> clearCommandRecord();

}
