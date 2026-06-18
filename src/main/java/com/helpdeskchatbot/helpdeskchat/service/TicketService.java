package com.helpdeskchatbot.helpdeskchat.service;

import com.helpdeskchatbot.helpdeskchat.entity.Ticket;
import com.helpdeskchatbot.helpdeskchat.exception.ResourceNotFoundException;
import com.helpdeskchatbot.helpdeskchat.repository.TicketRepository;
import com.helpdeskchatbot.helpdeskchat.tools.EmailTools;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@Slf4j
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EmailTools emailTools;

    public TicketService(TicketRepository ticketRepository, EmailTools emailTools) {
        this.ticketRepository = ticketRepository;
        this.emailTools = emailTools;
    }

// CREATE TICKET
@Transactional
public Ticket ticketCreate(Ticket ticket) {

    log.info("Creating new ticket for user: {}", ticket.getUsername());

        try {

            ticket.setTicketId(null);
            Ticket savedTicket = ticketRepository.save(ticket);

            log.info("Ticket created successfully with id: {}", savedTicket.getTicketId());

            // send confirmation email to the user; do not fail creation if email sending fails
            try {
                String subject = String.format("[Ticket #%d] %s - created", savedTicket.getTicketId(), savedTicket.getTitle());
                String message = String.format("Hello %s,\n\nYour ticket (id: %d) was created successfully.\nTitle: %s\nDescription: %s\n\nWe will contact you when there's an update.\n\nRegards,\nSupport Team",
                        savedTicket.getUsername(), savedTicket.getTicketId(), savedTicket.getTitle(), savedTicket.getDescription());

                // pass the created ticket id so the email log can reference the ticket
                emailTools.sendEmailToSupportTeam(savedTicket.getEmail(), subject, message, savedTicket.getTicketId());
                log.debug("Sent ticket creation email to {}", savedTicket.getEmail());
            } catch (Exception ex) {
                // Log the email failure; do not roll back ticket creation because of email problems
                log.error("Failed to send ticket creation email to {}: {}", ticket.getEmail(), ex.getMessage(), ex);
            }

            return savedTicket;

        } catch (Exception e) {

            log.error("Error while creating ticket", e);

            throw new RuntimeException("Unable to create ticket");
        }
}

    // UPDATE TICKET
    public Ticket ticketUpdate(Ticket ticket) {

        Ticket existingTicket = ticketRepository
                .findById(ticket.getTicketId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket not found with id: "
                                        + ticket.getTicketId()));

        existingTicket.setTitle(ticket.getTitle());
        existingTicket.setDescription(ticket.getDescription());
        existingTicket.setPriority(ticket.getPriority());
        existingTicket.setStatus(ticket.getStatus());
        existingTicket.setAssignedTo(ticket.getAssignedTo());

        log.info("Updating ticket id: {}", ticket.getTicketId());

        return ticketRepository.save(existingTicket);
    }

    // DELETE TICKET
    public void deleteTicket(Long ticketId) {

        Ticket existingTicket = ticketRepository
                .findById(ticketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket not found with id: "
                                        + ticketId));

        ticketRepository.delete(existingTicket);

        log.info("Deleted ticket id: {}", ticketId);
    }

    // GET BY USERNAME
    public Ticket getTicketByUsername(String username) {

        Ticket ticket = ticketRepository.findByUsername(username);

        if (ticket == null) {
            throw new ResourceNotFoundException(
                    "Ticket not found for username: " + username);
        }

        return ticket;
    }

    // GET BY EMAIL
    public Ticket getTicketByEmail(String email) {

        Ticket ticket = ticketRepository.findByEmail(email);

        if (ticket == null) {
            throw new ResourceNotFoundException(
                    "Ticket not found for email: " + email);
        }

        return ticket;
    }

    // GET BY TICKET ID
    public Ticket getTicketByTicketId(Long ticketId) {

        return ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket not found with id: "
                                        + ticketId));
    }
}
