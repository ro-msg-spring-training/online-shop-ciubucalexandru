package ro.msg.learning.shop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "github")
@Data
public class GitHubSettings {

    private GitHubResource resource;
    private GitHubClient client;

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "client")
    public static class GitHubClient {
        private String clientId;
        private String clientSecret;
        private String accessTokenUri;
        private String userAuthorizationUri;
        private String clientAuthenticationScheme;
        private String scope;
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "resource")
    public static class GitHubResource {
        private String userInfoUri;
    }
}
