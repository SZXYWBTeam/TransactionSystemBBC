package cn.szxywb.web.bbc.factory;

import cn.szxywb.web.bbc.bean.db.User;
import cn.szxywb.web.bbc.utils.Hib;
import cn.szxywb.web.bbc.utils.TextUtil;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserFactory {

    // 通过Token字段查询用户信息
    public static User findByToken(String token) {
        return Hib.query(session -> (User) session
                .createQuery("from User where token=:token")
                .setParameter("token", token)
                .uniqueResult());
    }

    // 通过Account字段查询用户信息
    public static User findByAccount(String account) {
        return Hib.query(session -> (User) session
                .createQuery("from User where account=:account")
                .setParameter(account, account)
                .uniqueResult());
    }

    // 通过Phone查找User
    public static User findByPhone(String phone) {
        return Hib.query(session -> (User) session
                .createQuery("from User where phone=:inPhone")
                .setParameter("inPhone", phone)
                .uniqueResult());
    }

    // 通过Name查找User
    public static User findByName(String name) {
        return Hib.query(session -> (User) session
                .createQuery("from User where name=:inName")
                .setParameter("inName", name)
                .uniqueResult());
    }

    // 通过InviteCode查找User
    public static User findByInviteCode(String inviteCode) {
        return Hib.query(session -> (User) session
                .createQuery("from User where inviteCode=:inviteCode")
                .setParameter("inviteCode", inviteCode)
                .uniqueResult());
    }

    // 通过ZfbCode查找User
    public static User findByZfbCode(String zfbCode) {
        return Hib.query(session -> (User) session
                .createQuery("from User where zfbCode=:zfbCode")
                .setParameter("zfbCode", zfbCode)
                .uniqueResult());
    }

    // 通过Id查找User
    public static User findById(String id) {
        return Hib.query(session -> session.get(User.class, id));
    }

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
     * 用户注册
     *
     * @param account  账户（手机号）
     * @param password 密码
     * @param name     用户名
     * @return User
     */
    public static User register(String account, String password, String phone, String name, String zfbCode) {
        // 去除账户中的首位空格
        account = account.trim();
        // 处理密码
        password = encodePassword(password);

        User user = createUser(account, password, phone, name, zfbCode, encodeInviteCode(LocalDateTime.now().toString()));
        if (user != null) {
            user = login(user);
        }

        return user;
    }

    /**
     * 注册部分的新建用户逻辑
     *
     * @param account  account
     * @param password password
     * @param name     name
     * @return User
     */
    private static User createUser(String account, String password, String phone, String name, String zfbCode, String inviteCode) {
        User user = new User();

        user.setAccount(account);
        user.setPassword(password);
        user.setPhone(phone);
        user.setName(name);
        user.setZfbCode(zfbCode);
        user.setInviteCode(inviteCode);

        return Hib.query(session -> {
            session.save(user);
            return user;
        });
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

    private static String encodeInviteCode(String code) {
        code = code.trim();

        code = TextUtil.getMD5(code);
        return TextUtil.encodeBase64(code);
    }
}
