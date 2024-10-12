import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import java.text.ParseException;

// Model classes
class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

class Train {
    private String trainNumber;
    private String departure;
    private String arrival;
    private String date;
    private String time;
    private int availableSeats;
    private double price;

    public Train(String trainNumber, String departure, String arrival, String date, String time, int availableSeats, double price) {
        this.trainNumber = trainNumber;
        this.departure = departure;
        this.arrival = arrival;
        this.date = date;
        this.time = time;
        this.availableSeats = availableSeats;
        this.price = price;
    }

    // Getters and setters
    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }
    public String getDeparture() { return departure; }
    public void setDeparture(String departure) { this.departure = departure; }
    public String getArrival() { return arrival; }
    public void setArrival(String arrival) { this.arrival = arrival; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return String.format("Train %s: %s to %s on %s at %s, %d seats available, Price: $%.2f",
                trainNumber, departure, arrival, date, time, availableSeats, price);
    }
}

class Passenger {
    private String name;
    private int age;

    public Passenger(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    @Override
    public String toString() {
        return String.format("Passenger: %s, Age: %d", name, age);
    }
}

abstract class Ticket {
    protected String username;
    protected Train train;
    protected List<Passenger> passengers;
    protected double totalPrice;
    protected int rating;

    public Ticket(String username, Train train, List<Passenger> passengers, double totalPrice) {
        this.username = username;
        this.train = train;
        this.passengers = passengers;
        this.totalPrice = totalPrice;
        this.rating = 0;
    }

    public abstract String getTicketType();

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Train getTrain() { return train; }
    public void setTrain(Train train) { this.train = train; }
    public List<Passenger> getPassengers() { return passengers; }
    public void setPassengers(List<Passenger> passengers) { this.passengers = passengers; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Ticket Type: %s\n", getTicketType()));
        sb.append(String.format("Username: %s\n", username));
        sb.append(String.format("Train: %s\n", train));
        sb.append("Passengers:\n");
        for (Passenger p : passengers) {
            sb.append(p).append("\n");
        }
        sb.append(String.format("Total Price: $%.2f", totalPrice));
        return sb.toString();
    }
}

class RegularTicket extends Ticket {
    public RegularTicket(String username, Train train, List<Passenger> passengers, double totalPrice) {
        super(username, train, passengers, totalPrice);
    }

    @Override
    public String getTicketType() {
        return "Regular";
    }
}

class BusinessTicket extends Ticket {
    public BusinessTicket(String username, Train train, List<Passenger> passengers, double totalPrice) {
        super(username, train, passengers, totalPrice);
    }

    @Override
    public String getTicketType() {
        return "Business";
    }
}

// Factory Pattern
class TicketFactory {
    public static Ticket createTicket(String type, String username, Train train, List<Passenger> passengers, double totalPrice) {
        if (type.equalsIgnoreCase("regular")) {
            return new RegularTicket(username, train, passengers, totalPrice);
        } else if (type.equalsIgnoreCase("business")) {
            return new BusinessTicket(username, train, passengers, totalPrice);
        }
        return null;
    }
}

// Strategy Pattern
interface PaymentStrategy {
    boolean processPayment(double amount);
}

class CreditCardPayment implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing credit card payment of $" + amount);
        // Simulating payment processing
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return Math.random() < 0.9; // 90% success rate
    }
}

class PayPalPayment implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing PayPal payment of $" + amount);
        // Simulating payment processing
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return Math.random() < 0.95; // 95% success rate
    }
}

// Singleton Pattern
class Database {
    private static Database instance;
    private static final String USER_FILE = "users.txt";
    private static final String TRAIN_FILE = "trains.txt";
    private static final String TICKET_FILE = "tickets.txt";

    private Database() {}

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // Database operations (read, write, update) for users, trains, and tickets
}

// DAO Pattern
interface UserDAO {
    boolean addUser(User user);
    User getUser(String username);
    boolean updateUser(User user);
}

class UserFileDAO implements UserDAO {
    private static final String USER_FILE = "users.txt";

