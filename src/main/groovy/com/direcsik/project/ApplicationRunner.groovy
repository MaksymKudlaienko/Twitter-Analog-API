package com.direcsik.project


import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories(basePackages = 'com.direcsik.project.repository')
class ApplicationRunner {

    static void main(String[] args) {
        SpringApplication.run(ApplicationRunner, args)
    }

}
