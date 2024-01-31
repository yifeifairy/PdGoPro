package com.emt.pdgo.next.util;

import com.emt.pdgo.next.common.PdproHelper;

import java.util.LinkedList;

/**
 * 指令队列: 先进先出
 */
public class CmdQueueHelper {
    private static CmdQueueHelper instance;
//
//    private PdproHelper(Context context) {
//        this.context = context.getApplicationContext();
//    }

    private CmdQueueHelper() {

    }

    public static CmdQueueHelper getInstance() {
        if (instance == null) {
            instance = new CmdQueueHelper();
        }
        return instance;
    }


    private LinkedList list = new LinkedList();

    public void put(Object t){ //加入数据
        list.addFirst(t);
    }
    public Object get(){ //取出先加入的数据
        return  list.removeLast();
    }
    public boolean isEmpt(){
        return  list.isEmpty();
    }

    public int getSize(){
        return  list.size();
    }

    public void clear(){
        list.clear();
    }

}
