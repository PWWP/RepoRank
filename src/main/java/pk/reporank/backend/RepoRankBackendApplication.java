package pk.reporank.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RepoRankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RepoRankBackendApplication.class, args);
    }

}