    @Override
    public boolean addUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            writer.write(user.getUsername() + "," + user.getPassword() + "\n");
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to user file.");
            return false;
        }
    }

    @Override
    public User getUser(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    return new User(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file.");
        }
        return null;
    }

    @Override
    public boolean updateUser(User user) {
        List<String> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(user.getUsername())) {
                    users.add(user.getUsername() + "," + user.getPassword());
                } else {
                    users.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file.");
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (String u : users) {
                writer.write(u + "\n");
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to user file.");
            return false;
        }
    }
}

// Controller
class TrainTicketSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static String currentUser = null;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static UserDAO userDAO = new UserFileDAO();
    private static List<Train> trains = new ArrayList<>();
    private static List<Ticket> tickets = new ArrayList<>();

    private static final String TRAINS_FILE = "trains.txt";
    private static final String TICKETS_FILE = "tickets.txt";

    public static void main(String[] args) {
        loadTrains();
        loadTickets();
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private static void showLoginMenu() {
        System.out.println("\n1. Login\n2. Register\n3. Exit");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
                System.out.println("Goodbye!");
                System.exit(0);
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void showMainMenu() {
    	// filter tickets based on user
		List<Ticket> userTickets = tickets.stream().filter(ticket -> ticket.getUsername().equals(currentUser) && ticket.getRating() == 0).collect(Collectors.toList());
    	
    	// check if user has finished their ticket
    	List<Ticket> usedTickets = userTickets.stream()
		    .filter(ticket -> {
		        try {
		            return new SimpleDateFormat("yyyy-MM-dd").parse(ticket.getTrain().getDate()).before(new Date());
		        } catch (ParseException e) {
		            e.printStackTrace();
		            return false;
		        }
		    })
		    .collect(Collectors.toList());
		
    	// ask user for ratings
		if (!usedTickets.isEmpty()) {
		    System.out.println("\nPlease rate your journies!");
			for (int i = 0; i < usedTickets.size(); i++) {
				Ticket ticket = usedTickets.get(i);
				System.out.println("-------------------------------------------------------------------------------------------------");
				System.out.println((i + 1) + ". " + ticket.toString());
				System.out.print("\nRating (1-5, 1 is lowest, 5 is highest): ");
				
				int rating = scanner.nextInt();
				ticket.setRating(rating);
				
				// update ticket file
				updateTicketInFile(ticket);
			}
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println("Thank you for your rating!");
		}
    	
        System.out.println("\n1. Order Ticket\n2. Check Tickets\n3. Edit Ticket\n4. Cancel Ticket\n5. Edit Profile\n6. Logout");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                orderTicket();
                break;
            case 2:
                checkTickets();
                break;
            case 3:
                editTicket();
                break;
            case 4:
                cancelTicket();
                break;
            case 5:
                editProfile();
                break;
            case 6:
                currentUser = null;
                System.out.println("Logged out successfully.");
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = userDAO.getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = username;
            System.out.println("Login successful.");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User newUser = new User(username, password);
        if (userDAO.addUser(newUser)) {
            System.out.println("Registration successful.");
        } else {
            System.out.println("Registration failed.");
        }
    }

    private static void loadTrains() {
        trains.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRAINS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    Train train = new Train(
                        parts[0], // trainNumber
                        parts[1], // departure
                        parts[2], // arrival
                        parts[3], // date
                        parts[4], // time
                        Integer.parseInt(parts[5]), // availableSeats
                        Double.parseDouble(parts[6]) // price
                    );
                    trains.add(train);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading trains file: " + e.getMessage());
        }
    }

    private static void loadTickets() {
        tickets.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(TICKETS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    String username = parts[0];
                    String trainNumber = parts[1];
                    String departure = parts[2];
                    String arrival = parts[3];
                    String date = parts[4];
                    String time = parts[5];
                    double totalPrice = Double.parseDouble(parts[6]);
                    int rating = Integer.parseInt(parts[7]);
                    
                    Train train = new Train(trainNumber, departure, arrival, date, time, 0, 0);
                    List<Passenger> passengers = new ArrayList<>();
                    
                    String[] passengerInfo = parts[8].split(";");
                    for (String info : passengerInfo) {
                        String[] pDetails = info.split(":");
                        if (pDetails.length == 2) {
                            passengers.add(new Passenger(pDetails[0], Integer.parseInt(pDetails[1])));
                        }
                    }
                    
                    Ticket ticket = TicketFactory.createTicket("regular", username, train, passengers, totalPrice);
                    tickets.add(ticket);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading tickets file: " + e.getMessage());
        }
    }

    private static void saveTicket(Ticket ticket) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TICKETS_FILE, true))) {
            StringBuilder sb = new StringBuilder();
            sb.append(ticket.getUsername()).append(",");
            sb.append(ticket.getTrain().getTrainNumber()).append(",");
            sb.append(ticket.getTrain().getDeparture()).append(",");
            sb.append(ticket.getTrain().getArrival()).append(",");
            sb.append(ticket.getTrain().getDate()).append(",");
            sb.append(ticket.getTrain().getTime()).append(",");
            sb.append(ticket.getTotalPrice()).append(",");
            sb.append(ticket.getRating()).append(",");
            
            for (int i = 0; i < ticket.getPassengers().size(); i++) {
                Passenger p = ticket.getPassengers().get(i);
                sb.append(p.getName()).append(":").append(p.getAge());
                if (i < ticket.getPassengers().size() - 1) {
                    sb.append(";");
                }
            }
            
            writer.write(sb.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving ticket: " + e.getMessage());
        }
    }

    private static void orderTicket() {
    	List<Train> availableTrains = new ArrayList<Train>();
    	
    	// check if user has preferences
    	System.out.println("Do you have any preferences? (yes/no)");
    	String preferences = scanner.nextLine();
    	
		if (preferences.equals("yes")) {
			System.out.println("Enter departure:");
			String departure = scanner.next();
	
			System.out.println("Enter arrival:");
			String arrival = scanner.next();
	
			System.out.println("Enter date (yyyy-MM-dd):");
			String date = scanner.next();
	
			trains = trains.stream().filter(t -> t.getDeparture().equals(departure) && t.getArrival().equals(arrival)
					&& t.getDate().equals(date)).collect(Collectors.toList());
			
		} else if (preferences.equals("no")){
			availableTrains = trains;
			
		} else {
			System.out.println("Invalid choice.");
	        	return;
		}
			
	        System.out.println("Available trains:");
		if (availableTrains.isEmpty()) {
			System.out.println("No trains found.");
			return;
		}
	
        for (int i = 0; i < availableTrains.size(); i++) {
            System.out.println((i + 1) + ". " + availableTrains.get(i));
        }

        System.out.print("Select a train (enter number): ");
        int trainChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (trainChoice < 1 || trainChoice > availableTrains.size()) {
            System.out.println("Invalid train selection.");
            return;
        }

        Train selectedTrain = availableTrains.get(trainChoice - 1);

        System.out.print("Enter number of passengers: ");
        int passengerCount = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        List<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < passengerCount; i++) {
            System.out.println("Passenger " + (i + 1) + ":");
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            passengers.add(new Passenger(name, age));
        }

        System.out.print("Select ticket type (1. Regular, 2. Business): ");
        int ticketTypeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String ticketType = (ticketTypeChoice == 1) ? "regular" : "business";
        double totalPrice = selectedTrain.getPrice() * passengerCount;
        if (ticketType.equals("business")) {
            totalPrice *= 1.5; // 50% more expensive for business class
        }

        System.out.println("Total price: $" + totalPrice);

        System.out.print("Select payment method (1. Credit Card, 2. PayPal): ");
        int paymentChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        PaymentStrategy paymentStrategy = (paymentChoice == 1) ? new CreditCardPayment() : new PayPalPayment();

        if (paymentStrategy.processPayment(totalPrice)) {
            Ticket newTicket = TicketFactory.createTicket(ticketType, currentUser, selectedTrain, passengers, totalPrice);
            tickets.add(newTicket);
            selectedTrain.setAvailableSeats(selectedTrain.getAvailableSeats() - passengerCount);
            saveTicket(newTicket);
            System.out.println("Ticket booked successfully!");
        } else {
            System.out.println("Payment failed. Ticket not booked.");
        }
    }

    private static void checkTickets() {
        List<Ticket> userTickets = tickets.stream()
                .filter(t -> t.getUsername().equals(currentUser))
                .toList();

        if (userTickets.isEmpty()) {
            System.out.println("You have no tickets.");
        } else {
            System.out.println("Your tickets:");
            for (int i = 0; i < userTickets.size(); i++) {
                System.out.println((i + 1) + ". " + userTickets.get(i));
            }
        }
    }

    private static void editTicket() {
    List<Ticket> userTickets = getUserTickets();
    if (userTickets.isEmpty()) {
        System.out.println("You have no tickets to edit.");
        return;
    }

    System.out.println("Your tickets:");
    for (int i = 0; i < userTickets.size(); i++) {
        System.out.println((i + 1) + ". " + userTickets.get(i));
    }

    System.out.print("Select a ticket to edit (enter number): ");
    int ticketChoice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    if (ticketChoice < 1 || ticketChoice > userTickets.size()) {
        System.out.println("Invalid ticket selection.");
        return;
    }

    Ticket selectedTicket = userTickets.get(ticketChoice - 1);

    System.out.println("What would you like to edit?");
    System.out.println("1. Change train");
    System.out.println("2. Edit passengers");
    System.out.print("Enter your choice: ");
    int editChoice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    switch (editChoice) {
        case 1:
            changeTrain(selectedTicket);
            break;
        case 2:
            editPassengers(selectedTicket);
            break;
        default:
            System.out.println("Invalid choice.");
    }
}

