package cn.szxywb.web.bbc.bean.card;

import cn.szxywb.web.bbc.bean.db.Wallet;
import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;

public class WalletCard {

    @Expose
    private String id;
    // 记录用户钱包的持币数量
    @Expose
    private Integer currency;
    // 比率
    @Expose
    private Double rate;
    // 隐藏比率，从创建时开始，每天增加1%
    @Expose
    private Double hideRate;
    // 此用户邀请其他用户的数量
    @Expose
    private Long inviteCount;

    // 钱包信息最后更新的时间
    @Expose
    private LocalDateTime modifyAt;

    public WalletCard(final Wallet wallet) {
        this.id = wallet.getId();
        this.currency = wallet.getCurrency();
        this.rate = wallet.getRate();
        this.hideRate = wallet.getHideRate();
        this.modifyAt = wallet.getUpdateAt();
        this.inviteCount = wallet.getInviteCount();
    }

}
