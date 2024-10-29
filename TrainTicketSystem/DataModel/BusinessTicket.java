package DataModel;

public class BusinessTicket extends Ticket {
    private String ticketType;
    private double price;
    private String loungeAccess;

    public BusinessTicket(String ticketType, double price, String loungeAccess) {
        this.ticketType = ticketType;
        this.price = price;
        this.loungeAccess = loungeAccess;
    }

  
    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLoungeAccess() {
        return loungeAccess;
    }

    public void setLoungeAccess(String loungeAccess) {
        this.loungeAccess = loungeAccess;
    }

    @Override
    public String toString() {
        return String.format("Business Ticket: %s, Price: $%.2f, Lounge Access: %s", ticketType, price, loungeAccess);
    }
}