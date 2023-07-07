package top.mengtech.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

// 监听nacos中路由变更
@Slf4j
@Component
@DependsOn({"gatewayConfig"})
public class DynamicRouterServiceImplByNacos {

    private ConfigService configService;

    private final DynamicRouterServiceImpl dynamicRouterService;

    public DynamicRouterServiceImplByNacos(DynamicRouterServiceImpl dynamicRouterService) {
        this.dynamicRouterService = dynamicRouterService;
    }

    // 构造完成后执行的方法：
    @PostConstruct
    public void init(){
        log.info("gateway route init...");
        try{
            // 1. 初始化nacos的配置客户端
            initConfigService();
            if(configService ==null){
                log.error("init nacos config service fail");
                return;
            }

            // 指定路由配置路径获取配置
            String configInfo = configService.getConfig(
                    GatewayConfig.NACOS_ROUTER_DATA_ID,
                    GatewayConfig.NACOS_ROUTE_GROUP,
                    GatewayConfig.DEFAULT_TIMEOUT
            );
            List<RouteDefinition> definitions = JSON.parseArray(configInfo, RouteDefinition.class);
            if(!CollectionUtils.isEmpty(definitions)){
                for(RouteDefinition definition:definitions){
                    dynamicRouterService.addRouteDefinition(definition);
                }
            }

        }catch (Exception e){
            log.error("gateway route init has some error:[{}]",e.getMessage(),e);
        }

        dynamicRouteByNacosListener(GatewayConfig.NACOS_ROUTER_DATA_ID,GatewayConfig.NACOS_ROUTE_GROUP);
    }

    public ConfigService initConfigService(){
        try{
            Properties properties = new Properties();
            properties.setProperty("serverAddr",GatewayConfig.NACOS_SERVER_ADDR);
            properties.setProperty("namespace",GatewayConfig.NACOS_NAMESPACE);
            return configService = NacosFactory.createConfigService(properties);
        }catch (Exception ex){
            log.error("init gateway nacos config error:[{}]",ex.getMessage(),ex);
            return null;
        }
    }

    /**
     * 监听nacos下发的动态路由配置
     * @param dataId
     * @param group
     */
    private void dynamicRouteByNacosListener(String dataId,String group){
        try{
            configService.addListener(dataId, group, new Listener() {

                // 自己提供线程池执行操作
                @Override
                public Executor getExecutor() {
                    // 使用默认线程池
                    return null;
                }

                // 收到配置更新
                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("start to update config:[{}]",configInfo);

                    List<RouteDefinition> definitions = JSON.parseArray(configInfo, RouteDefinition.class);
                    log.info("update route:[{}]",definitions.toString());

                    dynamicRouterService.updateList(definitions);
                }
            });
        }catch (NacosException e){
            log.error("dynamic update gateway config error:[{}]",e.getMessage(),e);
        }
    }
}
