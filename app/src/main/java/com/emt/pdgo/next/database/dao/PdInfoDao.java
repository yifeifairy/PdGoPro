package com.emt.pdgo.next.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.emt.pdgo.next.database.entity.PdInfoEntity;

import java.util.List;

@Dao
public interface PdInfoDao {

    @Query("select * from pdinfoentity")
    List<PdInfoEntity> getPdInfoList();

    @Insert
    void insertPd(PdInfoEntity PdInfoEntity);

    @Query("DELETE FROM pdinfoentity")
    void delete();

    @Update
    void update(PdInfoEntity PdInfoEntity);

}
