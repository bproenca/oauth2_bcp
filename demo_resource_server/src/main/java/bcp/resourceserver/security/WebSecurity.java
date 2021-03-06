package bcp.resourceserver.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

//Enables the use of annotions @Secured
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

        http        //.cors().and()
            .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/users/status/check")
                    //.hasAuthority("SCOPE_profile") //client scopes (default or requested)
                    .hasRole("developer") // user role mappings
                    // .hasAnyAuthority("ROLE_developer") // same as line above
                    // .hasAnyRole("devleoper","user")
                    .antMatchers(HttpMethod.GET, "/actuator/info")
                    .permitAll()
                .anyRequest().authenticated()
                .and()
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthenticationConverter)
        ;
    }

    // @Bean
    // CorsConfigurationSource corsConfigurationSource() {
    //     CorsConfiguration corsConfiguration = new CorsConfiguration();
    //     corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
    //     corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","OPTIONS"));
    //     corsConfiguration.setAllowedHeaders(Arrays.asList("*"));

    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", corsConfiguration);

    //     return source;
    // }
}
