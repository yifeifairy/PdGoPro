package com.emt.pdgo.next.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DelRxEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int num;

}
