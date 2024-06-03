package com.emt.pdgo.next.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.emt.pdgo.next.database.entity.RxEntity;

import java.util.List;

@Dao
public interface RxDao {

    @Query("select * from rxentity order by id desc")
    List<RxEntity> getRxList();

    @Insert
    void insertRx(RxEntity rxEntity);

    @Query("DELETE FROM rxentity")
    void delete();

    @Query("select * from rxentity where id > (:id)")
    List<RxEntity> getRxListById(int id);
}
