package top.mengtech;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 数据库表文档生成
 */
@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class DBDocTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void buildDoc(){
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        EngineConfig engineConfig =  EngineConfig.builder().fileOutputDir("/Users/mqm/WORKSPACE/CODE/java/e-commerce-alibaba")
                .openOutputDir(true)
                .fileType(EngineFileType.HTML)
                .produceType(EngineTemplateType.freemarker)
                .build();

        Configuration configuration = Configuration.builder()
                .version("1.0.0")
                .description("e-commerce-springcloud")
                .dataSource(dataSource)
                .engineConfig(engineConfig)
                .produceConfig(getProcessConfig())
                .build();

        new DocumentationExecute(configuration).execute();
    }

    /**
     * 配置：想要生成的数据表和忽略的数据表
     * @return
     */
    private ProcessConfig getProcessConfig(){
        List<String> ignoreTableName = Collections.singletonList("undo_log");
        List<String> ignorePrefix = Arrays.asList("a","b");
        List<String> ignoreSuffix = Arrays.asList("_test","_T");

        return ProcessConfig.builder()
                .designatedTableName(Collections.emptyList()) // 指定表名称
                .designatedTablePrefix(Collections.emptyList()) // 指定表前缀
                .designatedTableSuffix(Collections.emptyList()) // 指定表后缀
                .ignoreTableName(ignoreTableName) // 忽略表名
                .ignoreTablePrefix(ignorePrefix)    // 忽略表前缀
                .ignoreTableSuffix(ignoreSuffix)    // 按照后缀忽略
                .build();

    };


}
