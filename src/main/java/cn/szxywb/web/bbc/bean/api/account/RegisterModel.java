package cn.szxywb.web.bbc.bean.api.account;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

public class RegisterModel {
    @Expose
    private String account;
    @Expose
    private String password;
    @Expose
    private String phone;
    @Expose
    private String name;
    @Expose
    private String zfbCode;
    @Expose
    private String otherInviteCode; // 其他人的邀请码
    @Expose
    private String verCode; // 验证码

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZfbCode() {
        return zfbCode;
    }

    public void setZfbCode(String zfbCode) {
        this.zfbCode = zfbCode;
    }

    public String getOtherInviteCode() {
        return otherInviteCode;
    }

    public void setOtherInviteCode(String otherInviteCode) {
        this.otherInviteCode = otherInviteCode;
    }

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }

    // 参数校验
    public static boolean check(RegisterModel model) {
        return model!=null
                && !Strings.isNullOrEmpty(model.account)
                && !Strings.isNullOrEmpty(model.password)
                && !Strings.isNullOrEmpty(model.phone)
                && !Strings.isNullOrEmpty(model.name)
                && !Strings.isNullOrEmpty(model.zfbCode)
                && !Strings.isNullOrEmpty(model.verCode);
    }
}
