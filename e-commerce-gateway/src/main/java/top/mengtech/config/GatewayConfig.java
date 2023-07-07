package top.mengtech.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    /**
     * 读取配置的超时时间
     */
    public static final long DEFAULT_TIMEOUT = 30000;

    /**
     * nacos服务地址
     */
    public static String NACOS_SERVER_ADDR;

    /**
     *nacos命名空间
     */
    public static String NACOS_NAMESPACE;

    /**
     * data-id
     */
    public static String NACOS_ROUTER_DATA_ID;

    /**
     * 分组名
     */
    public static String NACOS_ROUTE_GROUP;

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    public void setNacosServerAddr(String nacosServerAddr){
        NACOS_SERVER_ADDR = nacosServerAddr;
    }

    @Value("${spring.cloud.nacos.discovery.namespace}")
    public void setNacosNamespace(String nacosNamespace){
        NACOS_NAMESPACE = nacosNamespace;
    }

    @Value("${nacos.gateway.route.config.data-id}")
    public void setNacosRouterDataId(String nacosRouterDataId){
        NACOS_ROUTER_DATA_ID = nacosRouterDataId;
    }

    @Value("${nacos.gateway.route.config.group}")
    public void setNacosRouteGroup(String nacosRouteGroup){
        NACOS_ROUTE_GROUP = nacosRouteGroup;
    }
}
