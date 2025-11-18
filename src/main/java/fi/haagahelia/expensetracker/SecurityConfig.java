package fi.haagahelia.expensetracker;

//import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

        // @Autowired
        private UserDetailsService userDetailsService; // type of attribute -> interface

        // Constructor injection
        public SecurityConfig(UserDetailsService userDetailsService) {
                this.userDetailsService = userDetailsService;
        }

        @Bean
        public SecurityFilterChain configure(HttpSecurity http) throws Exception {

                http.authorizeHttpRequests(
                                authorize -> authorize
                                                .requestMatchers("/css/**").permitAll()
                                                .requestMatchers("/api/**").permitAll()
                                                // .requestMatchers(toH2Console()).permitAll() // for h2console
                                                .requestMatchers("/login", "/logout").permitAll()
                                                .anyRequest().authenticated())
                                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions
                                                .disable())) // for h2console
                                .formLogin(formlogin -> formlogin
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/expenselist", true)
                                                .permitAll())
                                .logout(logout -> logout.permitAll())
                                // Disable CSRF for /api/** and H2 console
                                .csrf(csrf -> csrf
                                                // .ignoringRequestMatchers(toH2Console()) // PathRequest matcher
                                                .ignoringRequestMatchers("/api/**") // String pattern
                                );

                return http.build();
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        }
}
