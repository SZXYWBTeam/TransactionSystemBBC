package cn.szxywb.web.bbc;

import cn.szxywb.web.bbc.provider.GsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import java.util.logging.Logger;

@ApplicationPath("/")
public class Application extends ResourceConfig {

    public Application() {
        // 注册逻辑处理的包名
        packages("cn.szxywb.web.bbc.service");

        // 注册全局请求拦截器
        //register(AuthRequestFilter.class);

        // 注册Json解析器
        register(JacksonJsonProvider.class);
        register(GsonProvider.class);

        // 注册日志打印输出
        register(Logger.class);

    }
}
