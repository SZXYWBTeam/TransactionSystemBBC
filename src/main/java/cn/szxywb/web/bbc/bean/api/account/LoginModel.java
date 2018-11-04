package cn.szxywb.web.bbc.bean.api.account;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

public class LoginModel {
    @Expose
    private String account;
    @Expose
    private String password;
    @Expose
    private String verCode; // 验证码
    @Expose
    private String verCodeKey;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }

    public String getVerCodeKey() {
        return verCodeKey;
    }

    public void setVerCodeKey(String verCodeKey) {
        this.verCodeKey = verCodeKey;
    }

    // 参数校验
    public static boolean check(LoginModel model) {
        return model!=null
                && !Strings.isNullOrEmpty(model.account)
                && !Strings.isNullOrEmpty(model.password)
                && !Strings.isNullOrEmpty(model.verCode)
                && !Strings.isNullOrEmpty(model.verCodeKey);
    }
}
