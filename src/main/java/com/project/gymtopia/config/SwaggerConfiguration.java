package com.project.gymtopia.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
  @Bean
  public OpenAPI usedItemsApi(){
    return new OpenAPI()
        .info(new Info()
            .version("0.1")
            .title("Gymtopia")
            .description("누구나 온라인으로 피트니스를 즐길 수 있는 짐토피아!"));
  }
}
