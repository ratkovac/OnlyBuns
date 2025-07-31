package com.group27.OnlyBuns.publisher;

import dto.AdPostDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Component;

@Component
public class AdPostPublisher {

    private final RabbitTemplate rabbitTemplate;

    public AdPostPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        // Povezivanje RabbitTemplate sa Jackson2JsonMessageConverter
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    public void sendAdPost(AdPostDTO adPostDTO) {
        rabbitTemplate.convertAndSend("adPostFanoutExchange", "", adPostDTO);
    }
}
