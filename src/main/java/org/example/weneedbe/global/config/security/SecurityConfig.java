package org.example.weneedbe.global.config.security;

import lombok.RequiredArgsConstructor;
import org.example.weneedbe.domain.token.repository.RefreshTokenRepository;
import org.example.weneedbe.domain.user.service.UserService;
import org.example.weneedbe.global.config.jwt.TokenAuthenticationFilter;
import org.example.weneedbe.global.config.jwt.TokenProvider;
import org.example.weneedbe.global.config.oauth.OAUth2AUthorizationRequestBasedOnCookieRepository;
import org.example.weneedbe.global.config.oauth.OAuth2SuccessHandler;
import org.example.weneedbe.global.config.oauth.OAuth2UserCustomService;
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
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

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

              .oauth2Login(oauth2Login ->
                      oauth2Login
                              .authorizationEndpoint(authorization -> authorization
                                      .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()))
                              .successHandler(oAuth2SuccessHandler())
                              .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                      .userService(oAuth2UserCustomService))
              )

              .logout(logout -> logout
                      .logoutSuccessUrl("/login"))
              .exceptionHandling(exceptionHandling -> exceptionHandling
                      .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                              new AntPathRequestMatcher("/api/**")));
    return http.build();
  }

  @Bean
  public OAuth2SuccessHandler oAuth2SuccessHandler() {
      return new OAuth2SuccessHandler(tokenProvider, refreshTokenRepository,
              oAuth2AuthorizationRequestBasedOnCookieRepository(), userService);
  }

  @Bean
  public TokenAuthenticationFilter tokenAuthenticationFilter() {
      return new TokenAuthenticationFilter(tokenProvider);
  }

  @Bean
  public OAUth2AUthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
      return new OAUth2AUthorizationRequestBasedOnCookieRepository();
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