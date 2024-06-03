package com.emt.pdgo.next.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.emt.pdgo.next.database.entity.DelPdEntity;

import java.util.List;

@Dao
public interface DelPdDao {

    @Query("select * from delpdentity")
    List<DelPdEntity> delPdList();

    @Query("select * from delpdentity where id = (:id)")
    DelPdEntity getDelPdById(int id);

    @Insert
    void insertRx(DelPdEntity delPdEntity);

    @Update
    void update(DelPdEntity delPdEntity);

}
