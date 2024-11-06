import java.util.*;
import DataModel.*;

public class Main{
    public static void main(String[] args) {
        TrainTicketSystem train_ticket_system = TrainTicketSystem.getInstance();
        Scanner scanner = new Scanner(System.in);
        User current_LoginedUser = null;

        // Main menu
        // Scenario: current_LoginedUser == null
        while (current_LoginedUser == null) {
            System.out.println("Welcome to Train Ticket System!");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Please select an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();


            switch (option) {
                case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    current_LoginedUser = train_ticket_system.login(username, password);
                    train_ticket_system.setCurrentUser(current_LoginedUser);
                    // System.out.print("\nWelcome " + current_LoginedUser.getUsername() + "!");
                    
                    break;

                case 2:
                    System.out.print("Registeraion Form:\nEnter username: ");
                    String register_username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String register_pwd = scanner.nextLine();

                    boolean result = train_ticket_system.register(register_username, register_pwd);
                    if (result) {
                        System.out.println("Register successfully.");
                    } else {
                        System.out.println("Username already exists.");
                    }
            
                    System.out.println("");
                    break;

                case 3:
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        // while loop for normal user   
        while (current_LoginedUser.getRole() == "normal") {
        	
            // display finished orders
            train_ticket_system.displayFinishedOrders(current_LoginedUser.getId(), scanner);
            
        	System.out.println();
            train_ticket_system.displayMainMenu();
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
					train_ticket_system.orderTicket(scanner);
                    break;

                case 2:
                    train_ticket_system.checkTicket(scanner);
                    break;

                case 3:
                    break;

                case 4:
                    break;

                case 5:
                    break;

                //CS function
                case 6:
                    train_ticket_system.cs(scanner);
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        while (current_LoginedUser.getRole() == "admin") {
            System.out.println("1. Manage Train Schedule");
                System.out.println("2. View Reports");
                System.out.println("3. Manage Users");
                System.out.println("4. Edit Profile");
                System.out.println("5. Logout");
                System.out.println("6. Add Keyword and Answer");
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        System.out.println("Manage Train Schedule");
                        // ... Implement Manage Train Schedule functionality
                        manageTrainSchedule(train_ticket_system, scanner);// Corrected method call
                        break;
                    case 2:
                        System.out.println("View Reports");
               
                        break;
                    case 4:
                        System.out.println("Edit Profile");
                        // ... Implement Edit Profile functionality
                        break;
                    case 5:
                        // Logout
                        current_LoginedUser = null;
                        System.out.println("Logged out successfully.\n");
                        break;
                    case 6:
                        // Add Keyword and Answer
                        boolean isStay = true;
                        System.out.println("\n=============================================================================================================");
                        while(isStay){
                            boolean isKeyword = false;
                            boolean isAnswer = false;
                            String answer = null;
                            String keyword = null;
                            while(!isKeyword){
                                System.out.println("Enter keyword:");
                                keyword = scanner.nextLine();
                                if(keyword.isEmpty()){
                                    System.out.println("Keyword cannot be empty.");
                                } else if(keyword.equalsIgnoreCase("exit")){
                                    isStay = false;
                                } else{
                                    isKeyword = true;
                                }
                            }
                            while(!isAnswer){
                                System.out.println("Enter answer:");
                                answer = scanner.nextLine();
                                if(answer.isEmpty()){
                                    System.out.println("Answer cannot be empty.");
                                } else if(answer.equalsIgnoreCase("exit")){
                                    isStay = false;
                                } else{
                                    isAnswer = true;
                                }
                            }
                            if (isKeyword && isAnswer) { // Ensure both keyword and answer are provided
                                System.out.println(train_ticket_system.addQA(keyword, answer));
                            }
                        }
                        System.out.println("\n=============================================================================================================");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }

        private static void manageTrainSchedule(TrainTicketSystem system, Scanner scanner) {
            boolean managing = true;
            while (managing) {
                System.out.println("\n--- Manage Train Schedule ---");
                System.out.println("1. Add Train");
                System.out.println("2. Remove Train");
                System.out.println("3. Update Train");
                System.out.println("4. View All Trains");
                System.out.println("5. Back to Admin Menu");
                System.out.print("Choose an option: ");
                int choice;
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 5.");
                    scanner.nextLine(); // Clear invalid input
                    continue;
                }
    
                switch (choice) {
                    case 1:
                        addTrain(system, scanner);
                        break;
    
                    case 2:
                        removeTrain(system, scanner);
                        break;
    
                    case 3:
                        updateTrain(system, scanner);
                        break;
    
                    case 4:
                        System.out.println("\n--- All Available Trains ---");
                        system.displayTrains_available();
                        break;
    
                    case 5:
                        managing = false;
                        break;
    
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
    
        private static void addTrain(TrainTicketSystem system, Scanner scanner) {
            System.out.println("\n--- Add a New Train ---");
            System.out.print("Enter Train ID: ");
            String trainID = scanner.nextLine();
    
            Train existingTrain = system.getTrainByNumber(trainID);
            if (existingTrain != null) {
                System.out.println("Train ID already exists. Please use a different ID.");
                return;
            }
    
            System.out.print("Enter Departure Station: ");
            String departure = scanner.nextLine();
    
            System.out.print("Enter Arrival Station: ");
            String arrival = scanner.nextLine();
    
            System.out.print("Enter Departure Date (YYYY-MM-DD): ");
            String date = scanner.nextLine();
    
            System.out.print("Enter Departure Time (HH:MM): ");
            String time = scanner.nextLine();
    
            System.out.print("Enter Total Seats: ");
            int totalSeats;
            try {
                totalSeats = Integer.parseInt(scanner.nextLine());
                if (totalSeats <= 0) {
                    System.out.println("Total seats must be a positive number.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format for seats.");
                return;
            }
    
            System.out.print("Enter Ticket Price: ");
            double price;
            try {
                price = Double.parseDouble(scanner.nextLine());
                if (price < 0) {
                    System.out.println("Price cannot be negative.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format for price.");
                return;
            }
    
            Train newTrain = new Train(trainID, departure, arrival, date, time, totalSeats, price);
            system.addTrain(newTrain);
            System.out.println("Train added successfully.");
        }
    
     
        private static void removeTrain(TrainTicketSystem system, Scanner scanner) {
            System.out.println("\n--- Remove an Existing Train ---");
            System.out.print("Enter Train ID to Delete: ");
            String trainID = scanner.nextLine();
    
            boolean isDeleted = system.removeTrain(trainID);
            if (isDeleted) {
                System.out.println("Train deleted successfully.");
            } else {
                System.out.println("Train ID not found. Deletion failed.");
            }
        }
    
    
        private static void updateTrain(TrainTicketSystem system, Scanner scanner) {
            System.out.println("\n--- Update Train Details ---");
            System.out.print("Enter Train ID to Update: ");
            String trainID = scanner.nextLine();
    
            Train existingTrain = system.getTrainByNumber(trainID);
            if (existingTrain == null) {
                System.out.println("Train ID not found.");
                return;
            }
    
            System.out.println("Leave the field empty if you do not want to change it.");
    
            System.out.print("Enter New Departure Station (Current: " + existingTrain.getDeparture() + "): ");
            String departure = scanner.nextLine();
            if (!departure.isEmpty()) {
                existingTrain.setDeparture(departure);
            }
    
            System.out.print("Enter New Arrival Station (Current: " + existingTrain.getArrival() + "): ");
            String arrival = scanner.nextLine();
            if (!arrival.isEmpty()) {
                existingTrain.setArrival(arrival);
            }
    
            System.out.print("Enter New Departure Date (YYYY-MM-DD) (Current: " + existingTrain.getDate() + "): ");
            String date = scanner.nextLine();
            if (!date.isEmpty()) {
                existingTrain.setDate(date);
            }
    
            System.out.print("Enter New Departure Time (HH:MM) (Current: " + existingTrain.getTime() + "): ");
            String time = scanner.nextLine();
            if (!time.isEmpty()) {
                existingTrain.setTime(time);
            }
    
            System.out.print("Enter New Total Seats (Current: " + existingTrain.getAvailableSeats() + "): ");
            String totalSeatsStr = scanner.nextLine();
            if (!totalSeatsStr.isEmpty()) {
                try {
                    int totalSeats = Integer.parseInt(totalSeatsStr);
                    if (totalSeats > 0) {
                        existingTrain.setAvailableSeats(totalSeats);
                    } else {
                        System.out.println("Total seats must be a positive number. Skipping update for seats.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format for seats. Skipping update for seats.");
                }
            }
    
            System.out.print("Enter New Ticket Price (Current: " + existingTrain.getPrice() + "): ");
            String priceStr = scanner.nextLine();
            if (!priceStr.isEmpty()) {
                try {
                    double price = Double.parseDouble(priceStr);
                    if (price >= 0) {
                        existingTrain.setPrice(price);
                    } else {
                        System.out.println("Price cannot be negative. Skipping update for price.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format for price. Skipping update for price.");
                }
            }
    
            system.updateTrain(existingTrain);
            System.out.println("Train details updated successfully.");
        }
        
    
      
       
    }
