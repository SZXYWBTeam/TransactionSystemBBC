package cn.szxywb.web.bbc.bean.card;

import cn.szxywb.web.bbc.bean.db.Money;
import cn.szxywb.web.bbc.bean.db.User;
import com.google.gson.annotations.Expose;

/**
 * 佣金提现记录Card
 */
public class MoneyCard {
    @Expose
    private String serialNumber;
    @Expose
    private String UserName;
    @Expose
    private Integer money;

    public MoneyCard(Money money, User user) {
        this.serialNumber = money.getSerialNumber();
        UserName = user.getName();
        this.money = money.getMoney();
    }
}