private static void changeTrain(Ticket ticket) {
    System.out.println("Available trains:");
    for (int i = 0; i < trains.size(); i++) {
        System.out.println((i + 1) + ". " + trains.get(i));
    }

    System.out.print("Select a new train (enter number): ");
    int trainChoice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    if (trainChoice < 1 || trainChoice > trains.size()) {
        System.out.println("Invalid train selection.");
        return;
    }

    Train newTrain = trains.get(trainChoice - 1);
    
    // Update available seats for old and new train
    ticket.getTrain().setAvailableSeats(ticket.getTrain().getAvailableSeats() + ticket.getPassengers().size());
    newTrain.setAvailableSeats(newTrain.getAvailableSeats() - ticket.getPassengers().size());

    // Update ticket
    ticket.setTrain(newTrain);
    ticket.setTotalPrice(newTrain.getPrice() * ticket.getPassengers().size());

    updateTicketInFile(ticket);
    System.out.println("Ticket updated successfully.");
}

private static void editPassengers(Ticket ticket) {
    System.out.println("Current passengers:");
    List<Passenger> passengers = ticket.getPassengers();
    for (int i = 0; i < passengers.size(); i++) {
        System.out.println((i + 1) + ". " + passengers.get(i));
    }

    System.out.println("1. Add a passenger");
    System.out.println("2. Remove a passenger");
    System.out.println("3. Edit a passenger");
    System.out.print("Enter your choice: ");
    int choice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    switch (choice) {
        case 1:
            addPassenger(ticket);
            break;
        case 2:
            removePassenger(ticket);
            break;
        case 3:
            editPassenger(ticket);
            break;
        default:
            System.out.println("Invalid choice.");
    }
}

