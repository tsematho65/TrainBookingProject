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
						System.out.print("\nDo you have any preferences for your train ticket? (Y/N) ");
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
                    int trainChoice = scanner.nextInt();
                    System.out.println("\n=============================================================================================================");

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
                    
                    OrderRecord orderRecord = new OrderRecord(
                    	String.format("orderID_%s_%s", current_LoginedUser.getId(), current_LoginedUser.getOrderRecordList().size()),
                		current_LoginedUser.getId(), 
                		selectedTrain.getTrainNumber(), 
                		new Date(),
                		0, // tmp
                		null, // tmp
                		null // tmp
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

                //CS function
                case 7:
                    boolean isStay = true;
                    System.out.println("\n=============================================================================================================");
                    System.out.println("CSer : Hi, welcome to customer service, how can I help you? (type your question or type exit to back to main menu)");
                    while(isStay){
                        System.out.print("You : ");
                        String question = scanner.nextLine();
                        if(question.equals("exit")){
                            isStay = false;
                            train_ticket_system.getAnswer(question);
                        }else{
                            train_ticket_system.getAnswer(question);
                        }
                        System.out.println((isStay) ? "CSer : Anything else?(type your question or type exit to back to main menu)" : "");
                    }
                    System.out.println("\n=============================================================================================================");
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
            System.out.println("7. Add Keyword and answer");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.println("Manage Train Schedule");
                    // ...
                    break;
                case 7:
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
                            }else if(keyword.equals("exit")){
                                isStay = false;
                            }else{
                                isKeyword = true;
                            }
                        }
                        while(!isAnswer){
                            System.out.println("Enter answer:");
                            answer = scanner.nextLine();
                            if(answer.isEmpty()){
                                System.out.println("Answer cannot be empty.");
                            }else if(answer.equals("exit")){
                                isStay = false;
                            }else{
                                isAnswer = true;
                            }
                        }
                        System.out.println(train_ticket_system.addQA(keyword, answer));
                    }
                    System.out.println("\n=============================================================================================================");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                }
               
            }
        }

        
    }

