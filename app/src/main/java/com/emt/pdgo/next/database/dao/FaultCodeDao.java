package com.emt.pdgo.next.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.emt.pdgo.next.database.entity.FaultCodeEntity;

import java.util.List;

@Dao
public interface FaultCodeDao {

    @Query("select * from faultcodeentity order by id desc")
    List<FaultCodeEntity> getCodeList();

    @Insert
    void insertFaultCode(FaultCodeEntity entity);

    @Query("DELETE FROM faultcodeentity")
    void delete();

}
