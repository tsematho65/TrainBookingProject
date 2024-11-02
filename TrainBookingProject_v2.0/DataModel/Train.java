package DataModel;

public class Train {
    private String trainID;
    private String departure;
    private String arrival;
    private String date;
    private String time;
    private int availableSeats;
    private double price;
    private String status;

    public Train(String trainNumber, String departure, String arrival, String date, String time, int availableSeats,
            double price) {
        this.trainID = trainNumber;
        this.departure = departure;
        this.arrival = arrival;
        this.date = date;
        this.time = time;
        this.availableSeats = availableSeats;
        this.price = price;
        this.status = "active";
    }

    // Getters and setters
    public String getTrainNumber() {
        return trainID;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainID = trainNumber;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%s, From: %s to %s on %s at %s, seats available: %d, Price: $%.2f",
                trainID, departure, arrival, date, time, availableSeats, price);
    }
}