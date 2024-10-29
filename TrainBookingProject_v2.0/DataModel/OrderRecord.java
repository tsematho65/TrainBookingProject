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

    // Default constructor
    public OrderRecord() {}

    // Parameterized constructor
    public OrderRecord(int orderId, int userId, int trainId, Date orderDate, double amount, ArrayList<Passenger> passengerList, ArrayList<Ticket> ticketList) {
        this.orderId = orderId;
        this.userId = userId;
        this.trainId = trainId;
        this.orderDate = orderDate;
        this.amount = amount;
        this.passengerList = passengerList;
        this.ticketList = ticketList;
    }

    // Getters and setters
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
}