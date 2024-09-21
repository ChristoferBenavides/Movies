package com.christoferB.movies;

import com.christoferB.movies.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private MyUserDetailService userDetailService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(registry ->{

                    registry.requestMatchers("/register/user").permitAll();
                    registry.requestMatchers("/welcome").authenticated();
                    registry.requestMatchers(HttpMethod.GET, "/api/movies").permitAll();
                    registry.requestMatchers(HttpMethod.POST, "/api/movies").hasRole("USER");
                    registry.requestMatchers(HttpMethod.GET, "/api/movies/{id}").hasRole("USER");
                    registry.requestMatchers(HttpMethod.DELETE, "/api/movies/{id}").hasRole("ADMIN");
                    registry.requestMatchers(HttpMethod.PUT, "/api/movies/{id}").hasRole("ADMIN");
                    registry.requestMatchers(HttpMethod.GET,"/api/movies/vote/{id}/{rating}").permitAll();
                    registry.anyRequest().authenticated();
                })
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/403")) // error 403
                //.formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .formLogin(httpSecurityFormLoginConfigurer -> {
                    httpSecurityFormLoginConfigurer
                            .loginPage("/login")
                            .defaultSuccessUrl("/welcome", true)
                            .permitAll();
                })
                .httpBasic(withDefaults())
                .build();
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return userDetailService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
