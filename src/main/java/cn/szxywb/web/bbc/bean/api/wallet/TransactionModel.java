package cn.szxywb.web.bbc.bean.api.wallet;

import com.google.gson.annotations.Expose;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * 交易Model
 */
public class TransactionModel {
    // 币的数量（卖出、买入）
    @Expose
    private Integer currency;

    // 用户币的比率（需要进行数据验证）
    @Expose
    private Double rate;

    // 币对应的价值（= currency * rate）
    @Expose
    private Integer worth;

    @Expose
    private Boolean isSale;

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getWorth() {
        return (int) (currency * rate);
    }

    public void setWorth(Integer worth) {
        this.worth = worth;
    }

    public Boolean isSale() {
        return isSale;
    }

    public void setSale(Boolean sale) {
        isSale = sale;
    }

    // 参数校验
    public static boolean check(TransactionModel model) {
        return model != null
                && model.currency != 0
                && model.rate != 0
                && model.worth != 0
                && (int) (model.currency * model.rate) == model.worth
                && model.isSale != null;
    }
}
