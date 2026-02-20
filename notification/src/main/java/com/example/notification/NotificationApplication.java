package com.example.notification;

import tools.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NotificationApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(NotificationApplication.class, args);
	}

	@Bean
	public Queue queue()
	{
		return new Queue("crypto-queue-notification", false);
	}

	private final ObjectMapper objectMapper = new ObjectMapper();

	@RabbitListener(queues = "crypto-queue-notification")
	public void receive(String jsonMessage)
	{
		try
		{
			AlertDTO alert = objectMapper.readValue(jsonMessage, AlertDTO.class);

			System.err.println("=========================================");
			System.err.println(" [NOTIFICATION SERVICE] OTRZYMANO ALERT: ");
			System.err.println(" TREŚĆ: " + alert.getMessage());
			System.err.println(" CENA:  " + alert.getPrice());
			System.err.println("=========================================");

		}
		catch (Exception e)
		{
			System.out.println("Błąd: " + e.getMessage());
		}
	}
}