package tambola.ticket.generator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import tambola.ticket.generator.service.TambolaService;

@RestController
public class TambolaController {
	
	@Autowired
	TambolaService tambolaService;

	@PostMapping("/generate")
	public String generateTickets(int n) {
		return tambolaService.generateTickets(n).toString();
	}
	
	@GetMapping("/all")
	public String getAllTickeets() {
		return tambolaService.getAllTickeets();
	}
}
