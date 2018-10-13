package cn.szxywb.web.bbc.service;

import cn.szxywb.web.bbc.bean.api.account.AccountRspModel;
import cn.szxywb.web.bbc.bean.api.account.LoginModel;
import cn.szxywb.web.bbc.bean.api.base.ResponseModel;
import cn.szxywb.web.bbc.bean.db.User;
import cn.szxywb.web.bbc.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/account")
public class AccountService extends BaseService {

    // 登录
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> login(LoginModel model) {
        if(!LoginModel.check(model))
            return ResponseModel.buildParameterError();

        User user = UserFactory.login(model.getAccount(), model.getPassword());
        if(user != null) {
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        }

        return ResponseModel.buildLoginError();
    }

    @GET
    @Path("/ll")
    public String get() {
        return "GET";
    }
}
