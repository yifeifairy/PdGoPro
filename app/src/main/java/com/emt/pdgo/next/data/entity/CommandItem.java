package com.emt.pdgo.next.data.entity;

/**
 *  
 * @ProjectName:
 * @Package:        com.emt.pdgo.next.data.entity
 * @ClassName:      CommandItem
 * @Description:    下位机指令
 * @Author:         chenjh
 * @CreateDate:     2019/12/3 10:39 AM
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/12/3 10:39 AM
 * @UpdateRemark:   更新内容
 * @Version:        1.0
 */
public class CommandItem {

    public int type;

    public String mName;

    public String mCommand;

    public CommandItem(String mName,String mCommand){
        this.mName = mName;
        this.mCommand = mCommand;
    }

}
