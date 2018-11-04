package cn.szxywb.web.bbc.factory;

import cn.szxywb.web.bbc.bean.api.wallet.TransactionModel;
import cn.szxywb.web.bbc.bean.card.TransactionCard;
import cn.szxywb.web.bbc.bean.db.Transaction;
import cn.szxywb.web.bbc.bean.db.User;
import cn.szxywb.web.bbc.utils.Hib;

import java.util.List;

public class TransactionFactory {

    public static Transaction update(Transaction trans) {
        return Hib.query(session -> {
            session.saveOrUpdate(trans);
            return trans;
        });
    }

    public static List<Transaction> updateList(List<Transaction> trans) {
        return Hib.query(session -> {
            trans.forEach(session::saveOrUpdate);
            return trans;
        });
    }

    // 创建消息记录
    public static Transaction create(List<User> self, List<User> buyer, TransactionModel model) {
        return Hib.query(session -> {
            Transaction trans = new Transaction(self, buyer, model);
            session.save(trans);

            return trans;
        });
    }

    // 查找低于传入价格的交易列表
    @SuppressWarnings("unchecked")
    public static List<Transaction> findBelowWorth(TransactionModel model, User user, boolean isSale) {
        String hql;
        // 如果需要进行购买
        // 买家不能是自己 && 还没有匹配成功的订单
        if (isSale)
            hql = "from Transaction " +
                    " where worth<=:worth and buyerId!=:id and (sellerId=NULL OR sellerId='') " +
                    " order by currency desc";
        else
            hql = "from Transaction " +
                    " where worth<=:worth and sellerId!=:id and (buyerId=NULL OR buyerId='') " +
                    " order by currency desc";

        return Hib.query(session -> (List<Transaction>) session.createQuery(hql)
                .setParameter("worth", model.getWorth())
                .setParameter("id", user.getId())
                .list());
    }

    // 查找低于传入价格的交易列表（分页）
    @SuppressWarnings("unchecked")
    public static List<Transaction> findBelowWorthLimit(TransactionModel model,
                                                        User user, boolean isSale, int index, int pageSize) {
        String hql;
        // 如果需要进行购买
        // 买家不能是自己 && 还没有匹配成功的订单
        if (isSale)
            hql = "from Transaction " +
                    " where worth<=:worth and buyerId!=:id and (sellerId=NULL OR sellerId='') " +
                    " order by currency desc";
        else
            hql = "from Transaction " +
                    " where worth<=:worth and sellerId!=:id and (buyerId=NULL OR buyerId='') " +
                    " order by currency desc";

        return Hib.query(session -> (List<Transaction>) session.createQuery(hql)
                .setFirstResult(index * pageSize)
                .setMaxResults(pageSize)
                .setParameter("worth", model.getWorth())
                .setParameter("id", user.getId())
                .list());
    }

    public static Transaction findBySerial(String serial) {
        return Hib.query(session -> (Transaction) session
                .createQuery("from Transaction where serialNumber=:serial")
                .setParameter("serial", serial)
                .uniqueResult());
    }

    @SuppressWarnings("unchecked")
    public static List<Transaction> list(User self, int index, int pageSize) {
        return Hib.query(session -> session.createQuery("from Transaction" +
                " where buyerId like :id or sellerId like :id" +
                " order by updateAt desc")
                .setFirstResult(index * pageSize)
                .setMaxResults(pageSize)
                .setParameter("id", "%" + self.getId() + "%")
                .list());
    }

    // 保存双方用户交易的数据
    public static Transaction updateSaleInfo(Transaction mineTrans, List<Transaction> transList) {
        return Hib.query(session -> {
            session.update(mineTrans);
            transList.forEach(TransactionFactory::update);

            return mineTrans;
        });
    }

    @SuppressWarnings("unchecked")
    public static List<Transaction> findBySellTransId(String id) {
        return Hib.query(session -> (List<Transaction>) session
                .createQuery("from Transaction where sellTransId=:id")
                .setParameter("id", id)
                .list());
    }

    public static Transaction findById(String id) {
        return Hib.query(session -> (Transaction) session
                .createQuery("from Transaction where id=:id")
                .setParameter("id", id)
                .uniqueResult());
    }
}
