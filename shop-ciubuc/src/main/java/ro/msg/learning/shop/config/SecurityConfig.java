package ro.msg.learning.shop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.request.RequestContextListener;
import ro.msg.learning.shop.service.CustomUserDetailsService;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.ServletContext;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@EnableOAuth2Client
@EnableAuthorizationServer
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final OAuth2ClientContext oAuth2ClientContext;
    private final GitHubSettings gitHubSettings;
    private final ServletContext servletContext;

    @Override
    protected void configure(HttpSecurity http) {
        http
                .addFilterBefore(socialFilter(), BasicAuthenticationFilter.class);
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService);
    }

    private Filter socialFilter() {

        OAuth2ClientAuthenticationProcessingFilter filter =
                new OAuth2ClientAuthenticationProcessingFilter("/oauth/authorize");

        OAuth2RestTemplate template =
                new OAuth2RestTemplate(authorizationCodeResourceDetails(), oAuth2ClientContext);

        filter.setRestTemplate(template);

        UserInfoTokenServices tokenServices = new UserInfoTokenServices(
                resourceServerProperties().getUserInfoUri(), authorizationCodeResourceDetails().getClientId());

        tokenServices.setRestTemplate(template);
        filter.setTokenServices(tokenServices);

        return filter;
    }

    @Bean
    public AuthorizationCodeResourceDetails authorizationCodeResourceDetails() {
        AuthorizationCodeResourceDetails resourceDetails = new AuthorizationCodeResourceDetails();

        GitHubSettings.GitHubClient client = gitHubSettings.getClient();
        resourceDetails.setClientId(client.getClientId());
        resourceDetails.setClientSecret(client.getClientSecret());
        resourceDetails.setAccessTokenUri(client.getAccessTokenUri());
        resourceDetails.setUserAuthorizationUri(client.getUserAuthorizationUri());
        resourceDetails.setClientAuthenticationScheme(AuthenticationScheme.valueOf(client.getClientAuthenticationScheme()));
        resourceDetails.setScope(Collections.singletonList(client.getScope()));

        return resourceDetails;
    }

    private ResourceServerProperties resourceServerProperties() {
        ResourceServerProperties resourceServerProperties = new ResourceServerProperties();
        resourceServerProperties.setUserInfoUri(gitHubSettings.getResource().getUserInfoUri());
        return resourceServerProperties;
    }

    @Bean
    public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    @PostConstruct
    public void onStartup() {
        servletContext.addListener(new RequestContextListener());
    }

    @Bean
    @Order(0)
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}
