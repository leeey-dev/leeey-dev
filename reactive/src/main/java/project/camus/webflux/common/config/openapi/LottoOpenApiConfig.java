package project.camus.webflux.common.config.openapi;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LottoOpenApiConfig {

    @Value("${server.port}")
    private String port;

    @Bean
    public GroupedOpenApi lottoOpenApi() {

        return GroupedOpenApi.builder()
            .group("lotto")
            .packagesToScan("project.camus.webflux.app.lotto")
            .addOpenApiCustomizer(e -> e.info(new Info()
                    .title("Lotto")
                    .version("1.0"))
                .servers(List.of(new Server().url("http://127.0.0.1:" + port))))
            .build();
    }
}
