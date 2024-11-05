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
                    // NOT IMPLEMENTED YET
                    train_ticket_system.checkTicket(scanner);
                    break;

                case 3:
                    // NOT IMPLEMENTED YET
                    train_ticket_system.editTicket(scanner);
                    break;

                //CS function
                case 7:
                    train_ticket_system.cs(scanner);
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
                    train_ticket_system.csAdmin(scanner);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                }
               
            }
        }
        
    }