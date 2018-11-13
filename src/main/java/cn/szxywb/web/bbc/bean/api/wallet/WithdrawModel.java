package cn.szxywb.web.bbc.bean.api.wallet;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

public class WithdrawModel {

    @Expose
    private Integer currency;
    @Expose
    private Double rate;
    @Expose
    private String zfbCode;
    @Expose
    private String phone;

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

    public String getZfbCode() {
        return zfbCode;
    }

    public void setZfbCode(String zfbCode) {
        this.zfbCode = zfbCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static boolean check(WithdrawModel model) {
        return model != null
                && model.rate > 0
                && model.currency != 0
                && !Strings.isNullOrEmpty(model.zfbCode)
                && !Strings.isNullOrEmpty(model.phone);
    }
}
