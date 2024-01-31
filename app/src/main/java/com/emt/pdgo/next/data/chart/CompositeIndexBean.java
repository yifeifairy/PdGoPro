package com.emt.pdgo.next.data.chart;

/**
 * @ProjectName:
 * @Package: com.emt.pdgo.next.data.chart
 * @ClassName: CompositeIndexBean
 * @Description: java类作用描述
 * @Author: chenjh
 * @CreateDate: 2019/12/18 4:31 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/18 4:31 PM
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */

public class CompositeIndexBean {
    private double rate;
    private String tradeDate;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }
}
