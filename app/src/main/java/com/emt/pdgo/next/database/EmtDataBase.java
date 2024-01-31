package com.emt.pdgo.next.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.emt.pdgo.next.database.dao.FaultCodeDao;
import com.emt.pdgo.next.database.dao.PdDao;
import com.emt.pdgo.next.database.dao.PdInfoDao;
import com.emt.pdgo.next.database.dao.RxDao;
import com.emt.pdgo.next.database.entity.FaultCodeEntity;
import com.emt.pdgo.next.database.entity.PdEntity;
import com.emt.pdgo.next.database.entity.PdInfoEntity;
import com.emt.pdgo.next.database.entity.RxEntity;

@Database(entities = {RxEntity.class, PdEntity.class, FaultCodeEntity.class, PdInfoEntity.class}, version = 6)
public abstract class EmtDataBase extends RoomDatabase {

    private static final String DB_NAME = "EmtDataBase.db";
    private static volatile EmtDataBase instance;

    public static synchronized EmtDataBase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static EmtDataBase create(final Context context) {
//        String path = EmtFileSdkUtil.getBaseDirFile() + File.separator + DB_NAME;
        return Room.databaseBuilder(
                context,
                EmtDataBase.class,
                DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public abstract RxDao getRxDao();

    public abstract PdDao getPdDao();

    public abstract PdInfoDao getPdInfoDao();

    public abstract FaultCodeDao getFaultCodeDao();

//    public abstract FaultMsgDao getFaultMsgDao();

}
