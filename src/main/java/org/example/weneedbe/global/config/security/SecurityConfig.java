package org.example.weneedbe.global.config.security;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.global.config.jwt.TokenAuthenticationFilter;
import org.example.weneedbe.global.config.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http
              .httpBasic(httpBasic ->
                      httpBasic.disable()
              )
              .cors(corsConfig ->
                      corsConfig.configurationSource(corsConfigurationSource()))
              .csrf(csrf ->
                      csrf.disable()
              )
              .formLogin(formLogin ->
                      formLogin.disable())
              .sessionManagement((sessionManagement) ->
                      sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              )
              .authorizeHttpRequests((authorize) ->
                      authorize
                              .requestMatchers("/").permitAll()
                              .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/**").permitAll()
                              .anyRequest().permitAll()
              )
              .addFilterBefore(new TokenAuthenticationFilter(tokenProvider),
                      UsernamePasswordAuthenticationFilter.class)

              .exceptionHandling(exceptionHandling -> exceptionHandling
                      .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                              new AntPathRequestMatcher("/api/**")));
    return http.build();
  }


  @Bean
  public TokenAuthenticationFilter tokenAuthenticationFilter() {
      return new TokenAuthenticationFilter(tokenProvider);
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(Arrays.asList("*"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}