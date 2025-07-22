package com.pharma.flow.common.config;

import com.github.f4b6a3.uuid.UuidCreator;
import com.pharma.flow.adapter.persistence.entity.BaseEntity;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Configuration
@EnableR2dbcAuditing
@EnableTransactionManagement
public class R2dbcConfig {

    @Bean
    public BeforeConvertCallback<BaseEntity> idGeneratorCallback() {
        return (entity, table) -> {
            if (entity.getId() == null) {
                entity.setId(UuidCreator.getTimeOrderedEpoch());
            }
            return Mono.just(entity);
        };
    }

    @Bean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean
    public TransactionalOperator transactionalOperator(ReactiveTransactionManager transactionManager) {
        return TransactionalOperator.create(transactionManager);
    }
}
