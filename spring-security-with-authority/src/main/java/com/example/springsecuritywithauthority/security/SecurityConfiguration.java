package com.example.springsecuritywithauthority.security;

import com.example.springsecuritywithauthority.security.filter.CustomFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Locale;

/*
The prePostEnabled property enables Spring Security pre/post annotations.
The securedEnabled property determines if the @Secured annotation should be enabled.
The jsr250Enabled property allows us to use the @RoleAllowed annotation.
*/

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {
  private final UserDetailsService userDetailsService;

  public SecurityConfiguration(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(
            authorize ->
                authorize
                    //  public endpoint everyone can reach without any authority
                    .antMatchers("/h2-console", "/api/user/register")
                    .permitAll()

                    // admin endpoint only admin authority can reach
                    // .antMatchers("/api/user/admin-login")
                    // .hasAuthority(Authority.ADMIN.getAuthority().toUpperCase(Locale.ENGLISH))

                    //  user endpoint only user authority can reach
                    // .antMatchers("/api/user/user-login")
                    // .hasAnyAuthority(Authority.ADMIN
                    // .getAuthority().toUpperCase(Locale.ENGLISH),Authority.USER.getAuthority().toUpperCase(Locale.ENGLISH))

                    //  editor endpoint only editor authority can reach
                    // .antMatchers("/api/user/editor-login")
                    // .hasAuthority(Authority.EDITOR.getAuthority().toUpperCase(Locale.ENGLISH))

                    //  any authority can reach
                    .antMatchers("/api/user/any-of-request-login")
                    .hasAnyAuthority(
                        Authority.ADMIN.getAuthority().toUpperCase(Locale.ENGLISH),
                        Authority.USER.getAuthority().toUpperCase(Locale.ENGLISH),
                        Authority.EDITOR.getAuthority().toUpperCase(Locale.ENGLISH),
                        Authority.READONLY.getAuthority().toUpperCase(Locale.ENGLISH)))
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(Customizer.withDefaults())
        .addFilterBefore(new CustomFilter(), BasicAuthenticationFilter.class)
        .headers()
        .frameOptions()
        .disable().and()
        .build();
  }

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }
}
