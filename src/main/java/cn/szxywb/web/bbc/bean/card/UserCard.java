package cn.szxywb.web.bbc.bean.card;

import cn.szxywb.web.bbc.bean.db.User;
import cn.szxywb.web.bbc.factory.UserFactory;
import cn.szxywb.web.bbc.utils.Hib;
import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;

public class UserCard {

    @Expose
    private String id;
    // 用户名
    @Expose
    private String name;
    // 手机号
    @Expose
    private String phone;

    @Expose
    private String zfbCode;

    @Expose
    private String inviteCode;

    @Expose
    private String account;

    // 邀请人的用户Id
    @Expose
    private String inviterAccount;

    // 用户信息最后更新的时间
    @Expose
    private LocalDateTime modifyAt = LocalDateTime.now();

    public UserCard(final User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.modifyAt = user.getUpdateAt();
        this.zfbCode = user.getZfbCode();
        this.account = user.getAccount();
        this.inviteCode = user.getInviteCode();
        this.inviterAccount = Strings.isNullOrEmpty(user.getOtherInviteCode()) ? null
                : UserFactory.findByInviteCode(user.getOtherInviteCode()).getAccount();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getInviterAccount() {
        return inviterAccount;
    }

    public void setInviterAccount(String inviterAccount) {
        this.inviterAccount = inviterAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZfbCode() {
        return zfbCode;
    }

    public void setZfbCode(String zfbCode) {
        this.zfbCode = zfbCode;
    }

    public LocalDateTime getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(LocalDateTime modifyAt) {
        this.modifyAt = modifyAt;
    }
}
