package net.javaguides.orderservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.order.name}")
    private String orderQueue;

    @Value("${rabbitmq.queue.email.name}")
    private String emailQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.binding.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.binding.email.routing.key}")
    private String emailRoutingKey;

    // spring bean for queue - order queue
    @Bean
    public Queue orderQueue(){

        return new Queue(orderQueue);
    }

    @Bean
    public Queue emailQueue(){

        return new Queue(emailQueue);
    }

    // spring bean for exchange
    @Bean
    public TopicExchange exchange(){

        return new TopicExchange(exchange);
    }

    // spring bean for binding using routing key
    @Bean
    public Binding binding(){

        return BindingBuilder.bind(orderQueue())
                .to(exchange())
                .with(routingKey);
    }

    @Bean
    public Binding emailBinding(){

        return BindingBuilder.bind(emailQueue())
                .to(exchange())
                .with(emailRoutingKey);
    }

    // message converter
    @Bean
    public MessageConverter messageConverter(){

        return new Jackson2JsonMessageConverter();
    }

    // configure RabbitTemplate
    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory){

        RabbitTemplate template=new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());

        return template;
    }
}