private static void addPassenger(Ticket ticket) {
    System.out.print("Enter passenger name: ");
    String name = scanner.nextLine();
    System.out.print("Enter passenger age: ");
    int age = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    Passenger newPassenger = new Passenger(name, age);
    ticket.getPassengers().add(newPassenger);
    ticket.setTotalPrice(ticket.getTotalPrice() + ticket.getTrain().getPrice());
    ticket.getTrain().setAvailableSeats(ticket.getTrain().getAvailableSeats() - 1);

    updateTicketInFile(ticket);
    System.out.println("Passenger added successfully.");
}

private static void removePassenger(Ticket ticket) {
    List<Passenger> passengers = ticket.getPassengers();
    System.out.println("Select a passenger to remove:");
    for (int i = 0; i < passengers.size(); i++) {
        System.out.println((i + 1) + ". " + passengers.get(i));
    }

    System.out.print("Enter passenger number: ");
    int passengerChoice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    if (passengerChoice < 1 || passengerChoice > passengers.size()) {
        System.out.println("Invalid passenger selection.");
        return;
    }

    passengers.remove(passengerChoice - 1);
    ticket.setTotalPrice(ticket.getTotalPrice() - ticket.getTrain().getPrice());
    ticket.getTrain().setAvailableSeats(ticket.getTrain().getAvailableSeats() + 1);

    updateTicketInFile(ticket);
    System.out.println("Passenger removed successfully.");
}

