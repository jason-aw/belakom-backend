package com.belajarkomputer.belakombackend.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@AllArgsConstructor
@EnableTransactionManagement
public class MongoConfig extends AbstractMongoClientConfiguration {

  private MongoProperties mongoProperties;

  @Bean
  MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory mongoDatabaseFactory) {
    return new MongoTransactionManager(mongoDatabaseFactory);
  }

  @Override
  protected String getDatabaseName() {
    return this.mongoProperties.getDatabase();
  }
}
