package cn.szxywb.web.bbc.bean.api.account;

import cn.szxywb.web.bbc.bean.card.UserCard;
import cn.szxywb.web.bbc.bean.db.User;
import com.google.gson.annotations.Expose;

public class AccountRspModel {
    // 用户基本信息
    @Expose
    private UserCard user;
    // 当前登录的账号
    @Expose
    private String account;
    // 当前登录成功后获取的Token
    // 可以通过Token获取用户的所有信息
    @Expose
    private String token;

    public AccountRspModel(User user) {
        this.account = user.getPhone();
        this.token = user.getToken();
        this.user = new UserCard(user);
    }

    public UserCard getUser() {
        return user;
    }

    public void setUser(UserCard user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
