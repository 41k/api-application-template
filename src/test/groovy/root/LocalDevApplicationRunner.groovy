package root

import org.springframework.boot.SpringApplication

class LocalDevApplicationRunner {
    static void main(String[] args) {
        def application = new SpringApplication(ApplicationRunner.class)
        System.setProperty('spring.profiles.default', 'test')
        application.run(args)
    }
}
