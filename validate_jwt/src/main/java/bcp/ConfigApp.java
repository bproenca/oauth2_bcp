package bcp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigApp {

    @Value("${my-resource-server.keycloak-host-port}")
    public String keycloakHostPort;
    
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        final FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<JwtFilter>();
        registrationBean.setFilter(new JwtFilter(keycloakHostPort));
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }
    
}