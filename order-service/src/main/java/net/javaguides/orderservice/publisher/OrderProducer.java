package net.javaguides.orderservice.publisher;

import net.javaguides.orderservice.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private static final Logger LOGGER= LoggerFactory.getLogger(OrderProducer.class);

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.binding.routing.key}")
    private String orderRoutingKey;

    @Value("${rabbitmq.binding.email.routing.key}")
    private String emailRoutingKey;

    private RabbitTemplate template;

    @Autowired
    public OrderProducer(RabbitTemplate template) {
        this.template = template;
    }

    public void sendMessage(OrderEvent orderEvent){

        LOGGER.info(String.format("Order event sent to RabbitMQ => %s",orderEvent.toString()));

        // send order event to order queue
        template.convertAndSend(exchange,orderRoutingKey,orderEvent);

        // send order event to email queue
        template.convertAndSend(exchange,emailRoutingKey,orderEvent);
    }
}
