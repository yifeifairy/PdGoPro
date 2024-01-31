package com.emt.pdgo.next.data;

import android.util.Log;

import com.emt.pdgo.next.data.local.CommandRecord;
import com.emt.pdgo.next.data.local.MainBoardOrder;
import com.emt.pdgo.next.data.local.TemperatureControlParameter;
import com.emt.pdgo.next.data.local.TreatmentHistory;
import com.emt.pdgo.next.data.local.TreatmentHistory_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.data
 * @ClassName: PdGoDbManager
 * @Description: 本地数据库数据操作
 * @Author: chenjh
 * @CreateDate: 2019/12/3 5:52 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/3 5:52 PM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class PdGoDbManager implements DbSource {
    private final static String TAG = "PdGoDbManager";

    private static PdGoDbManager instance;
    private static byte[] lock = new byte[0];


    public static PdGoDbManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                instance = new PdGoDbManager();
            }
        }
        return instance;
    }


    private SimpleDateFormat mTimeFormat;

    private static MainBoardOrder mainBoardOrderTable;
    //温控参数设置
    private static TemperatureControlParameter temperatureBoardOrderTable;

    /**
     * 初始化主板默认数据
     */
    public void initMainBoardTable() {

        try {
            mainBoardOrderTable = SQLite.select()
                    .from(MainBoardOrder.class)//查询第一个
                    .querySingle();
            if (mainBoardOrderTable == null) {
                Log.e(TAG, "initMainBoardParameter()  mainBoardOrderTable is null");
                mainBoardOrderTable = new MainBoardOrder();
                mainBoardOrderTable.id = 1;
                mainBoardOrderTable.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "initMainBoardParameter() Exception :" + e.getMessage());
        }
    }

    /**
     * 初始化温控板默认参数值
     */
    public void initTemperBoardTable() {

        try {
            temperatureBoardOrderTable = SQLite.select()
                    .from(TemperatureControlParameter.class)//查询第一个
                    .querySingle();
            if (temperatureBoardOrderTable == null) {
                Log.e(TAG, "initTemperBoardParameter()  temperatureBoardOrderTable is null");
                temperatureBoardOrderTable = new TemperatureControlParameter();
                temperatureBoardOrderTable.id = 1;
                temperatureBoardOrderTable.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "initTemperBoardParameter() Exception :" + e.getMessage());
        }

    }


    public MainBoardOrder getMainBoardTable() {
        if (mainBoardOrderTable == null) {
            initMainBoardTable();
        }
        return mainBoardOrderTable;
    }

    public TemperatureControlParameter getTemperatureBoardTable() {
        if (temperatureBoardOrderTable == null) {
            initTemperBoardTable();
        }
        return temperatureBoardOrderTable;
    }

    public List<TreatmentHistory> getTreatmentHistoryList(String uuid) {

        return SQLite.select().from(TreatmentHistory.class)
                .where(TreatmentHistory_Table.uuid.is(uuid))
                .queryList();
    }

    /**
     * 根据uuid和周期查询数据
     *
     * @param uuid
     * @param cycle
     * @return
     */
    public TreatmentHistory findByUUIDAndPeriod(String uuid, int cycle) {
        TreatmentHistory mc;
        try {
            mc = SQLite.select().from(TreatmentHistory.class)
                    .where(TreatmentHistory_Table.uuid.is(uuid))
                    .and(TreatmentHistory_Table.cycle.is(cycle))
                    .querySingle();
            return mc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Flowable<List<TreatmentHistory>> getAllTreatmentHistory() {
        return Flowable.create(new FlowableOnSubscribe<List<TreatmentHistory>>() {
            @Override
            public void subscribe(FlowableEmitter<List<TreatmentHistory>> e) throws Exception {

                e.onNext(SQLite.select().from(TreatmentHistory.class)
                        .queryList());
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<List<CommandRecord>> getAllCommandRecord() {
        return Flowable.create(new FlowableOnSubscribe<List<CommandRecord>>() {
            @Override
            public void subscribe(FlowableEmitter<List<CommandRecord>> e) throws Exception {

                e.onNext(SQLite.select().from(CommandRecord.class)
                        .queryList());
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Boolean> insertNewCommandRecord(int type, String time, String command) {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> e) throws Exception {
                //这里数据库操作只做示例代码，主要关注rxjava的Flowable使用方法
                CommandRecord mCommandRecord = new CommandRecord();
                mCommandRecord.type = type;
                mCommandRecord.command = command;
                mCommandRecord.time = time;
                mCommandRecord.save();
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Boolean> clearCommandRecord() {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> e) throws Exception {
                //这里数据库操作只做示例代码，主要关注rxjava的Flowable使用方法
                SQLite.delete().from(CommandRecord.class).async().execute();
            }
        }, BackpressureStrategy.BUFFER);
    }

    private String getCommandLogItem() {
        if (mTimeFormat == null) {
            mTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        long mtime = System.currentTimeMillis();

        return mTimeFormat.format(new Date(mtime));
    }
}
