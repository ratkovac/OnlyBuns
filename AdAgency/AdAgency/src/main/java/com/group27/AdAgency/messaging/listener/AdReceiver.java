package com.group27.AdAgency.messaging.listener;

import com.group27.AdAgency.dto.AdPostDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AdReceiver {

    @RabbitListener(queues = "#{anonymousQueue.name}", messageConverter = "messageConverter")
    public void receiveAd(AdPostDTO adPostDTO) {
        System.out.println("Received Ad in Agency: " + adPostDTO);
    }
}