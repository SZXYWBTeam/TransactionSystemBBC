package cn.szxywb.web.bbc.factory;

import cn.szxywb.web.bbc.bean.db.Money;
import cn.szxywb.web.bbc.bean.db.User;
import cn.szxywb.web.bbc.bean.db.Wallet;
import cn.szxywb.web.bbc.utils.Hib;

import java.util.List;

public class MoneyFactory {

    public static Money update(Money money) {
        return Hib.query(session -> {
            session.saveOrUpdate(money);
            return money;
        });
    }

    public static boolean withdraw(Money money, Wallet wallet) {
        return Hib.query(session -> {
           session.saveOrUpdate(money);
           session.saveOrUpdate(wallet);

           return true;
        });
    }

    // （分页）
    @SuppressWarnings("unchecked")
    public static List<Money> list(User user, int index, int pageSize) {
        String hql = "from Money " +
                " where userId=:id " +
                " order by updateAt desc";
        // 如果需要进行购买
        // 买家不能是自己 && 还没有匹配成功的订单
        return Hib.query(session -> (List<Money>) session.createQuery(hql)
                .setFirstResult(index * pageSize)
                .setMaxResults(pageSize)
                .setParameter("id", user.getId())
                .list());
    }
}
