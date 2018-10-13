package cn.szxywb.web.bbc.service;

import cn.szxywb.web.bbc.bean.api.account.AccountRspModel;
import cn.szxywb.web.bbc.bean.api.account.LoginModel;
import cn.szxywb.web.bbc.bean.api.account.RegisterModel;
import cn.szxywb.web.bbc.bean.api.base.ResponseModel;
import cn.szxywb.web.bbc.bean.db.User;
import cn.szxywb.web.bbc.factory.UserFactory;
import cn.szxywb.web.bbc.utils.RandomValidateCode;
import com.google.common.base.Strings;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Path("/account")
public class AccountService extends BaseService {

    // 验证码标志
    private static final String VER_CODE = "ver_code";

    // 登录
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> login(@Context HttpServletRequest req, LoginModel model) {
        if (!LoginModel.check(model))
            return ResponseModel.buildParameterError();

        // 判断验证码是否正确
        String verCode = (String) req.getSession().getAttribute(VER_CODE);
        if(Strings.isNullOrEmpty(verCode) || !verCode.equalsIgnoreCase(model.getVerCode()))
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
    public ResponseModel<AccountRspModel> register(@Context HttpServletRequest req, RegisterModel model) {
        if (!RegisterModel.check(model))
            return ResponseModel.buildParameterError();

        // 判断验证码是否正确
        String verCode = (String) req.getSession().getAttribute(VER_CODE);
        if(Strings.isNullOrEmpty(verCode) || !verCode.equalsIgnoreCase(model.getVerCode()))
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
            } else {
                // TODO 邀请码作用
            }
        }

        // 创建用户
        user = UserFactory.register(model.getAccount(),
                model.getPassword(),
                model.getPhone(),
                model.getName(),
                model.getZfbCode());

        if (user != null) {
            // 返回当前账户
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        } else {
            // 注册异常
            return ResponseModel.buildRegisterError();
        }
    }

    @GET
    @Path("/verCode")
    @Produces("image/png")
    public void getVerCode(@Context HttpServletRequest req, @Context HttpServletResponse resp) {
        RandomValidateCode code = new RandomValidateCode();
        BufferedImage randImage = code.getRandImage();

        //System.out.println(code.randomString);
        req.getSession().setAttribute(VER_CODE, code.randomString);

        try {
            ImageIO.write(randImage, "png", resp.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("/ll")
    public String get() {
        return "GET";
    }
}
