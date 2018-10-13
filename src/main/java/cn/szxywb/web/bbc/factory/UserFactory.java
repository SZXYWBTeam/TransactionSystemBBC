package cn.szxywb.web.bbc.factory;

import cn.szxywb.web.bbc.bean.db.User;
import cn.szxywb.web.bbc.utils.Hib;
import cn.szxywb.web.bbc.utils.TextUtil;

import java.util.UUID;

public class UserFactory {

    public static User login(String account, String password) {
        String accountStr = account.trim();
        String passwordStr = encodePassword(password);

        User user = Hib.query(session -> (User) session
                .createQuery("from User where account=:account and password=:password")
                .setParameter("account", accountStr)
                .setParameter("password", passwordStr)
                .uniqueResult());

        if (user != null) {
            // 如果登录成功
            // 更新Token
            user = login(user);
        }

        return user;
    }

    /**
     * 把一个User进行登录的操作
     *
     * @param user user
     * @return User
     */
    private static User login(User user) {
        // 使用一个随机的UUID值充当Token
        String newToken = UUID.randomUUID().toString();
        // 进行一次Base64格式化
        newToken = TextUtil.encodeBase64(newToken);
        user.setToken(newToken);

        return update(user);
    }

    /**
     * 更新用户信息到数据库
     *
     * @param user user
     * @return User
     */
    public static User update(User user) {
        return Hib.query(session -> {
            session.saveOrUpdate(user);
            return user;
        });
    }

    private static String encodePassword(String passWord) {
        passWord = passWord.trim();

        passWord = TextUtil.getMD5(passWord);
        return TextUtil.encodeBase64(passWord);
    }
}
