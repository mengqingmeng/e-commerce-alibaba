package top.mengtech.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * 事件推送：动态更新路由网关Service
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class DynamicRouterServiceImpl implements ApplicationEventPublisherAware {

    /**
     * 写路由定义
     */
    private final RouteDefinitionWriter routeDefinitionWriter;

    /**
     * 获取路由定义
     */
    private final RouteDefinitionLocator routeDefinitionLocator;

    /**
     * 事件发布
     */
    private ApplicationEventPublisher publisher;

    public DynamicRouterServiceImpl(RouteDefinitionWriter routeDefinitionWriter,
                                    RouteDefinitionLocator routeDefinitionLocator) {
        this.routeDefinitionWriter = routeDefinitionWriter;
        this.routeDefinitionLocator = routeDefinitionLocator;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        // 事件发布具柄的初始化
        this.publisher = applicationEventPublisher;
    }

    /**
     * 新增路由定义：从nacos的配置文件中读取路径配置，并且新增到路由定义中。
     * @param routeDefinition 路由定义
     * @return
     */
    public String addRouteDefinition(RouteDefinition routeDefinition){
        log.info("gateway add route:[{}]",routeDefinition);

        // 保存路由定义并发布
        routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();

        // 事件通知给网关，同步新增的定义
        this.publisher.publishEvent(new RefreshRoutesEvent(this));

        return "success";
    }

    /**
     * 根据路由ID深处路由
     * @param id
     * @return
     */
    private String deleteById(String id){
        try{
            log.info("gateway delete route id:[{}]",id);
            this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();

            // 发布事件通知，更新路由定义
            this.publisher.publishEvent(new RefreshRoutesEvent(this));

            return "delete success";
        }catch(Exception ex) {
            log.error("gateway delete route fail:[{}]", ex.getMessage(), ex);
            return "delete fail";
        }
    }

    public String updateList(List<RouteDefinition> routeDefinitionList){
        log.info("gateway update route:[{}]",routeDefinitionList);

        // 先拿到当前网关中存储的路由定义
        List<RouteDefinition> routeDefinitionsExist = routeDefinitionLocator.getRouteDefinitions()
                .buffer().blockFirst();

        // 清除
        if(!CollectionUtils.isEmpty(routeDefinitionsExist)){
            // 清除旧的路由定义
            routeDefinitionsExist.forEach(rd->{
                log.info("delete route definition:[{}]",rd.getId());
                deleteById(rd.getId());
            });
        }

        // 新增
        routeDefinitionList.forEach(rd->{
            addRouteDefinition(rd);
        });

        return "success";
    }

    /**
     * 更新路由 ： 删除 + 新增 = 更新
     * @param routeDefinition
     * @return
     */
    public String updateRouteDefinition(RouteDefinition routeDefinition){

        try{
            log.info("gateway update route:[{}]",routeDefinition);

            // 删除
            this.routeDefinitionWriter.delete(Mono.just(routeDefinition.getId()));
            // 新增
            this.routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();

            // 更新
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "update success";
        }catch(Exception ex){
            return "update fail,not find route routeId:" + routeDefinition.getId();
        }
    }
}
