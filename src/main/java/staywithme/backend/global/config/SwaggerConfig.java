package staywithme.backend.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration    // 스프링 실행시 설정파일 읽어드리기 위한 어노테이션
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.COOKIE)
                .scheme("bearer")
                .bearerFormat("JWT");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Bearer Token");
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer Token", securityScheme))
                .addSecurityItem(securityRequirement)
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Stay with me Swagger")
                .description("Stay with me REST API")
                .version("1.0.0");
    }

    @Bean
    public GroupedOpenApi defaultApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .pathsToMatch("/**")
                .pathsToExclude("/login", "/join", "/reissue", "/quit", "/logout")  // /login 경로는 제외
                .build();
    }

    // /login 경로에 대한 추가 API 그룹 설정
    @Bean
    public GroupedOpenApi loginApi() {
        return GroupedOpenApi.builder()
                .group("login")
                .addOpenApiCustomizer(openApi -> openApi.path("/login", new io.swagger.v3.oas.models.PathItem()
                        .post(new io.swagger.v3.oas.models.Operation()
                                .summary("로그인 로직")
                                .requestBody(new RequestBody()
                                        .content(new Content()
                                                .addMediaType("application/json", new MediaType()
                                                        .schema(new Schema()
                                                                .addProperty("username", new Schema<String>().type("string").description("로그인 ID"))
                                                                .addProperty("password", new Schema<String>().type("string").description("비밀번호"))
                                                        )
                                                )
                                        )
                                )
                                .responses(new ApiResponses()
                                        .addApiResponse("200", new ApiResponse()
                                                .description("로그인 성공 시 토큰과 회원정보를 반환합니다")
                                        )
                                        .addApiResponse("401", new ApiResponse()
                                                .description("로그인 실패 시 에러 메시지를 반환합니다")
                                        ))
                        )
                ))
                .pathsToExclude("/api/**")
                .build();
    }
}
