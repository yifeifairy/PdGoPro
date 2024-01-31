package com.emt.pdgo.next.data.chart;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.data.chart
 * @ClassName: IncomeBean
 * @Description: java类作用描述
 * @Author: chenjh
 * @CreateDate: 2019/12/18 4:31 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/18 4:31 PM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class IncomeBean {


    private String tradeDate;
    private int value;

    public IncomeBean(String tradeDate,int value){
        this.tradeDate = tradeDate;
        this.value = value;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
