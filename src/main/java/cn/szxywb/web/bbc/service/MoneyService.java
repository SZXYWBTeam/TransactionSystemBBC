package cn.szxywb.web.bbc.service;

import cn.szxywb.web.bbc.bean.api.base.ResponseModel;
import cn.szxywb.web.bbc.bean.api.wallet.WithdrawModel;
import cn.szxywb.web.bbc.bean.card.MoneyCard;
import cn.szxywb.web.bbc.bean.db.Money;
import cn.szxywb.web.bbc.bean.db.User;
import cn.szxywb.web.bbc.bean.db.Wallet;
import cn.szxywb.web.bbc.factory.MoneyFactory;
import cn.szxywb.web.bbc.factory.WalletFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/money")
public class MoneyService extends BaseService {

    // 佣金提出（提现）
    @Path("/withdraw")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("ConstantConditions")
    public ResponseModel withDraw(WithdrawModel model) {
        if (!WithdrawModel.check(model))
            return ResponseModel.buildParameterError();

        User self = getSelf();
        Wallet wallet = WalletFactory.findByUserId(self.getId());
        // 传入数据和用户数据进行对比
        if (model.getRate() > wallet.getHideRate()
                || model.getCurrency() > wallet.getCurrency()
                || !model.getPhone().equals(self.getPhone())
                || !model.getZfbCode().equals(self.getZfbCode())) {
            return ResponseModel.buildParameterError();
        }

        // 创建提现记录
        Money money = new Money(model, self);
        // 修改持币数
        wallet.setCurrency(wallet.getCurrency() - model.getCurrency());
        Boolean isOK = MoneyFactory.withdraw(money, wallet);

        if (isOK == null)
            return ResponseModel.buildServiceError();

        return ResponseModel.buildOk();
    }

    @Path("/list/{index}/{pageSize}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<MoneyCard>> list(@PathParam("index") int index,
                                               @PathParam("pageSize") int pageSize) {
        User self = getSelf();

        List<Money> list = MoneyFactory.list(self, index, pageSize);
        List<MoneyCard> cards = list.stream()
                .map(money -> new MoneyCard(money, self))
                .collect(Collectors.toList());

        return ResponseModel.buildOk(cards);
    }

}
