import java.util.*;
import DB_init.*;
import DataModel.*;
import DAO.*;

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
            System.out.println("1. Order Ticket");
            System.out.println("2. Check Tickets");
            System.out.println("3. Edit Ticket");
            System.out.println("4. Cancel Ticket");
            System.out.println("5. Edit Profile");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.println("Order Ticket:");
                    System.out.println("\nDo you have any preferences? (Y/N)");     // rating, preferences implementation
                    String preferences = scanner.nextLine();

                    if (preferences.equals("Y")) {
                        System.out.println("Enter departure:");
                        String departure = scanner.nextLine();
                
                        System.out.println("Enter arrival:");
                        String arrival = scanner.nextLine();
                
                        System.out.println("Enter date (yyyy-MM-dd):");
                        String date = scanner.nextLine();
                
                        // ...
                        
                    } else if (preferences.equals("N")){
                        // ...
                        
                    } else {
                        System.out.println("Invalid choice.");
                        return;
                    }
                    
                    // insert train recommendations
                    // if (tickets.size() > 0) {	
                    //     System.out.println("\n------------------------------------------------------------------------------------------------------");
                    //     System.out.println("Best trains:");
			
			        // calculate average rating for each train
                    // ...

                    // sort train recommendations by average rating
                    // recommendations.sort(Comparator.comparingDouble(Recommendation::getAverageRating).reversed());
                    
                    // display train recommendations
                    
                    // Base Case: No train recommendations
                    System.out.println("\n------------------------------------------------------------------------------------------------------");
                    System.out.println("Available trains:");
                    train_ticket_system.displayTrains_available();
                    System.out.println("please enter the train number you want to order: ");
                    int trainChoice = scanner.nextInt();

                    if (trainChoice < 0 || trainChoice > train_ticket_system.getTrainTable().size() ) {
                        System.out.println("Invalid train selection.");
                        return;
                    }
                    Train selectedTrain = train_ticket_system.getTrainTable().get(trainChoice);
                    
                    System.out.print("Enter number of passengers: ");
                    int passengerCount = scanner.nextInt();

                    ArrayList<Passenger> order_passengerList = new ArrayList<>();
                    // ArrayList<Ticket> order_ticketList = new ArrayList<>(); <-- class Ticket isn't defined yet, inheritance approach ... sub class ... factory pattern

                    for (int i = 0; i < passengerCount; i++) {
                        System.out.print("Select ticket type (1. Regular, 2. Business): ");
                        int ticketTypeChoice = scanner.nextInt();

                        System.out.println("Passenger " + (i + 1) + ":");
                        System.out.print("Name: ");
                        String name = scanner.next();

                        System.out.print("Age: ");
                        int age = scanner.nextInt();
                        // scanner.nextLine(); // Consume newline

                        order_passengerList.add(new Passenger(name, age));
                    }
                    

                    break;

                case 2:
                    System.out.println("Check Tickets");
                    // ....
                    break;

                case 3:
                    System.out.println("Edit Ticket");
                    // ....
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        while (current_LoginedUser.getRole() == "admin") {
            System.out.println("1. Manage Train Schedule");
            // ...
            System.out.println("5. Edit Profile");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Manage Train Schedule");
                    // ...
                    break;
                // ...
            }
        }

        
    }

}