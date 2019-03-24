package com.freeco.config;

import javax.sql.DataSource;

import com.freeco.security.UrlUserService;

import com.freeco.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UrlUserService urlUserService;
    @Autowired
    SessionRegistry sessionRegistry;
    @Autowired
    private DataSource dataSource;

     @Bean
     public PersistentTokenRepository persistentTokenRepository(){
         JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
         tokenRepository.setDataSource(dataSource);
         //如果token表不存在，使用下面语句可以初始化该表；若存在，请注释掉这条语句，否则会报错。
//         tokenRepository.setCreateTableOnStartup(true);
         return tokenRepository;
     }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	System.out.println("--------------------------------------WebSecurityConfigurerAdapter configure..");
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/login").permitAll()
            .antMatchers("/logout").permitAll()
            .antMatchers("/images/**").permitAll()
            .antMatchers("/js/**").permitAll()
            .antMatchers("/css/**").permitAll()
            .antMatchers("/fonts/**").permitAll()
            .antMatchers("/favicon.ico").permitAll()
            .antMatchers("/users/**").permitAll()
//            .antMatchers("/").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login")	//设置登陆页
            .defaultSuccessUrl("/").permitAll()	//设置登陆成功页
            .failureUrl("/login?error")
            //自动登录
            .and().rememberMe()
            .tokenRepository(persistentTokenRepository())
            //有效时间：单位s
            .tokenValiditySeconds(600)
            .and()
            .sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry)
            .and()
            .and()
            .logout()
            .invalidateHttpSession(true)	//注销清除会话
            .clearAuthentication(true)
            .and()
            .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(urlUserService).passwordEncoder(new PasswordEncoder() {

            public String encode(CharSequence rawPassword) {
                return MD5Util.encode((String) rawPassword);
            }

            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encodedPassword.equals(MD5Util.encode((String) rawPassword));
            }
        });
    }

//    @Bean
//    public SessionRegistry getSessionRegistry(){
//        SessionRegistry sessionRegistry=new SessionRegistryImpl();
//        return sessionRegistry;
//    }
}