package cn.szxywb.web.bbc.service;

import cn.szxywb.web.bbc.bean.api.base.ResponseModel;
import cn.szxywb.web.bbc.bean.api.wallet.SaleModel;
import cn.szxywb.web.bbc.bean.api.wallet.TransactionModel;
import cn.szxywb.web.bbc.bean.card.TransMatchCard;
import cn.szxywb.web.bbc.bean.card.TransactionCard;
import cn.szxywb.web.bbc.bean.card.WalletCard;
import cn.szxywb.web.bbc.bean.db.Transaction;
import cn.szxywb.web.bbc.bean.db.User;
import cn.szxywb.web.bbc.bean.db.Wallet;
import cn.szxywb.web.bbc.factory.TransactionFactory;
import cn.szxywb.web.bbc.factory.UserFactory;
import cn.szxywb.web.bbc.factory.WalletFactory;
import com.google.common.base.Strings;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/wallet")
public class WalletService extends BaseService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<WalletCard> mineWallet() {
        User self = getSelf();

        Wallet wallet = WalletFactory.findByUserId(self.getId());
        if (wallet == null)
            return ResponseModel.buildServiceError();

        // 添加直属人数量
        Long count = UserFactory.findInviteCountByCode(self.getInviteCode());
        wallet.setInviteCount(count);

        // 通过时间比较，更新钱包的比率
        Duration duration = Duration.between(wallet.getCreateAt(), LocalDateTime.now());
        Double plusRate = duration.toDays() * 0.01;

        //day = ChronoUnit.DAYS.between(wallet.getCreateAt(), LocalDateTime.now());

        // 今日比率未更新时才进行更新
        if (wallet.getHideRate() - Wallet.DEFAULT_HIDE_RATE != plusRate) {
            wallet.setHideRate(Wallet.DEFAULT_HIDE_RATE + plusRate);
            wallet.setUpdateAt(LocalDateTime.now());

            wallet = WalletFactory.update(wallet);
            if (wallet == null)
                return ResponseModel.buildServiceError();
        }

        return ResponseModel.buildOk(new WalletCard(wallet));
    }

    // 币卖出
    @POST
    @Path("/sale")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("Duplicates")
    public ResponseModel<TransactionCard> sale(TransactionModel model) {
        User self = getSelf();
        if (!TransactionModel.check(model))
            return ResponseModel.buildParameterError();

        // 比率验证（比率值必須小于这个用户的最大隐藏比率）
        Wallet wallet = WalletFactory.findByUserId(self.getId());
        if (wallet.getHideRate() < model.getRate())
            return ResponseModel.buildParameterError();

        // 账号剩余币数量验证
        if (wallet.getCurrency() < model.getCurrency())
            return ResponseModel.buildParameterError();

        // 账户金额做对应修改
        wallet.setCurrency(wallet.getCurrency() - model.getCurrency());
        wallet = WalletFactory.update(wallet);

        List<User> u = new ArrayList<>();
        u.add(self);
        Transaction trans = TransactionFactory.create(u, null, model);
        if (trans == null || wallet == null)
            return ResponseModel.buildServiceError();

        return ResponseModel.buildOk(new TransactionCard(trans));
    }

    // 币买入
    @POST
    @Path("/buy")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("Duplicates")
    public ResponseModel<TransactionCard> buy(TransactionModel model) {
        User self = getSelf();
        if (!TransactionModel.check(model))
            return ResponseModel.buildParameterError();

        // 比率验证（比率值必須小于这个用户的最大隐藏比率）
        Wallet wallet = WalletFactory.findByUserId(self.getId());
        if (wallet.getHideRate() < model.getRate())
            return ResponseModel.buildParameterError();

        // 账号剩余币数量验证
        if (wallet.getCurrency() < model.getCurrency())
            return ResponseModel.buildParameterError();

        // 账户金额做对应修改
        wallet.setCurrency(wallet.getCurrency() - model.getCurrency());
        wallet = WalletFactory.update(wallet);

        List<User> u = new ArrayList<>();
        u.add(self);
        Transaction trans = TransactionFactory.create(null, u, model);
        if (trans == null || wallet == null)
            return ResponseModel.buildServiceError();

        return ResponseModel.buildOk(new TransactionCard(trans));
    }

    // 查找匹配项
    @POST
    @Path("/match/{index:[0-9]*}/{pageSize:[0-9]*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("Duplicates")
    public ResponseModel<TransMatchCard> match(TransactionModel model,
                                               @PathParam("index") int index,
                                               @PathParam("pageSize") int pageSize) {
        User self = getSelf();
        if (!TransactionModel.check(model))
            return ResponseModel.buildParameterError();

        // 进行数据匹配（不能匹配买家为自己）
        List<TransactionCard> recommendTransCards = new ArrayList<>();
        List<Transaction> transList = TransactionFactory.findBelowWorth(model, self, model.isSale());
        // 返回空数据
        if (transList == null || transList.size() == 0)
            return ResponseModel.buildOk(null);


        List<User> buyers = new ArrayList<>();
        List<User> sellers = new ArrayList<>();
        // 将Transaction转换为TransactionCard
        // 填充User对象
        List<TransactionCard> transCards = transList.stream()
                .map(buyer -> {
                    if (!Strings.isNullOrEmpty(buyer.getBuyerId())) {
                        buyers.clear();
                        for (String id : buyer.getBuyerId().split(" ")) {
                            buyers.add(UserFactory.findById(id));
                        }
                        buyer.setBuyers(buyers);
                    }
                    if (!Strings.isNullOrEmpty(buyer.getSellerId())) {
                        sellers.clear();
                        for (String id : buyer.getSellerId().split(" ")) {
                            sellers.add(UserFactory.findById(id));
                        }
                        buyer.setSellers(sellers);
                    }
                    return new TransactionCard(buyer);
                })
                .collect(Collectors.toList());

        // 双指针依次往后查找匹配项
        int worthSum = 0;
        for (int i = 0; i < transCards.size(); i++) {
            // 初始化
            worthSum = 0;
            recommendTransCards.clear();
            for (int j = i; j < transCards.size(); j++) {
                int worth = transCards.get(j).getWorth();
                if (worthSum + worth > model.getWorth())
                    continue;

                worthSum += worth;
                recommendTransCards.add(transCards.get(j));
                // 找到匹配项
                if (worthSum == model.getWorth())
                    break;
            }
            // 找到匹配项
            if (worthSum == model.getWorth())
                break;
        }

        if (worthSum != model.getWorth())
            recommendTransCards.clear();

        // 分页返回数据
        int fromIndex = index * pageSize;
        int toIndex = pageSize + (index * pageSize) ;
        return ResponseModel.buildOk(
                new TransMatchCard(
                        transCards.subList(fromIndex,
                                toIndex > transCards.size()
                                        ? (transCards.size())
                                        : toIndex),
                        recommendTransCards
                )
        );
    }

    // 佣金提出
}
