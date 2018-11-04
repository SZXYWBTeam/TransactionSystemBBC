package cn.szxywb.web.bbc.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 钱包的Model，对应数据库
 */
@Entity
@Table(name = "TB_WALLET")
public class Wallet {

    public static final Double DEFAULT_RATE = 0.00;
    public static final Double DEFAULT_HIDE_RATE = 1.00;
    public static final Integer INITIAL_CURRENCY = 100;
    public static final Long INITIAL_INVITE_COUNT = 0L;

    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(updatable = false, nullable = false, unique = true)
    private String userId;

    // 记录用户钱包的持币数量
    @Column
    private Integer currency = Wallet.INITIAL_CURRENCY;

    // 比率
    @Column(nullable = false)
    private Double rate = Wallet.DEFAULT_RATE;

    // 隐藏比率，从创建时开始，每天增加1%
    @Column(nullable = false)
    private Double hideRate = Wallet.DEFAULT_HIDE_RATE;

    // 定义为创建时间戳，在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    // 定义为更新时间戳，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    // 此用户邀请其他用户的数量
    @Transient
    private Long inviteCount = Wallet.INITIAL_INVITE_COUNT;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public Double getHideRate() {
        return hideRate;
    }

    public void setHideRate(Double hideRate) {
        this.hideRate = hideRate;
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

    public Long getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(Long inviteCount) {
        this.inviteCount = inviteCount;
    }
}
