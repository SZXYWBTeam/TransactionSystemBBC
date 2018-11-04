package cn.szxywb.web.bbc.bean.api.account;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

public class UpdateModel {
    @Expose
    private String phone;
    @Expose
    private String name;
    @Expose
    private String zfbCode;
    @Expose
    private String oldPwd;
    @Expose
    private String newPwd;

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

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    // 参数校验 -- 密码修改
    public static boolean checkPwd(UpdateModel model) {
        return model != null
                && !Strings.isNullOrEmpty(model.newPwd)
                && !Strings.isNullOrEmpty(model.oldPwd);
    }

    // 参数校验 -- 资料修改
    public static boolean checkInfo(UpdateModel model) {
        return model != null
                && !Strings.isNullOrEmpty(model.phone)
                && !Strings.isNullOrEmpty(model.name)
                && !Strings.isNullOrEmpty(model.zfbCode);
    }
}
