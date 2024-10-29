package DataModel;

public class RegularTicket extends Ticket {
    private String ticketType;
    private double price;

    public RegularTicket(String ticketType, double price) {
        this.ticketType = ticketType;
        this.price = price;
    }

    // Getters and setters
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

    @Override
    public String toString() {
        return String.format("Regular Ticket: %s, Price: $%.2f", ticketType, price);
    }
}