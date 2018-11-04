package cn.szxywb.web.bbc.bean.api.wallet;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * 卖出交易Model
 */
public class SaleModel {

    // 我的订单号
    @Expose
    private String mineSerial;

    // 买方的订单号列表
    @Expose
    private List<String> serials;

    public String getMineSerial() {
        return mineSerial;
    }

    public void setMineSerial(String mineSerial) {
        this.mineSerial = mineSerial;
    }

    public List<String> getSerials() {
        return serials;
    }

    public void setSerials(List<String> serials) {
        this.serials = serials;
    }

    // 参数校验
    public static boolean check(SaleModel model) {
        return model != null
                && !Strings.isNullOrEmpty(model.mineSerial)
                && !(model.serials == null
                || model.serials.size() == 0);
    }

    public static boolean checkSale(SaleModel model) {
        return model != null
                && !Strings.isNullOrEmpty(model.mineSerial);
    }

    public static boolean checkPay(SaleModel model) {
        return model != null
                && !Strings.isNullOrEmpty(model.mineSerial);
    }

    public static boolean checkReceipt(SaleModel model) {
        return model != null
                && !Strings.isNullOrEmpty(model.mineSerial);
    }
}
