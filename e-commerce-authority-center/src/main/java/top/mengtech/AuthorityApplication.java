package top.mengtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 授权中心启动入口
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
public class AuthorityApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(AuthorityApplication.class,args);
    }
}
