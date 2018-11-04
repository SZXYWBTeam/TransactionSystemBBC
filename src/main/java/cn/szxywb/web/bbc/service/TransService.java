package cn.szxywb.web.bbc.service;

import cn.szxywb.web.bbc.bean.api.base.ResponseModel;
import cn.szxywb.web.bbc.bean.api.wallet.SaleModel;
import cn.szxywb.web.bbc.bean.card.TransMatchCard;
import cn.szxywb.web.bbc.bean.card.TransactionCard;
import cn.szxywb.web.bbc.bean.card.UserCard;
import cn.szxywb.web.bbc.bean.db.Transaction;
import cn.szxywb.web.bbc.bean.db.User;
import cn.szxywb.web.bbc.bean.db.Wallet;
import cn.szxywb.web.bbc.factory.TransactionFactory;
import cn.szxywb.web.bbc.factory.UserFactory;
import cn.szxywb.web.bbc.factory.WalletFactory;
import com.google.common.base.Strings;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Path("/trans")
public class TransService extends BaseService {

    // 订单 交易记录List（分页）
    @POST
    @Path("{index:[0-9]*}/{pageSize:[0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("Duplicates")
    public ResponseModel<List<TransactionCard>> list(@PathParam("index") int index,
                                                     @PathParam("pageSize") int pageSize) {
        User self = getSelf();

        List<User> sellers = new ArrayList<>();
        List<User> buyers = new ArrayList<>();
        return ResponseModel.buildOk(TransactionFactory.list(self, index, pageSize)
                .stream()
                .map(trans -> {
                    if (!Strings.isNullOrEmpty(trans.getBuyerId())) {
                        buyers.clear();
                        for (String id : trans.getBuyerId().split(" ")) {
                            buyers.add(UserFactory.findById(id));
                        }
                        trans.setBuyers(buyers);
                    }
                    if (!Strings.isNullOrEmpty(trans.getSellerId())) {
                        sellers.clear();
                        for (String id : trans.getSellerId().split(" ")) {
                            sellers.add(UserFactory.findById(id));
                        }
                        trans.setSellers(sellers);
                    }
                    return new TransactionCard(trans);
                })
                .collect(Collectors.toList()));
    }

    // 卖出
    @POST
    @Path("/doSale")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<TransactionCard> doSale(SaleModel model) {
        User self = getSelf();
        if (!SaleModel.check(model))
            return ResponseModel.buildParameterError();

        // 检查订单数据
        Transaction mineTrans = TransactionFactory.findBySerial(model.getMineSerial());
        List<Transaction> transList = model.getSerials()
                .stream()
                .map(TransactionFactory::findBySerial)
                .collect(Collectors.toList());
        // 服务器查询到的信息检查
        if (mineTrans == null || (transList == null || transList.size() == 0))
            return ResponseModel.buildParameterError();

        AtomicInteger count = new AtomicInteger();
        transList.forEach(trans -> count.addAndGet(trans.getWorth()));
        if (mineTrans.getWorth() != count.get())
            return ResponseModel.buildParameterError();

        transList.forEach(trans -> trans.setSellTransId(mineTrans.getId()));
        transList.add(mineTrans);
        // 修改双方订单状态
        transList.forEach(trans -> trans.setStatus(Transaction.TRANSACTION_STATUS_MATCH_AFTER));
        transList = TransactionFactory.updateList(transList);
        if (transList == null)
            return ResponseModel.buildServiceError();

        List<User> sellers = new ArrayList<>();
        sellers.add(UserFactory.findById(mineTrans.getSellerId()));
        mineTrans.setSellers(sellers);

        List<Transaction> transBuy = TransactionFactory.findBySellTransId(mineTrans.getId());
        List<User> buyers = transBuy.stream()
                .map(trans -> {
                    trans.setStatus(Transaction.TRANSACTION_STATUS_MATCH_AFTER);
                    return UserFactory.findById(trans.getBuyerId());
                })
                .collect(Collectors.toList());
        mineTrans.setBuyers(buyers);
        return ResponseModel.buildOk(new TransactionCard(mineTrans));
    }

    // 进行订单匹配（只有卖家可以进入订单匹配界面）
    @POST
    @Path("/doMatch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<TransactionCard> doMatch(SaleModel model) {
        User self = getSelf();
        if (!SaleModel.check(model))
            return ResponseModel.buildParameterError();

        // 检查订单数据
        Transaction mineTrans = TransactionFactory.findBySerial(model.getMineSerial());
        List<Transaction> transList = model.getSerials()
                .stream()
                .map(TransactionFactory::findBySerial)
                .collect(Collectors.toList());
        // 服务器查询到的信息检查
        if (mineTrans == null || (transList == null || transList.size() == 0))
            return ResponseModel.buildParameterError();

        // 双方价格是否匹配
        AtomicInteger count = new AtomicInteger();
        transList.forEach(trans -> count.addAndGet(trans.getWorth()));
        if (mineTrans.getWorth() != count.get())
            return ResponseModel.buildParameterError();

        transList.forEach(trans -> trans.setSellTransId(mineTrans.getId()));
        transList.add(mineTrans);
        // 修改双方订单状态为已匹配
        transList.forEach(trans -> trans.setStatus(Transaction.TRANSACTION_STATUS_MATCH_AFTER));
        transList = TransactionFactory.updateList(transList);
        if (transList == null)
            return ResponseModel.buildServiceError();

        return ResponseModel.buildOk();
    }


    // 打款
    @POST
    @Path("/doPay")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<TransactionCard> doPay(SaleModel model) {
        User self = getSelf();
        if (!SaleModel.checkPay(model))
            return ResponseModel.buildParameterError();

        Transaction mineTrans = TransactionFactory.findBySerial(model.getMineSerial());
        // 服务器查询到的信息检查
        if (mineTrans == null)
            return ResponseModel.buildParameterError();

        // TODO 参数匹配及支付宝接口调用

        mineTrans.setStatus(Transaction.TRANSACTION_STATUS_PAY_AFTER);
        mineTrans = TransactionFactory.update(mineTrans);
        if (mineTrans == null)
            return ResponseModel.buildServiceError();

        // 修改双方交易人的账户金额
        // 买家打款
        Wallet buyerWallet = WalletFactory.findByUserId(mineTrans.getBuyerId());
        buyerWallet.setCurrency(buyerWallet.getCurrency() - mineTrans.getCurrency());
        // 卖家增加金额
        Transaction sellerTrans = TransactionFactory.findById(mineTrans.getSellTransId());
        Wallet sellerWallet = WalletFactory.findByUserId(sellerTrans.getSellerId());
        sellerWallet.setCurrency(sellerWallet.getCurrency() + mineTrans.getCurrency());

        // 保存数据
        WalletFactory.update(buyerWallet);
        WalletFactory.update(sellerWallet);

        return ResponseModel.buildOk();
    }

    // 收款
    @POST
    @Path("/doReceipt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<TransactionCard> doReceipt(SaleModel model) {
        User self = getSelf();
        if (!SaleModel.checkReceipt(model))
            return ResponseModel.buildParameterError();

        Transaction mineTrans = TransactionFactory.findBySerial(model.getMineSerial());
        // 服务器查询到的信息检查
        if (mineTrans == null)
            return ResponseModel.buildParameterError();

        // TODO 参数匹配及支付宝接口调用

        mineTrans.setStatus(Transaction.TRANSACTION_STATUS_OVER);
        mineTrans = TransactionFactory.update(mineTrans);
        if (mineTrans == null)
            return ResponseModel.buildServiceError();

        List<Transaction> buyTrans = TransactionFactory.findBySellTransId(mineTrans.getId());
        if (buyTrans == null)
            return ResponseModel.buildParameterError();

        buyTrans.forEach(trans -> {
            trans.setStatus(Transaction.TRANSACTION_STATUS_OVER);
            TransactionFactory.update(trans);
        });

        return ResponseModel.buildOk();
    }

    // 显示订单详情
    @POST
    @Path("/info/{serialNumber}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<TransactionCard> getInfo(@PathParam("serialNumber") String serialNumber) {
        User self = getSelf();
        //拿到订单编号
        if (Strings.isNullOrEmpty(serialNumber))
            return ResponseModel.buildParameterError();

        //根据订单编号去数据库查询出model
        Transaction trans = TransactionFactory.findBySerial(serialNumber);
        if(trans == null)
            return ResponseModel.buildParameterError();

        List<User> buyders = new ArrayList<>();
        List<User> sellers = new ArrayList<>();
        //判断model.sellTransId 为空表示是卖的订单  不为空表示买家订单
        if (Strings.isNullOrEmpty(trans.getSellTransId())) {
            //卖家订单 根据sellTransId 查询出所有的买家订单 一个卖家订单 多个买家订单
            List<Transaction> bySellTrans = TransactionFactory.findBySellTransId(trans.getId());
            trans.setBuyers(bySellTrans.stream()
                    .map(transaction -> UserFactory.findById(transaction.getBuyerId()))
                    .collect(Collectors.toList()));
            sellers.add(self);
            trans.setSellers(sellers);
        } else {
            //买家订单 根据sellTransId  查询出卖家订单 一个买家订单 一个卖家订单
            Transaction sellTrans = TransactionFactory.findById(trans.getSellTransId());
            buyders.add(self);
            sellers.add(UserFactory.findById(sellTrans.getSellerId()));
            trans.setBuyers(buyders);
            trans.setSellers(sellers);
        }

        // return
        return ResponseModel.buildOk(new TransactionCard(trans));
    }

}
