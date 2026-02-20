package com.example.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController
{
	@Autowired
	private AlertRepository alertRepository;

	@GetMapping
	public List<AlertEntity> getAlerts()
	{
		return alertRepository.findAll();
	}

	@PostMapping
	public AlertEntity createAlert(@RequestBody AlertEntity alert)
	{
		return alertRepository.save(alert);
	}

	@DeleteMapping("/{id}")
	public void deleteAlert(@PathVariable Long id)
	{
		alertRepository.deleteById(id);
	}

	@PutMapping("/{id}")
	public AlertEntity updateAlert(@PathVariable Long id, @RequestBody AlertEntity updatedAlert)
	{
		return alertRepository.findById(id)
				.map(alert -> {
					alert.setLimitValue(updatedAlert.getLimitValue());
					return alertRepository.save(alert);
				})
				.orElseThrow(() -> new RuntimeException("Alert nie znaleziony"));
	}
}
