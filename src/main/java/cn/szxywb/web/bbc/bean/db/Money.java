package cn.szxywb.web.bbc.bean.db;

import cn.szxywb.web.bbc.bean.api.wallet.WithdrawModel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "TB_MONEY_HISTORY")
public class Money {

    public static final Integer TRANSACTION_STATUS_OVER = 1;
    public static final Integer TRANSACTION_STATUS_PRE = -1;

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    // 编号
    @Column
    private String serialNumber;

    // 提现金额
    @Column(updatable = false, nullable = false)
    private Integer money;

    // 状态
    @Column(nullable = false)
    private Integer status = TRANSACTION_STATUS_OVER;

    @Transient
    private User user;
    @Column
    private String userId;

    // 定义为创建时间戳，在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    // 定义为更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    public Money() {
    }

    public Money(WithdrawModel model, User user) {
        Double money = model.getCurrency() * model.getRate() * model.getRate();
        //this.id = UUID.randomUUID().toString();
        this.money = money.intValue();
        this.user = user;
        this.userId = user.getId();
        this.serialNumber = "DO" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(Transaction.DATE_FORMATE));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
