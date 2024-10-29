package DataModel;

import java.util.*;

public class OrderRecord {
    private int orderId;
    private int userId;
    private int trainId;
    private Date orderDate;
    private double amount;
    private ArrayList<Passenger> passengerList;
    private ArrayList<Ticket> ticketList;

    public OrderRecord() {}

  
    public OrderRecord(int orderId, int userId, int trainId, Date orderDate, double amount, ArrayList<Passenger> passengerList, ArrayList<Ticket> ticketList) {
        this.orderId = orderId;
        this.userId = userId;
        this.trainId = trainId;
        this.orderDate = orderDate;
        this.amount = amount;
        this.passengerList = passengerList;
        this.ticketList = ticketList;
    }


    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getTrainId() {
        return trainId;
    }
    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }
    public Date getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public ArrayList<Passenger> getPassengerList() {
        return passengerList;
    }
    public void setPassengerList(ArrayList<Passenger> passengerList) {
        this.passengerList = passengerList;
    }
    public ArrayList<Ticket> getTicketList() {
        return ticketList;
    }
    public void setTicketList(ArrayList<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Order ID: %d\nUser ID: %d\nTrain ID: %d\nOrder Date: %s\nTotal Amount: $%.2f\n", 
            orderId, userId, trainId, orderDate.toString(), amount));
        sb.append("Passengers:\n");
        for (Passenger p : passengerList) {
            sb.append(p.toString()).append("\n");
        }
        sb.append("Tickets:\n");
        for (Ticket t : ticketList) {
            sb.append(t.toString()).append("\n");
        }
        return sb.toString();
    }
}