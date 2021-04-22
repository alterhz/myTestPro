package com.magazine.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 启用csrf这个会导致rest的post请求失败，返回403错误
        http.cors().and().csrf().disable();

        http.authorizeRequests()
                .antMatchers("/admin/**").authenticated()
                .antMatchers("/filters/**").authenticated()
                .antMatchers("/config/**").authenticated()
                .antMatchers("/bookList/**").authenticated()
                .anyRequest().permitAll()

                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/bookList/")

                .and()
                    .rememberMe()
                    .tokenValiditySeconds(2419200)
                    .key("BookSecured");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public UserDetailsService users() {
        // The builder will ensure the passwords are encoded before saving in memory
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        UserDetails user = users
                .username("zhanglu")
                .password("zl_0401")
                .roles("USER")
                .build();
        UserDetails admin = users
                .username("admin")
                .password("123")
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

}
