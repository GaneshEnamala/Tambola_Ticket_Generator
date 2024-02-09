package tambola.ticket.generator.repositery;

import org.springframework.data.jpa.repository.JpaRepository;

import tambola.ticket.generator.model.Ticket;

public interface TicketRepositery extends JpaRepository<Ticket, Integer> {

}
