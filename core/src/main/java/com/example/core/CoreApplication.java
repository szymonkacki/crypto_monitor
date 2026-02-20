package com.example.core;

import jakarta.persistence.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@RestController
public class CoreApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(CoreApplication.class, args);
	}

	@Autowired
	private PriceRepository priceRepository;

	@Autowired
	private AlertRepository alertRepository;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Bean
	public Queue queueCore()
	{
		return new Queue("crypto-queue-core", false);
	}

	@Bean
	public Queue queueNotification()
	{
		return new Queue("crypto-queue-notification", false);
	}

	@RabbitListener(queues = "crypto-queue-core")
	public void receiveMessage(String jsonMessage)
	{
		try
		{
			CryptoPriceDTO dto = objectMapper.readValue(jsonMessage, CryptoPriceDTO.class);

			System.out.println(" [Core] Odebrano: " + dto.getSymbol() + " = " + dto.getPrice());

			PriceEntity entity = new PriceEntity();
			entity.setCurrency(dto.getSymbol());
			entity.setPrice(dto.getPrice());
			entity.setTimestamp(LocalDateTime.now());
			priceRepository.save(entity);

			List<AlertEntity> alerts = alertRepository.findAll();
			for (AlertEntity alert : alerts)
			{
				if (dto.getPrice() > alert.getLimitValue())
				{
					String tresc = "UWAGA! Waluta " + dto.getSymbol() +
							" osiągnęła cenę " + dto.getPrice() +
							" (Ustawiony próg: " + alert.getLimitValue() + ")";

					AlertDTO alertDto = new AlertDTO(tresc, dto.getPrice());
					String jsonAlert = objectMapper.writeValueAsString(alertDto);

					rabbitTemplate.convertAndSend("crypto-queue-notification", jsonAlert);
					System.out.println(" [Core -> Notification] Wysłano alert dla progu: " + alert.getLimitValue());
				}
			}

		}
		catch (Exception e)
		{
			System.err.println("Błąd: " + e.getMessage());
		}
	}

	@GetMapping("/api/prices")
	public List<PriceEntity> getPrices()
	{
		return priceRepository.findAll();
	}
}

@Entity
class PriceEntity
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	private String currency;
	public Double price;
	public LocalDateTime timestamp;

	public Long getId() { return id; }
	public String getCurrency() { return currency; }       // Getter
	public void setCurrency(String currency) { this.currency = currency; } // Setter
	public Double getPrice() { return price; }
	public void setPrice(Double price) { this.price = price; }
	public LocalDateTime getTimestamp() { return timestamp; }
	public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

}

@Entity
class AlertEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Double limitValue;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Double getLimitValue() { return limitValue; }
	public void setLimitValue(Double limitValue) { this.limitValue = limitValue; }
}

@Repository
interface PriceRepository extends JpaRepository<PriceEntity, Long> {}

@Repository
interface AlertRepository extends JpaRepository<AlertEntity, Long> {}

