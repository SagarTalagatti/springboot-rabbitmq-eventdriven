package net.javaguides.orderservice.controller;

import net.javaguides.orderservice.dto.Order;
import net.javaguides.orderservice.dto.OrderEvent;
import net.javaguides.orderservice.publisher.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private OrderProducer producer;

    @Autowired
    public OrderController(OrderProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/orders")
    public String placeOrder(@RequestBody Order order){

        order.setOrderId(UUID.randomUUID().toString());

        OrderEvent orderEvent=new OrderEvent();
        orderEvent.setStatus("PENDING");
        orderEvent.setMessage("Order is in pending state");
        orderEvent.setOrder(order);

        producer.sendMessage(orderEvent);

        return "Order sent to RabbitMQ exchange...";
    }
}
