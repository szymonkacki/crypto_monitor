package com.example.fetcher;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
public class FetcherApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(FetcherApplication.class, args);
	}

	@Autowired
	private RabbitTemplate rabbitTemplate;

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final RestTemplate restTemplate = new RestTemplate();

	@Bean
	public Queue queueCore()
	{
		return new Queue("crypto-queue-core", false);
	}

	@Scheduled(fixedRate = 2000) //in ms
	public void fetchAndSend()
	{
		String url = "https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT";

		try
		{
			Map response = restTemplate.getForObject(url, Map.class);

			if (response != null && response.get("price") != null)
			{
				String priceStr = response.get("price").toString();
				Double price = Double.valueOf(priceStr);

				CryptoPriceDTO dto = new CryptoPriceDTO("BTC", price);
				String jsonMessage = objectMapper.writeValueAsString(dto);

				rabbitTemplate.convertAndSend("crypto-queue-core", jsonMessage);

				System.out.println(" [Fetcher] Wysłano: " + jsonMessage);
			}
		}
		catch (Exception e)
		{
			System.out.println("Błąd pobierania: " + e.getMessage());
		}
	}
}