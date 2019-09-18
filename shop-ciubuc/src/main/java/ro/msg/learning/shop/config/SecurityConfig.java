package ro.msg.learning.shop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ro.msg.learning.shop.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.option}")
    private String securityOption;
    private static final String LOGIN_PAGE = "/login";
    private static final String PRODUCTS_PAGE = "/product/**";
    private static final String ORDERS_PAGE = "/order/**";
    private static final String H2_CONSOLE = "/h2-console/**";

    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);

        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if ("with-basic".equals(securityOption)) {
            configureHttpBasic(http);
        } else if ("with-form".equals(securityOption)) {
            configureFormBased(http);
        }
    }

    private void configureHttpBasic(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(PRODUCTS_PAGE).hasRole("ADMIN")
                .antMatchers(ORDERS_PAGE).permitAll()
                .antMatchers("/", H2_CONSOLE).authenticated()
                .antMatchers(LOGIN_PAGE).permitAll()
                .and()
                .httpBasic().and()
                .headers().frameOptions().disable();
    }

    private void configureFormBased(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(PRODUCTS_PAGE).hasRole("ADMIN")
                .antMatchers(ORDERS_PAGE).hasRole("CUSTOMER")
                .antMatchers("/", H2_CONSOLE).authenticated()
                .antMatchers(LOGIN_PAGE).permitAll()
                .and()
                .formLogin()
                .loginPage(LOGIN_PAGE).permitAll().failureForwardUrl("/login-error")
                .and()
                .logout().permitAll()
                .and()
                .headers().frameOptions().disable();
    }
}
