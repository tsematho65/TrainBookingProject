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
                    System.out.print("\nWelcome " + current_LoginedUser.getUsername() + "!");
                    
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
					if (current_LoginedUser.getOrderRecordList().size() > 0) {
						System.out.print("\nDo you need any recommendations? (Y/N) ");
						String preferences = scanner.nextLine();
						
						if (preferences.equals("Y")) {
							System.out.print("\nPlease enter the location that you might want to depart or arrive [LA, Washington DC, Miami, Chicago, None]: ");
							String location = scanner.nextLine();
							
	                		train_ticket_system.displayRecommendations(current_LoginedUser.getId(), location);
						}
					}
                    
                    // Base Case: No train recommendations
                    System.out.println("\n=============================================================================================================");
                    System.out.println("Available trains:");
                    train_ticket_system.displayTrains_available();
                    System.out.println("\nPlease enter the train number you want to order: ");
                    int trainChoice = scanner.nextInt() - 1;
                    System.out.println("\n=============================================================================================================");

                    if (trainChoice < 0 || trainChoice > train_ticket_system.getTrainTable().size() ) {
                        System.out.println("Invalid train selection.");
                        return;
                    }
                    Train selectedTrain = train_ticket_system.getTrainTable().get(trainChoice);
                    
                    System.out.print("Enter number of passengers: ");
                    int passengerCount = scanner.nextInt();

                    ArrayList<Passenger> order_passengerList = new ArrayList<>();

                    int seatCount = selectedTrain.getAvailableSeats();
                    if (passengerCount > seatCount) {
                        System.out.println("Not enough seats available.");
                        break;
                    }
                    int totalPrice = 0;
                    int counter = 0;
                    double ticketPrice = selectedTrain.getPrice();
                    
                    while (counter < passengerCount) {
                        System.out.print("Select ticket type (1. Regular, 2. Upgrade(with meal) price = + $30 ): ");
                        int ticketTypeChoice = scanner.nextInt();
                        
                        if (ticketTypeChoice == 2) {
                            totalPrice += (ticketPrice+30);
                        } else if(ticketTypeChoice == 1) {
                            totalPrice += ticketPrice;
                        } else {
                            System.out.println("Invalid ticket type. Please try again.");
                            continue;
                        }
                        
                        System.out.println("Passenger " + (counter + 1) + ":");
                        System.out.print("Name: ");
                        String name = scanner.next();

                        System.out.print("Age: ");
                        int age = scanner.nextInt();
                        // scanner.nextLine(); // Consume newline
                        
                        order_passengerList.add(new Passenger(name, age));
                        counter++;
                    }
                    

                    // Seats arrangement -> ticket Info:
                    ArrayList<String> seatNumbersForticket ;
                    //System.out.println("Seats will be arranged randomly.");
                    if(passengerCount > 1 && passengerCount <= 6) {
                        System.out.println("Would you like to arrange seats together? (Y/N)");
                        String preference = scanner.next();
                        if (preference.equals("Y")) {
                            seatNumbersForticket = train_ticket_system.arrangeSeat(selectedTrain.getTrainNumber(), passengerCount);
                        }else{
                            seatNumbersForticket = train_ticket_system.arrangeSeat(selectedTrain.getTrainNumber());

                        }
                    } else if(passengerCount > 6) {
                        System.out.println("Seats will be arranged randomly.");
                        seatNumbersForticket = train_ticket_system.arrangeSeat(selectedTrain.getTrainNumber(), passengerCount);
                    }
                    else {
                        System.out.println("Seats will be arranged randomly.");
                        seatNumbersForticket = train_ticket_system.arrangeSeat(selectedTrain.getTrainNumber());
                    }
                    
                    // test:
                    // System.out.println("test_seats are: " + seatNumbersForticket);
                    // System.out.println("test_seats left: " + selectedTrain.getAvailableSeats());

                    // Ticket Info:
                    // ...

                    OrderRecord orderRecord = new OrderRecord(
                    	String.format("orderID_%s_%s", current_LoginedUser.getId(), current_LoginedUser.getOrderRecordList().size()),
                		current_LoginedUser.getId(),
                		selectedTrain.getTrainNumber(), 
                		new Date(),
                		totalPrice, // tmp
                		order_passengerList, // tmp
                		null// tmp
                	);
                    current_LoginedUser.addOrderRecord(orderRecord);
                    
                    System.out.println("Order successful.");
//                    System.out.println("Order ID: " + orderRecord.getOrderId());

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