//package com.rays.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//@Configuration
//@EnableWebSecurity
//@Order(101) // Assign a different order value
//
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//            .authorizeRequests()
//                .antMatchers("/Auth/**", "/User/**").permitAll() // Public endpoints
//                .anyRequest().authenticated() // All other requests need authentication
//            .and()
//            .oauth2Login()
//                .loginPage("/Auth/google") // Custom login page
//                .userInfoEndpoint()
//                .userService(customOAuth2UserService()) // Custom OAuth2 user service
//            .and()
//            .defaultSuccessUrl("/Auth/loginSuccess") // Redirect after successful login
//            .failureUrl("/Auth/loginFailure"); // Redirect after login failure
//    }
//
//    @Bean
//    public CustomOAuth2UserService customOAuth2UserService() {
//        return new CustomOAuth2UserService();
//    }
//
//    // Custom service to handle OAuth2 user information
//    public static class CustomOAuth2UserService extends DefaultOAuth2UserService {
//        @Override
//        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//            OAuth2User oauth2User = super.loadUser(userRequest);
//            // Custom processing, e.g., mapping user roles, updating user info, etc.
//            return oauth2User;
//        }
//    }
//}
