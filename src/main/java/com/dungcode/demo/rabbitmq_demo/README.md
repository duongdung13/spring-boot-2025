# RabbitMQ Demo

This is a sample implementation of RabbitMQ with Spring Boot AMQP.

## Prerequisites

- Java 21
- Spring Boot 3.2.3
- RabbitMQ Server (you can run it with Docker)

## Running RabbitMQ with Docker

```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

This will start RabbitMQ with the management console accessible at http://localhost:15672/ (username: guest, password: guest)

## Using the API

### Send a message to RabbitMQ

```bash
curl -X POST "http://localhost:8080/api/rabbitmq/publish?message=Hello%20RabbitMQ"
```

## Code Structure

1. `RabbitMQConfig.java` - Configuration for RabbitMQ with queue, exchange, and binding
2. `Message.java` - Message model class
3. `RabbitMQProducer.java` - Service to send messages to RabbitMQ
4. `RabbitMQConsumer.java` - Service to receive messages from RabbitMQ
5. `RabbitMQController.java` - REST controller to expose endpoints

## Implementation Notes

- Messages are automatically converted to JSON using Jackson2JsonMessageConverter
- The consumer logs messages when they are received
- The producer creates messages with unique IDs and timestamps 