package hungrybird.springbootsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .authorizeRequests()
      .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
      .antMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name()) // allows only student to access api/v1/students
      .anyRequest()
      .authenticated()
      .and()
      .httpBasic();
  }

  @Override
  @Bean
  protected UserDetailsService userDetailsService() {
    // ROLE: Student
    UserDetails vince = User.builder()
      .username("vince")
      .password(passwordEncoder.encode("password"))
      .roles(ApplicationUserRole.STUDENT.name()) // ROLE_STUDENT
      .build();

    // Role: Admin
    UserDetails hide = User.builder()
      .username("hide")
      .password(passwordEncoder.encode("password"))
      .roles(ApplicationUserRole.ADMIN.name())
      .build();

    // Role: Admin Trainee
    UserDetails suho = User.builder()
      .username("suho")
      .password(passwordEncoder.encode("password"))
      .roles(ApplicationUserRole.ADMINTRAINEE.name())
      .build();

    return new InMemoryUserDetailsManager(vince, hide, suho);
  }
}
