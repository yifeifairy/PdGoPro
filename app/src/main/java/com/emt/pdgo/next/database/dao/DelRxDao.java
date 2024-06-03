package com.emt.pdgo.next.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.emt.pdgo.next.database.entity.DelRxEntity;

import java.util.List;

@Dao
public interface DelRxDao {

    @Query("select * from delrxentity")
    List<DelRxEntity> delRxList();

    @Query("select * from delrxentity where id = (:id)")
    DelRxEntity getDelRxById(int id);

    @Insert
    void insertRx(DelRxEntity delRxEntity);

    @Update
    void update(DelRxEntity delRxEntity);

}
