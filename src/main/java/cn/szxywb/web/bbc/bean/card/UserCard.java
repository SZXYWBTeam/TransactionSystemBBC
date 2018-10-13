package cn.szxywb.web.bbc.bean.card;

import cn.szxywb.web.bbc.bean.db.User;
import cn.szxywb.web.bbc.utils.Hib;
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

    // 用户信息最后更新的时间
    @Expose
    private LocalDateTime modifyAt = LocalDateTime.now();

    public UserCard(final User user) {
        this(user, false);
    }

    public UserCard(final User user, boolean isFollow) {
        this.id = user.getId();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.modifyAt = user.getUpdateAt();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LocalDateTime getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(LocalDateTime modifyAt) {
        this.modifyAt = modifyAt;
    }
}
