import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class TrainTicketSystem {
    private static final String USER_FILE = "users.txt";
    private static final String TRAIN_FILE = "trains.txt";
    private static final String TICKET_FILE = "tickets.txt";
    private static Scanner scanner = new Scanner(System.in);
    private static String currentUser = null;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        while (true) {
            if (currentUser == null) {
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
            } else {
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
        }
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    currentUser = username;
                    System.out.println("Login successful.");
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file.");
        }

        System.out.println("Invalid username or password.");
    }

    private static void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            writer.write(username + "," + password + "\n");
            System.out.println("Registration successful.");
        } catch (IOException e) {
            System.out.println("Error writing to user file.");
        }
    }

    private static void orderTicket() {
        System.out.println("Available locations:");
        List<String> locations = getLocations();
        for (int i = 0; i < locations.size(); i++) {
            System.out.println((i + 1) + ". " + locations.get(i));
        }

        System.out.print("Select departure location (number): ");
        int departureChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Select arrival location (number): ");
        int arrivalChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (departureChoice == arrivalChoice) {
            System.out.println("Departure and arrival locations cannot be the same.");
            return;
        }

        String departure = locations.get(departureChoice - 1);
        String arrival = locations.get(arrivalChoice - 1);

        Date travelDate = getValidTravelDate();
        if (travelDate == null) {
            return;
        }

        List<String[]> trains = readTrains(departure, arrival, travelDate);
        if (trains.isEmpty()) {
            System.out.println("No trains available for the selected route and date.");
            return;
        }

        System.out.println("Available trains:");
        for (int i = 0; i < trains.size(); i++) {
            String[] train = trains.get(i);
            System.out.printf("%d. Train %s - %s to %s - Date: %s - Departure: %s - Available seats: %s, Price: $%s\n", 
                              i + 1, train[0], train[1], train[2], train[3], train[4], train[5], train[6]);
        }

        System.out.print("Enter the number of the train you want to book: ");
        int trainChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (trainChoice < 1 || trainChoice > trains.size()) {
            System.out.println("Invalid train number.");
            return;
        }

        String[] selectedTrain = trains.get(trainChoice - 1);
        int availableSeats = Integer.parseInt(selectedTrain[5]);
        double price = Double.parseDouble(selectedTrain[6]);

        if (availableSeats == 0) {
            System.out.println("Sorry, this train is fully booked.");
            return;
        }

        System.out.print("How many seats do you want to book? ");
        int seatsToBook = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (seatsToBook > availableSeats) {
            System.out.println("Not enough seats available.");
            return;
        }

        List<String[]> passengers = new ArrayList<>();
        for (int i = 0; i < seatsToBook; i++) {
            System.out.printf("Enter details for passenger %d:\n", i + 1);
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Age: ");
            String age = scanner.nextLine();
            passengers.add(new String[]{name, age});
        }

        double totalPrice = seatsToBook * price;
        System.out.printf("Total price: $%.2f\n", totalPrice);
        System.out.print("Confirm booking (y/n)? ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            if (processPayment(totalPrice)) {
                selectedTrain[5] = String.valueOf(availableSeats - seatsToBook);
                updateTrains(trains);
                saveTicket(selectedTrain[0], selectedTrain[1], selectedTrain[2], selectedTrain[3], selectedTrain[4], passengers, totalPrice);
                System.out.println("Booking confirmed!");
            } else {
                System.out.println("Payment failed. Booking cancelled.");
            }
        } else {
            System.out.println("Booking cancelled.");
        }
    }

    private static Date getValidTravelDate() {
        Date travelDate = null;
        while (travelDate == null) {
            System.out.print("Enter travel date (YYYY-MM-DD): ");
            String dateStr = scanner.nextLine();
            try {
                travelDate = dateFormat.parse(dateStr);
                Date today = new Date();
                if (travelDate.before(today)) {
                    System.out.println("Travel date cannot be in the past. Please enter a future date.");
                    travelDate = null;
                }
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
        return travelDate;
    }

    private static void checkTickets() {
        List<String[]> tickets = readTickets();
        if (tickets.isEmpty()) {
            System.out.println("You have no tickets.");
            return;
        }

        System.out.println("Your tickets:");
        for (int i = 0; i < tickets.size(); i++) {
            String[] ticket = tickets.get(i);
            System.out.printf("%d. Train %s - %s to %s - Date: %s - Departure: %s - Total price: $%s\n", 
                              i + 1, ticket[1], ticket[2], ticket[3], ticket[4], ticket[5], ticket[6]);
            String[] passengers = ticket[7].split(";");
            for (int j = 0; j < passengers.length; j++) {
                String[] passenger = passengers[j].split(":");
                System.out.printf("   Passenger %d: %s, Age: %s\n", j + 1, passenger[0], passenger[1]);
            }
        }
    }

    private static void editTicket() {
        List<String[]> tickets = readTickets();
        if (tickets.isEmpty()) {
            System.out.println("You have no tickets to edit.");
            return;
        }

        System.out.println("Your tickets:");
        for (int i = 0; i < tickets.size(); i++) {
            String[] ticket = tickets.get(i);
            System.out.printf("%d. Train %s - %s to %s - Date: %s - Departure: %s\n", 
                              i + 1, ticket[1], ticket[2], ticket[3], ticket[4], ticket[5]);
        }

        System.out.print("Enter the number of the ticket you want to edit: ");
        int ticketChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (ticketChoice < 1 || ticketChoice > tickets.size()) {
            System.out.println("Invalid ticket number.");
            return;
        }

        String[] selectedTicket = tickets.get(ticketChoice - 1);
        System.out.println("1. Edit passenger information\n2. Change travel date");
        System.out.print("Choose an option: ");
        int editChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (editChoice) {
            case 1:
                editPassengerInfo(selectedTicket);
                break;
            case 2:
                changeTravelDate(selectedTicket);
                break;
            default:
                System.out.println("Invalid option.");
                return;
        }

        updateTickets(tickets);
        System.out.println("Ticket updated successfully.");
    }

    private static void editPassengerInfo(String[] ticket) {
        String[] passengers = ticket[7].split(";");
        System.out.println("Current passengers:");
        for (int i = 0; i < passengers.length; i++) {
            String[] passenger = passengers[i].split(":");
            System.out.printf("%d. %s, Age: %s\n", i + 1, passenger[0], passenger[1]);
        }

        System.out.print("Enter the number of the passenger you want to edit: ");
        int passengerChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (passengerChoice < 1 || passengerChoice > passengers.length) {
            System.out.println("Invalid passenger number.");
            return;
        }

        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new age: ");
        String newAge = scanner.nextLine();

        passengers[passengerChoice - 1] = newName + ":" + newAge;
        ticket[7] = String.join(";", passengers);
    }

    private static void changeTravelDate(String[] ticket) {
        Date newDate = getValidTravelDate();
        if (newDate == null) {
            return;
        }

        String[] train = readTrain(ticket[1], dateFormat.format(newDate));
        if (train == null) {
            System.out.println("No available train on the selected date.");
            return;
        }

        int requiredSeats = ticket[7].split(";").length;
        int availableSeats = Integer.parseInt(train[5]);
        if (requiredSeats > availableSeats) {
            System.out.println("Not enough seats available on the new date.");
            return;
        }

        ticket[4] = dateFormat.format(newDate);
        ticket[5] = train[4]; // Update departure time

        // Update train information
        train[5] = String.valueOf(availableSeats - requiredSeats);
        updateTrain(train);
    }

    private static void cancelTicket() {
        List<String[]> tickets = readTickets();
        if (tickets.isEmpty()) {
            System.out.println("You have no tickets to cancel.");
            return;
        }

        System.out.println("Your tickets:");
        for (int i = 0; i < tickets.size(); i++) {
            String[] ticket = tickets.get(i);
            System.out.printf("%d. Train %s - %s to %s - Date: %s - Departure: %s\n", 
                              i + 1, ticket[1], ticket[2], ticket[3], ticket[4], ticket[5]);
        }

        System.out.print("Enter the number of the ticket you want to cancel: ");
        int ticketChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (ticketChoice < 1 || ticketChoice > tickets.size()) {
            System.out.println("Invalid ticket number.");
            return;
        }

        String[] cancelledTicket = tickets.remove(ticketChoice - 1);
        updateTickets(tickets);

        // Update train information
        String[] train = readTrain(cancelledTicket[1], cancelledTicket[4]);
        if (train != null) {
            int cancelledSeats = cancelledTicket[7].split(";").length;
            int availableSeats = Integer.parseInt(train[5]);
            train[5] = String.valueOf(availableSeats + cancelledSeats);
            updateTrain(train);
        }

        System.out.println("Ticket cancelled successfully.");
    }

    private static void editProfile() {
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();

        List<String> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(currentUser)) {
                    users.add(currentUser + "," + newPassword);
                } else {
                    users.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (String user : users) {
                writer.write(user + "\n");
            }
            System.out.println("Profile updated successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to user file.");
        }
    }

    private static List<String> getLocations() {
        Set<String> locations = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRAIN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                locations.add(parts[1]);
                locations.add(parts[2]);
            }
        } catch (IOException e) {
            System.out.println("Error reading train file.");
        }
        return new ArrayList<>(locations);
    }

    private static List<String[]> readTrains(String departure, String arrival, Date travelDate) {
        List<String[]> trains = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRAIN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] train = line.split(",");
                if (train[1].equals(departure) && train[2].equals(arrival)) {
                    Date trainDate = dateFormat.parse(train[3]);
                    if (trainDate.equals(travelDate)) {
                        trains.add(train);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error reading train file.");
        }
        return trains;
    }

    private static String[] readTrain(String trainNumber, String date) {
        try (BufferedReader reader = new BufferedReader(new FileReader(TRAIN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] train = line.split(",");
                if (train[0].equals(trainNumber) && train[3].equals(date)) {
                    return train;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading train file.");
        }
        return null;
    }

    private static void updateTrains(List<String[]> trains) {
        List<String> allTrains = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRAIN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allTrains.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading train file.");
            return;
        }

        List<String> updatedTrains = new ArrayList<>();
        for (String train : allTrains) {
            String[] trainParts = train.split(",");
            boolean found = false;
            for (String[] updatedTrain : trains) {
                if (trainParts[0].equals(updatedTrain[0]) && trainParts[3].equals(updatedTrain[3])) {
                    updatedTrains.add(String.join(",", updatedTrain));
                    found = true;
                    break;
                }
            }
            if (!found) {
                updatedTrains.add(train);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRAIN_FILE))) {
            for (String train : updatedTrains) {
                writer.write(train + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating train file.");
        }
    }

    private static void updateTrain(String[] train) {
        List<String> trains = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRAIN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] trainParts = line.split(",");
                if (trainParts[0].equals(train[0]) && trainParts[3].equals(train[3])) {
                    trains.add(String.join(",", train));
                } else {
                    trains.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading train file.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRAIN_FILE))) {
            for (String t : trains) {
                writer.write(t + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating train file.");
        }
    }

    private static void saveTicket(String trainNumber, String departure, String arrival, String travelDate, String departureTime, List<String[]> passengers, double totalPrice) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TICKET_FILE, true))) {
            StringBuilder passengerInfo = new StringBuilder();
            for (String[] passenger : passengers) {
                passengerInfo.append(passenger[0]).append(":").append(passenger[1]).append(";");
            }
            passengerInfo.setLength(passengerInfo.length() - 1); // Remove last semicolon
            writer.write(String.format("%s,%s,%s,%s,%s,%s,%.2f,%s\n", currentUser, trainNumber, departure, arrival, travelDate, departureTime, totalPrice, passengerInfo));
        } catch (IOException e) {
            System.out.println("Error saving ticket.");
        }
    }

    private static List<String[]> readTickets() {
        List<String[]> tickets = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TICKET_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] ticket = line.split(",", 8);
                if (ticket[0].equals(currentUser)) {
                    tickets.add(ticket);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading ticket file.");
        }
        return tickets;
    }

    private static void updateTickets(List<String[]> tickets) {
        List<String> allTickets = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TICKET_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allTickets.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading ticket file.");
            return;
        }

        List<String> updatedTickets = new ArrayList<>();
        for (String ticket : allTickets) {
            String[] ticketParts = ticket.split(",", 2);
            if (!ticketParts[0].equals(currentUser)) {
                updatedTickets.add(ticket);
            }
        }

        for (String[] ticket : tickets) {
            updatedTickets.add(String.join(",", ticket));
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TICKET_FILE))) {
            for (String ticket : updatedTickets) {
                writer.write(ticket + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating ticket file.");
        }
    }

    private static boolean processPayment(double amount) {
        System.out.printf("Processing payment of $%.2f...\n", amount);
        // Simulating payment processing
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return Math.random() < 0.9; // 90% success rate
    }
}