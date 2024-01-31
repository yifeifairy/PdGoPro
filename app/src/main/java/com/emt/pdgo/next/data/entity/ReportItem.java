package com.emt.pdgo.next.data.entity;

/**
 *
 * @ProjectName:
 * @Package:        com.emt.pdgo.next.data.entity
 * @ClassName:      ReportItem
 * @Description:    自检模块
 * @Author:         chenjh
 * @CreateDate:     2019/11/14 4:03 PM
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/11/14 4:03 PM
 * @UpdateRemark:   更新内容
 * @Version:        1.0
 */
public class ReportItem {

    public String module;

    public boolean isNormal;

    public ReportItem(String module,boolean isNormal){
        this.module = module;
        this.isNormal = isNormal;
    }

}
