package cn.szxywb.web.bbc.provider;

import cn.szxywb.web.bbc.bean.api.base.ResponseModel;
import cn.szxywb.web.bbc.bean.db.User;
import cn.szxywb.web.bbc.factory.UserFactory;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.glassfish.jersey.server.ContainerRequest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

/**
 * 用于所有的请求接口的过滤与拦截
 */
@Provider
public class AuthRequestFilter implements ContainerRequestFilter {
    public static class TokenHolder{
        public String token ;
    }
    @Context
    HttpServletRequest request ;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // 检查是否是登录注册接口
        String relationPath = ((ContainerRequest) requestContext).getPath(false);
        if (relationPath.startsWith("account/login")
                || relationPath.startsWith("account/register")
                || relationPath.startsWith("account/verCode")) {
            return;
        }

        // 从Headers中去找到第一个token节点
        String token = requestContext.getHeaders().getFirst("token");
        if(Strings.isNullOrEmpty(token)){
            String par = this.inputStreamToString(requestContext.getEntityStream()) ;
            requestContext.setEntityStream(new ByteArrayInputStream(par.getBytes("utf-8")));
            TokenHolder tokenHolder = new Gson().fromJson(par, TokenHolder.class);
            token = tokenHolder.token ;
        }
        if (!Strings.isNullOrEmpty(token)) {
            // 查询自己的信息
            User self = UserFactory.findByToken(token);
            if (self != null) {
                requestContext.setSecurityContext(new SecurityContext() {
                    @Override
                    public Principal getUserPrincipal() {
                        return self;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        return false;
                    }

                    @Override
                    public boolean isSecure() {
                        return false;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return null;
                    }
                });

                return ;
            }
        }

        // 直接返回账户需要登录的Model
        ResponseModel model = ResponseModel.buildAccountError();
        // 构建一个返回
        Response response = Response.status(Response.Status.OK)
                .entity(model)
                .build();
        // 拦截
        requestContext.abortWith(response);
    }

    public String inputStreamToString(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }
}
