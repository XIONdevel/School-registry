package com.example.school.security.config;


import com.example.school.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        jwtFilter, UsernamePasswordAuthenticationFilter.class
                )
                .authorizeHttpRequests(authorize -> authorize       //TODO: configure matchers
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers("/**").permitAll()
//                                .requestMatchers("/**").hasAuthority(Permission.ADMIN.name())
//                        .requestMatchers("/api/v1/**").hasAuthority(Role.DIRECTOR.name())
//                        .requestMatchers("/api/v1/**").hasAuthority(Role.OFFICE.name())
//                        .requestMatchers("/api/v1/**").hasAuthority(Role.TEACHER.name())
//                        .requestMatchers("/api/v1/**").hasAuthority(Role.STAFF.name())
//                        .requestMatchers("/api/v1/**").hasAuthority(Role.STUDENT.name())
//                        .requestMatchers("/api/v1/**").hasAuthority(Role.PARENT.name())
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin       //TODO: add login page
                        .loginPage("/api/v1/auth/login")
                        .permitAll()
                )
                .rememberMe(Customizer.withDefaults());
        return http.build();
    }

}
