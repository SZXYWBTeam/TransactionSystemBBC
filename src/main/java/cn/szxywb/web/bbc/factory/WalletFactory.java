package cn.szxywb.web.bbc.factory;

import cn.szxywb.web.bbc.bean.db.Wallet;
import cn.szxywb.web.bbc.utils.Hib;

public class WalletFactory {

    // 通过userID查询钱包信息
    public static Wallet findByUserId(String uId) {
        return Hib.query(session -> (Wallet) session
                .createQuery("from Wallet where userId=:uId")
                .setParameter("uId", uId)
                .uniqueResult());
    }

    // 更新信息到数据库
    public static Wallet update(Wallet wallet) {
        return Hib.query(session -> {
            session.saveOrUpdate(wallet);
            return wallet;
        });
    }
}
