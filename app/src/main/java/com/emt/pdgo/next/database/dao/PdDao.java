package com.emt.pdgo.next.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.emt.pdgo.next.database.entity.PdEntity;

import java.util.List;

@Dao
public interface PdDao {

    @Query("select * from pdentity where id = (:id)")
    List<PdEntity> getPdListById(int id);

    @Query("select * from pdentity")
    List<PdEntity> getPdList();

    @Insert
    void insertPd(PdEntity PdEntity);

    @Query("DELETE FROM pdentity")
    void delete();


}
