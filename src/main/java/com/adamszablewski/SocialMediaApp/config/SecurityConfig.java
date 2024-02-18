package com.adamszablewski.SocialMediaApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration

public class SecurityConfig {
//
//    private CustomUserDetailsService customUserDetailsService;
//    private JwtAuthEntryPoint authEntryPoint;

//    @Autowired
//    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtAuthEntryPoint authEntryPoint) {
//        this.customUserDetailsService = customUserDetailsService;
//        this.authEntryPoint = authEntryPoint;
//    }

//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                .cors(AbstractHttpConfigurer::disable)
//                .csrf(AbstractHttpConfigurer::disable)
//                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(request -> {
//                    request.requestMatchers(antMatcher("/users/auth/**")).permitAll();
//                    request.anyRequest().authenticated();
//                })
//                .httpBasic(Customizer.withDefaults());
//
//
//        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }

//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter(){
//        return new JwtAuthenticationFilter();
//    }
}





