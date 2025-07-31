package com.group27.OnlyBuns.agency.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "adPostFanoutExchange";

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_NAME);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue agencyQueue1() {
        return new Queue("agencyQueue1");
    }

    @Bean
    public Binding binding1(FanoutExchange fanoutExchange, Queue agencyQueue1) {
        return BindingBuilder
                .bind(agencyQueue1())
                .to(fanoutExchange());
    }
}
