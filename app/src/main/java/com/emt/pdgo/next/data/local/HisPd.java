package com.emt.pdgo.next.data.local;

import com.emt.pdgo.next.data.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * 历史治疗数据
 */
@Table(database = AppDatabase.class)
public class HisPd extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int id; // 方案编号
    public List<HisPdItem> hisPdItems;

    @Column(name = "startTime")
    public String startTime; // 开始治疗时间
    @Column(name = "endTime")
    public String endTime; // 结束治疗时间

    @OneToMany(methods = {OneToMany.Method.ALL},
            variableName = "hisPdItems")
    public List<HisPdItem> getHisPdItems() {
        if (hisPdItems == null) {
            hisPdItems = new Select()
                    .from(HisPdItem.class)
                    .where(HisPdItem_Table.id.eq(id))
                    .queryList();
        }
        return hisPdItems;
    }


}
