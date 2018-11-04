package cn.szxywb.web.bbc.bean.db;

import cn.szxywb.web.bbc.bean.api.wallet.TransactionModel;
import com.google.common.base.Strings;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 交易记录Model，对应数据库
 */
@Entity
@Table(name = "TB_TRANSACTION")
public class Transaction {

    public static final Integer TRANSACTION_STATUS_MATCH_AFTER = 0;
    public static final Integer TRANSACTION_STATUS_MATCH_PRE = -1;
    public static final Integer TRANSACTION_STATUS_OVER = 1;

    public static final Integer TRANSACTION_STATUS_PAY_PRE = 1001;
    public static final Integer TRANSACTION_STATUS_PAY_AFTER = 1002;

    public static final Integer TRANSACTION_STATUS_RECIVE_PRE = 2001; // 待收款

    public static final String DATE_FORMATE = "mmssSSS";

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    // 编号
    @Column
    private String serialNumber;

    // 交易币的数量
    @Column(nullable = false)
    private Integer currency;

    // 价值
    @Column(updatable = false, nullable = false)
    private Integer worth;

    // 状态
    @Column(nullable = false)
    private Integer status = Transaction.TRANSACTION_STATUS_MATCH_PRE;

    // 买家
//    @JoinColumn(name = "buyerId")
//    @ManyToOne
    @Transient
    private List<User> buyers;
    @Column
    private String buyerId;

    // 卖家
//    @JoinColumn(name = "sellerId")
//    @ManyToOne
    @Transient
    private List<User> sellers;
    @Column
    private String sellerId;

    @Column
    private String sellTransId;

    // 定义为创建时间戳，在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    // 定义为更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    public Transaction() {

    }

    public Transaction(List<User> seller, List<User> buyer, TransactionModel model) {
        this.buyers = buyer;
        this.sellers = seller;

        StringBuilder str = new StringBuilder();
        str.delete(0, str.length());
        if (buyers != null)
            buyers.forEach(b -> str.append(b.getId()).append(" "));
        this.buyerId = buyer == null ? null : str.toString();

        str.delete(0, str.length());
        if (sellers != null)
            sellers.forEach(s -> str.append(s.getId()).append(" "));
        this.sellerId = seller == null ? null : str.toString();

        this.worth = model.getWorth();
        this.currency = model.getCurrency();
        this.serialNumber = "DO" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMATE));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public List<User> getBuyers() {
        return buyers;
    }

    public void setBuyers(List<User> buyers) {
        this.buyers = buyers;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public List<User> getSellers() {
        return sellers;
    }

    public void setSellers(List<User> sellers) {
        this.sellers = sellers;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellTransId() {
        return sellTransId;
    }

    public void setSellTransId(String sellTransId) {
        this.sellTransId = sellTransId;
    }
}
