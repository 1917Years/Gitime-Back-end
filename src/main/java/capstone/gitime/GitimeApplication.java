package capstone.gitime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing
public class GitimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitimeApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

//	@Bean
//	public AuditorAware<Long> auditorAware() {
//		return () ->
//				Optional.of(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()));
//	}
}
