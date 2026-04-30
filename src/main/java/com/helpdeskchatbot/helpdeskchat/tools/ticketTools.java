package com.helpdeskchatbot.helpdeskchat.tools;

import com.helpdeskchatbot.helpdeskchat.entity.Ticket;
import com.helpdeskchatbot.helpdeskchat.service.TicketService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class ticketTools {
    private final TicketService ticketService;

    public ticketTools(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Tool(description = "this help to create new ticket in the db")
    public Ticket createTicketTools(@ToolParam(description = "Ticket details") Ticket ticket) {
        return ticketService.ticketCreate(ticket);
    }

    @Tool(description = "this help to the ticket in the db")
    public Ticket getTicketByUSerName(@ToolParam(description = "username of the ticket owner") String username) {
        return ticketService.getTicketByUsername(username);
    }

    @Tool(description = "this help to update the ticket")
    public Ticket updateTicket(@ToolParam(description = "Ticket details") Ticket ticket) {
        return ticketService.ticketUpdate(ticket);
    }

    //get current system time
    @Tool(description = "this help to get the current system time")
    public String getCurrentTime() {
        return java.time.LocalDateTime.now().toString();
    }

}