private static void editPassenger(Ticket ticket) {
    List<Passenger> passengers = ticket.getPassengers();
    System.out.println("Select a passenger to edit:");
    for (int i = 0; i < passengers.size(); i++) {
        System.out.println((i + 1) + ". " + passengers.get(i));
    }

    System.out.print("Enter passenger number: ");
    int passengerChoice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    if (passengerChoice < 1 || passengerChoice > passengers.size()) {
        System.out.println("Invalid passenger selection.");
        return;
    }

    Passenger selectedPassenger = passengers.get(passengerChoice - 1);

    System.out.print("Enter new name (or press enter to keep current): ");
    String newName = scanner.nextLine();
    if (!newName.isEmpty()) {
        selectedPassenger.setName(newName);
    }

    System.out.print("Enter new age (or -1 to keep current): ");
    int newAge = scanner.nextInt();
    scanner.nextLine(); // Consume newline
    if (newAge != -1) {
        selectedPassenger.setAge(newAge);
    }

    updateTicketInFile(ticket);
    System.out.println("Passenger updated successfully.");
}

private static void cancelTicket() {
    List<Ticket> userTickets = getUserTickets();
    if (userTickets.isEmpty()) {
        System.out.println("You have no tickets to cancel.");
        return;
    }

    System.out.println("Your tickets:");
    for (int i = 0; i < userTickets.size(); i++) {
        System.out.println((i + 1) + ". " + userTickets.get(i));
    }

    System.out.print("Select a ticket to cancel (enter number): ");
    int ticketChoice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    if (ticketChoice < 1 || ticketChoice > userTickets.size()) {
        System.out.println("Invalid ticket selection.");
        return;
    }

    Ticket selectedTicket = userTickets.get(ticketChoice - 1);

    // Update available seats
    selectedTicket.getTrain().setAvailableSeats(selectedTicket.getTrain().getAvailableSeats() + selectedTicket.getPassengers().size());

    // Remove ticket from the list
    tickets.remove(selectedTicket);

    // Update tickets file
    updateTicketsFile();

    System.out.println("Ticket cancelled successfully.");
    System.out.println("Refund amount: $" + (selectedTicket.getTotalPrice() * 0.9)); // 90% refund
}

private static List<Ticket> getUserTickets() {
    return tickets.stream()
            .filter(t -> t.getUsername().equals(currentUser))
            .collect(Collectors.toList());
}

private static void updateTicketInFile(Ticket updatedTicket) {
    updateTicketsFile();
}

private static void updateTicketsFile() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(TICKETS_FILE))) {
        for (Ticket ticket : tickets) {
            StringBuilder sb = new StringBuilder();
            sb.append(ticket.getUsername()).append(",");
            sb.append(ticket.getTrain().getTrainNumber()).append(",");
            sb.append(ticket.getTrain().getDeparture()).append(",");
            sb.append(ticket.getTrain().getArrival()).append(",");
            sb.append(ticket.getTrain().getDate()).append(",");
            sb.append(ticket.getTrain().getTime()).append(",");
            sb.append(ticket.getTotalPrice()).append(",");
            sb.append(ticket.getRating()).append(",");
            
            for (int i = 0; i < ticket.getPassengers().size(); i++) {
                Passenger p = ticket.getPassengers().get(i);
                sb.append(p.getName()).append(":").append(p.getAge());
                if (i < ticket.getPassengers().size() - 1) {
                    sb.append(";");
                }
            }
            
            writer.write(sb.toString());
            writer.newLine();
        }
    } catch (IOException e) {
        System.out.println("Error updating tickets file: " + e.getMessage());
    }
}

    private static void editProfile() {
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();

        User user = userDAO.getUser(currentUser);
        if (user != null) {
            user.setPassword(newPassword);
            if (userDAO.updateUser(user)) {
                System.out.println("Profile updated successfully.");
            } else {
                System.out.println("Failed to update profile.");
            }
        } else {
            System.out.println("User not found.");
        }
    }
}
