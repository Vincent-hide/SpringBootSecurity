package hungrybird.springbootsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
      .authorizeRequests()
      .antMatchers("/", "index", "/css/*", "/js/*")
      .permitAll()
      .anyRequest()
      .authenticated()
      .and()
      .httpBasic();
  }

  @Override
  @Bean
  protected UserDetailsService userDetailsService() {
    UserDetails vince = User.builder()
      .username("vince")
      .password(passwordEncoder.encode("password"))
      .roles(ApplicationUserRole.STUDENT.name()) // ROLE_STUDENT
      .build();

    UserDetails hide = User.builder()
      .username("hide")
      .password(passwordEncoder.encode("password123"))
      .roles(ApplicationUserRole.ADMIN.name())
      .build();

    return new InMemoryUserDetailsManager(vince, hide);
  }
}
