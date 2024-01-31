package com.emt.pdgo.next.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FaultCodeEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String time;

    public String code;

}
