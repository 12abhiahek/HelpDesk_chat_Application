package com.helpdeskchatbot.helpdeskchat.service;

import com.helpdeskchatbot.helpdeskchat.entity.Ticket;
import com.helpdeskchatbot.helpdeskchat.repository.TicketRepository;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    //create ticket
    public Ticket ticketCreate(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    // update ticket
    public Ticket ticketUpdate(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    // delete ticket
    public void deleteTicket(Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }

    // g

    // get ticket by username
    public Ticket getTicketByUsername(String username) {
        return ticketRepository.findByUsername(username);
    }

    // get ticket by ticket id
    public  Ticket getTicketByTicketId(Long ticketId) {
        return ticketRepository.findByTicketId(ticketId);
    }
}
