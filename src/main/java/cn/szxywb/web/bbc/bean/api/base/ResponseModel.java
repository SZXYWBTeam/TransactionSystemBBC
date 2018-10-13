package cn.szxywb.web.bbc.bean.api.base;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class ResponseModel<M> implements Serializable {
    // 成功
    public static final int SUCCEED = 1;
    // 未知错误
    public static final int ERROR_UNKNOWN = 0;

    // 没有找到用户信息
    public static final int ERROR_NOT_FOUND_USER = 4041;

    // 创建用户失败
    public static final int ERROR_CREATE_USER = 3001;

    // 请求参数错误
    public static final int ERROR_PARAMETERS = 4001;
    // 请求参数错误-已存在账户
    public static final int ERROR_PARAMETERS_EXIST_ACCOUNT = 4002;
    // 请求参数错误-已存在名称
    public static final int ERROR_PARAMETERS_EXIST_NAME = 4003;
    // 请求参数错误-已存在手机号
    public static final int ERROR_PARAMETERS_EXIST_PHONE = 4004;
    // 请求参数错误-已存在支付宝号码
    public static final int ERROR_PARAMETERS_EXIST_ZFB_CODE = 4005;
    // 请求参数错误-不存在这个邀请码
    public static final int ERROR_PARAMETERS_NOT_FOUND_INVITE_CODE = 4006;
    // 请求参数错误-验证码错误
    public static final int ERROR_PARAMETERS_ERROR_VER_CODE = 4007;

    // 服务器错误
    public static final int ERROR_SERVICE = 5001;

    // 账户Token错误，需要重新登录
    public static final int ERROR_ACCOUNT_TOKEN = 2001;
    // 账户登录失败
    public static final int ERROR_ACCOUNT_LOGIN = 2002;
    // 账户注册失败
    public static final int ERROR_ACCOUNT_REGISTER = 2003;
    // 没有权限操作
    public static final int ERROR_ACCOUNT_NO_PERMISSION = 2010;

    @Expose
    private int code;
    @Expose
    private String message;
    @Expose
    private LocalDateTime time = LocalDateTime.now();
    @Expose
    private M result;

    public ResponseModel() {
        code = 1;
        message = "ok";
    }

    public ResponseModel(M result) {
        this();
        this.result = result;
    }

    public ResponseModel(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseModel(int code, String message, M result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public boolean isSucceed() {
        return code == SUCCEED;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public M getResult() {
        return result;
    }

    public void setResult(M result) {
        this.result = result;
    }

    public static <M> ResponseModel<M> buildOk() {
        return new ResponseModel<M>();
    }

    public static <M> ResponseModel<M> buildOk(M result) {
        return new ResponseModel<M>(result);
    }

    public static <M> ResponseModel<M> buildParameterError() {
        return new ResponseModel<M>(ERROR_PARAMETERS, "Parameters Error.");
    }

    public static <M> ResponseModel<M> buildHaveAccountError() {
        return new ResponseModel<M>(ERROR_PARAMETERS_EXIST_ACCOUNT, "Already have this account.");
    }

    public static <M> ResponseModel<M> buildHaveNameError() {
        return new ResponseModel<M>(ERROR_PARAMETERS_EXIST_NAME, "Already have this name.");
    }

    public static <M> ResponseModel<M> buildHavePhoneError() {
        return new ResponseModel<M>(ERROR_PARAMETERS_EXIST_PHONE, "Already have this phone.");
    }

    public static <M> ResponseModel<M> buildHaveZFBCode() {
        return new ResponseModel<M>(ERROR_PARAMETERS_EXIST_ZFB_CODE, "Already have this zfbCode.");
    }

    public static <M> ResponseModel<M> buildNotFoundInviteCode() {
        return new ResponseModel<M>(ERROR_PARAMETERS_NOT_FOUND_INVITE_CODE, "Not Found InviteCode.");
    }

    public static <M> ResponseModel<M> buildErrorVerCode() {
        return new ResponseModel<M>(ERROR_PARAMETERS_ERROR_VER_CODE, "Error verCode.");
    }

    public static <M> ResponseModel<M> buildServiceError() {
        return new ResponseModel<M>(ERROR_SERVICE, "Service Error.");
    }

    public static <M> ResponseModel<M> buildNotFoundUserError(String str) {
        return new ResponseModel<M>(ERROR_NOT_FOUND_USER, str != null ? str : "Not Found User.");
    }

    public static <M> ResponseModel<M> buildAccountError() {
        return new ResponseModel<M>(ERROR_ACCOUNT_TOKEN, "Account Error; you need login.");
    }

    public static <M> ResponseModel<M> buildLoginError() {
        return new ResponseModel<M>(ERROR_ACCOUNT_LOGIN, "Account or password error.");
    }

    public static <M> ResponseModel<M> buildRegisterError() {
        return new ResponseModel<M>(ERROR_ACCOUNT_REGISTER, "Have this account.");
    }

    public static <M> ResponseModel<M> buildNoPermissionError() {
        return new ResponseModel<M>(ERROR_ACCOUNT_NO_PERMISSION, "You do not have permission to operate.");
    }

    public static <M> ResponseModel<M> buildCreateError(int type) {
        return new ResponseModel<M>(type, "Create failed.");
    }

}