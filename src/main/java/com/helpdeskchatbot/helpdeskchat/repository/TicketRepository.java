package com.helpdeskchatbot.helpdeskchat.repository;

import com.helpdeskchatbot.helpdeskchat.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
        Ticket findByTicketId(Long ticketId);
        Ticket findByUsername(String username);
        java.util.List<Ticket> findAllByUsername(String username);

//         Ticket deleteById(Ticket ticketId);
}
