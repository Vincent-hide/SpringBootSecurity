package hungrybird.springbootsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http                                                    // withHttpOnlyFalse: disable cookie from client side. ex. someone tries to get cookie with js, it would be impossible.
//      .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//      .and()
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
        .antMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name()) // allows only student to access api/v1/students
  //      .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
  //      .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
  //      .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
  //      .antMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ApplicationUserRole.ADMIN.name(), ApplicationUserRole.ADMINTRAINEE.name())
        .anyRequest()
        .authenticated()
      .and()
        .formLogin()
        .loginPage("/login").permitAll()
        .defaultSuccessUrl("/courses", true)
        .passwordParameter("password")
        .usernameParameter("username")
      .and()
        .rememberMe()
  //      .tokenRepository() // it can be used when you store the session ID in database
        .tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(21))
        .key("somethingverysecured")
        .rememberMeParameter("remember-me")
      .and()
        .logout()
        .logoutUrl("/logout")
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID", "remember-me")
        .logoutSuccessUrl("/login");

  }

  @Override
  @Bean
  protected UserDetailsService userDetailsService() {
    // ROLE: Student
    UserDetails vince = User.builder()
      .username("vince")
      .password(passwordEncoder.encode("password"))
      //.roles(ApplicationUserRole.STUDENT.name()) // ROLE_STUDENT
      .authorities((ApplicationUserRole.STUDENT.getGrantedAuthority()))
      .build();

    // Role: Admin
    UserDetails hide = User.builder()
      .username("hide")
      .password(passwordEncoder.encode("password"))
      //.roles(ApplicationUserRole.ADMIN.name())
      .authorities(ApplicationUserRole.ADMIN.getGrantedAuthority())
      .build();

    // Role: Admin Trainee
    UserDetails suho = User.builder()
      .username("suho")
      .password(passwordEncoder.encode("password"))
      //.roles(ApplicationUserRole.ADMINTRAINEE.name())
      .authorities(ApplicationUserRole.ADMINTRAINEE.getGrantedAuthority())
      .build();

    return new InMemoryUserDetailsManager(vince, hide, suho);
  }
}
