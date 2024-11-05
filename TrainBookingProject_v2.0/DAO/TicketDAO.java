package DAO;

import java.util.*;
import DB_init.*;
import DataModel.*;

public class TicketDAO {
    private ArrayList<Ticket> table_ticket;

    public TicketDAO() {
        table_ticket = Database.getInstance().getTable_ticket();
    }

    public ArrayList<Ticket> getTable_ticket() {
        return table_ticket;
    }

    public boolean addTicket(Ticket ticket) {
        table_ticket.add(ticket);
        return true;
    }

}