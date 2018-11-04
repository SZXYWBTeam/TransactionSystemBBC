package cn.szxywb.web.bbc.service;

import cn.szxywb.web.bbc.bean.api.account.AccountRspModel;
import cn.szxywb.web.bbc.bean.api.account.LoginModel;
import cn.szxywb.web.bbc.bean.api.account.RegisterModel;
import cn.szxywb.web.bbc.bean.api.account.UpdateModel;
import cn.szxywb.web.bbc.bean.api.base.ResponseModel;
import cn.szxywb.web.bbc.bean.db.User;
import cn.szxywb.web.bbc.bean.db.Wallet;
import cn.szxywb.web.bbc.factory.UserFactory;
import cn.szxywb.web.bbc.factory.WalletFactory;
import cn.szxywb.web.bbc.utils.RandomValidateCode;
import com.google.common.base.Strings;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Path("/account")
public class AccountService extends BaseService {

    // 验证码缓存Map
    private static Map<String, String> vCodeMap = new HashMap<>();

    // 登录
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> login(LoginModel model) {
        if (!LoginModel.check(model))
            return ResponseModel.buildParameterError();

        // 判断验证码是否正确
        String verCode = vCodeMap.get(model.getVerCodeKey());
        // 清除验证码缓存
        vCodeMap.remove(model.getVerCodeKey());
        if (Strings.isNullOrEmpty(verCode) || !verCode.equalsIgnoreCase(model.getVerCode()))
            return ResponseModel.buildErrorVerCode();

        User user = UserFactory.login(model.getAccount(), model.getPassword());
        if (user != null) {
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        }

        // 登录失败
        return ResponseModel.buildLoginError();
    }

    // 注册
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> register(RegisterModel model) {
        if (!RegisterModel.check(model))
            return ResponseModel.buildParameterError();

        // 判断验证码是否正确
        String verCode = vCodeMap.get(model.getVerCodeKey());
        // 清除验证码缓存
        vCodeMap.remove(model.getVerCodeKey());
        if (Strings.isNullOrEmpty(verCode) || !verCode.equalsIgnoreCase(model.getVerCode()))
            return ResponseModel.buildErrorVerCode();

        // 账户重复
        User user = UserFactory.findByAccount(model.getAccount().trim());
        if (user != null)
            return ResponseModel.buildHaveAccountError();

        // 用户名重复
        user = UserFactory.findByName(model.getName().trim());
        if (user != null)
            return ResponseModel.buildHaveNameError();

        // 手机号重复
        user = UserFactory.findByPhone(model.getPhone().trim());
        if (user != null)
            return ResponseModel.buildHavePhoneError();

        // 支付宝号码重复
        user = UserFactory.findByZfbCode(model.getZfbCode().trim());
        if (user != null)
            return ResponseModel.buildHaveZFBCode();

        if (!Strings.isNullOrEmpty(model.getOtherInviteCode())) {
            // 邀请码不存在
            user = UserFactory.findByInviteCode(model.getOtherInviteCode().trim());
            if (user == null) {
                return ResponseModel.buildNotFoundInviteCode();
            }
        }

        // 创建用户
        user = UserFactory.register(model.getAccount(),
                model.getPassword(),
                model.getPhone(),
                model.getName(),
                model.getZfbCode(),
                model.getOtherInviteCode());

        // 注册异常
        if (user == null)
            return ResponseModel.buildRegisterError();

        // 创建用户对应的钱包
        Wallet wallet = new Wallet();
        wallet.setUserId(user.getId());
        wallet = WalletFactory.update(wallet);

        // 用户钱包创建异常
        if(wallet == null)
            return ResponseModel.buildServiceError();

        // 返回当前账户
        AccountRspModel rspModel = new AccountRspModel(user, wallet);
        return ResponseModel.buildOk(rspModel);
    }

    @GET
    @Path("/verCode/{dataTime}")
    @Produces("image/png")
    public void getVerCode(@PathParam("dataTime") String dataTime, @Context HttpServletResponse resp) {
        RandomValidateCode code = new RandomValidateCode();
        BufferedImage randImage = code.getRandImage();

        // 保存到验证码Map中进行保存
        vCodeMap.put(dataTime, code.randomString);
        try {
            ImageIO.write(randImage, "png", resp.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 修改密码
    @POST
    @Path("/updatePwd")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> updatePwd(UpdateModel model) {
        if (!UpdateModel.checkPwd(model))
            return ResponseModel.buildParameterError();

        User user = getSelf();
        if (user == null)
            return ResponseModel.buildNotFoundUserError(null);
        if (!user.getPassword().equals(UserFactory.encodePassword(model.getOldPwd())))
            return ResponseModel.buildErrorOldPwd();

        user.setPassword(UserFactory.encodePassword(model.getNewPwd()));
        user = UserFactory.update(user);
        // 服务器更新异常
        if (user == null)
            return ResponseModel.buildServiceError();

        return ResponseModel.buildOk(new AccountRspModel(user));
    }

    // 修改个人资料
    @POST
    @Path("/updateInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> updateInfo(UpdateModel model) {
        if (!UpdateModel.checkInfo(model))
            return ResponseModel.buildParameterError();

        User user = getSelf();
        if (user == null)
            return ResponseModel.buildNotFoundUserError(null);

        user.setPhone(model.getPhone());
        user.setName(model.getName());
        user.setZfbCode(model.getZfbCode());
        user = UserFactory.update(user);
        if (user == null)
            return ResponseModel.buildServiceError();

        return ResponseModel.buildOk(new AccountRspModel(user));
    }


    @GET
    @Path("/ll")
    public String get() {
        return "GET";
    }
}
