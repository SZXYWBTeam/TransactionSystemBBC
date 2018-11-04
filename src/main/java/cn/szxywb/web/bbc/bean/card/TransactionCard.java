package cn.szxywb.web.bbc.bean.card;

import cn.szxywb.web.bbc.bean.db.Transaction;
import cn.szxywb.web.bbc.bean.db.User;
import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 返回订单信息的Model
 */
public class TransactionCard {

    // 编号
    @Expose
    private String serialNumber;

    // 币数量
    @Expose
    private Integer currency;

    // 价值
    @Expose
    private Integer worth;

    // 状态
    @Expose
    private Integer status;

    // 买家
    @Expose
    private List<UserCard> buyers;

    // 卖家
    @Expose
    private List<UserCard> sellers;

    @Expose
    private LocalDateTime updateAt;

    public TransactionCard(Transaction trans) {
        this.serialNumber = trans.getSerialNumber();
        this.currency = trans.getCurrency();
        this.worth = trans.getWorth();
        this.status = trans.getStatus();
        this.updateAt = trans.getUpdateAt();
        this.buyers = (trans.getBuyers() == null ? null
                : trans.getBuyers().stream().map(UserCard::new)
                .collect(Collectors.toList()));
        this.sellers = (trans.getSellers() == null ? null
                : trans.getSellers().stream().map(UserCard::new)
                .collect(Collectors.toList()));
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public Integer getWorth() {
        return worth;
    }

    public void setWorth(Integer worth) {
        this.worth = worth;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<UserCard> getBuyers() {
        return buyers;
    }

    public void setBuyers(List<UserCard> buyers) {
        this.buyers = buyers;
    }

    public List<UserCard> getSellers() {
        return sellers;
    }

    public void setSellers(List<UserCard> sellers) {
        this.sellers = sellers;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
