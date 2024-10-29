// Main.java
package DataModel;

import java.util.*;
import DB_init.*;
import DAO.*;

public class Main{
    public static void main(String[] args) {
        TrainTicketSystem train_ticket_system = TrainTicketSystem.getInstance();
        Scanner scanner = new Scanner(System.in);
        User current_LoginedUser = null;
        OrderRecordDAO orderRecordDAO = new OrderRecordDAO();
        TicketDAO ticketDAO = new TicketDAO();

        // Login or Register Menu
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
                    System.out.print("Registration Form:\nEnter username: ");
                    String register_username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String register_pwd = scanner.nextLine();

                    boolean result = train_ticket_system.register(register_username, register_pwd);
                    if (result) {
                        System.out.println("Registered successfully.");
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
        
      
        while (current_LoginedUser.getRole().equals("normal")) {
            System.out.println("\n1. Order Ticket");
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
                    orderTicket(train_ticket_system, scanner, current_LoginedUser, ticketDAO, orderRecordDAO);
                    break;

                case 2:
                    checkTickets(train_ticket_system, scanner, current_LoginedUser, orderRecordDAO);
                    break;

                case 3:
                    editTicket(train_ticket_system, scanner, current_LoginedUser, ticketDAO, orderRecordDAO);
                    break;

                case 4:
                    cancelTicket(train_ticket_system, scanner, current_LoginedUser, ticketDAO, orderRecordDAO);
                    break;

                case 5:
                    System.out.println("Edit Profile feature to be implemented.");
                    break;

                case 6:
                    System.out.println("Logging out...");
                    current_LoginedUser = null;
                    main(args); 
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        // Admin Menu
        while (current_LoginedUser.getRole().equals("admin")) {
            System.out.println("\n1. Manage Train Schedule");
            System.out.println("5. Edit Profile");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.println("Manage Train Schedule feature to be implemented.");
                    break;
        
                case 6:
                    System.out.println("Logging out...");
                    current_LoginedUser = null;
                    main(args); 
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }


    private static void orderTicket(TrainTicketSystem system, Scanner scanner, User user, TicketDAO ticketDAO, OrderRecordDAO orderRecordDAO) {
        System.out.println("Order Ticket:");
        System.out.println("\nDo you have any preferences? (Y/N)");
        String preferences = scanner.nextLine();

        String departure = "";
        String arrival = "";
        String date = "";

        if (preferences.equalsIgnoreCase("Y")) {
            System.out.print("Enter departure: ");
            departure = scanner.nextLine();
    
            System.out.print("Enter arrival: ");
            arrival = scanner.nextLine();
    
            System.out.print("Enter date (yyyy-MM-dd): ");
            date = scanner.nextLine();
    
        } else if (preferences.equalsIgnoreCase("N")){
           
        } else {
            System.out.println("Invalid choice.");
            return;
        }
        

        System.out.println("\n------------------------------------------------------------------------------------------------------");
        System.out.println("Available trains:");
        int availableTrainCount = system.displayTrains_available();
        if (availableTrainCount == 0) {
            System.out.println("No trains available based on your criteria.");
            return;
        }
        System.out.print("Please enter the train number you want to order: ");
        int trainChoice = scanner.nextInt();
        scanner.nextLine();

        if (trainChoice < 0 || trainChoice >= system.getTrainTable().size() ) {
            System.out.println("Invalid train selection.");
            return;
        }
        Train selectedTrain = system.getTrainTable().get(trainChoice);
        
        if (!selectedTrain.getStatus().equals("active") || selectedTrain.getAvailableSeats() <= 0) {
            System.out.println("Selected train is not available.");
            return;
        }

        System.out.print("Enter number of passengers: ");
        int passengerCount = scanner.nextInt();
        scanner.nextLine();

        if (passengerCount <= 0) {
            System.out.println("Number of passengers must be at least 1.");
            return;
        }

        ArrayList<Passenger> order_passengerList = new ArrayList<>();
        ArrayList<Ticket> order_ticketList = new ArrayList<>();

        double totalAmount = 0.0;

        for (int i = 0; i < passengerCount; i++) {
            System.out.println("\nPassenger " + (i + 1) + ":");
            System.out.print("Name: ");
            String name = scanner.nextLine();

            System.out.print("Age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); 

            System.out.print("Select ticket type (1. Regular, 2. Business): ");
            int ticketTypeChoice = scanner.nextInt();
            scanner.nextLine(); 

            Ticket ticket = null;
            if (ticketTypeChoice == 1) {
                double price = selectedTrain.getPrice(); 
                ticket = new RegularTicket("Regular", price);
                totalAmount += price;
            } else if (ticketTypeChoice == 2) {
                double price = selectedTrain.getPrice() * 1.5; 
                ticket = new BusinessTicket("Business", price, "Yes");
                totalAmount += price;
            } else {
                System.out.println("Invalid ticket type. Skipping this passenger.");
                continue;
            }

            order_passengerList.add(new Passenger(name, age));
            order_ticketList.add(ticket);
            ticketDAO.addTicket(ticket); 
        }

        // Update train's available seats
        selectedTrain.setAvailableSeats(selectedTrain.getAvailableSeats() - passengerCount);
        system.getTrainTable().set(trainChoice, selectedTrain); 

        // Create Order Record
        int newOrderId = system.getOrdersByUserId(Integer.parseInt(user.getId().split("_")[1])).size() + 1;
        int userId = Integer.parseInt(user.getId().split("_")[1]);
        int trainId = Integer.parseInt(selectedTrain.getTrainNumber().split("_")[1]); 
        Date orderDate = new Date();

        OrderRecord newOrder = new OrderRecord(newOrderId, userId, trainId, orderDate, totalAmount, order_passengerList, order_ticketList);
        orderRecordDAO.addOrderRecord(newOrder);

        System.out.println("\nOrder placed successfully!");
        System.out.println("Order Details:");
        System.out.println(newOrder.toString());
    }

    // Check Tickets Function
    private static void checkTickets(TrainTicketSystem system, Scanner scanner, User user, OrderRecordDAO orderRecordDAO) {
        System.out.println("Your Order Records:");
        int userId = Integer.parseInt(user.getId().split("_")[1]); // Assuming userID format is "userID_X"
        ArrayList<OrderRecord> userOrders = system.getOrdersByUserId(userId);

        if (userOrders.isEmpty()) {
            System.out.println("You currently have no orders.");
        } else {
            for (OrderRecord order : userOrders) {
                System.out.println(order.toString());
                System.out.println("--------------------------------------------------");
            }
        }
    }

    // Edit Ticket Function
    private static void editTicket(TrainTicketSystem system, Scanner scanner, User user, TicketDAO ticketDAO, OrderRecordDAO orderRecordDAO) {
        System.out.print("Enter the Order ID you want to edit: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        OrderRecord order = system.getOrderById(orderId);
        if (order == null || order.getUserId() != Integer.parseInt(user.getId().split("_")[1])) {
            System.out.println("Order not found or you do not have permission to edit this order.");
            return;
        }

        System.out.println("Current Order Details:");
        System.out.println(order.toString());
        System.out.println("You can perform the following actions:");
        System.out.println("1. Modify Passenger Information");
        System.out.println("2. Modify Ticket Type");
        System.out.println("3. Return");
        System.out.print("Choose an option: ");
        int editOption = scanner.nextInt();
        scanner.nextLine();

        switch (editOption) {
            case 1:
                // Modify Passenger Information
                ArrayList<Passenger> passengers = order.getPassengerList();
                for (int i = 0; i < passengers.size(); i++) {
                    Passenger p = passengers.get(i);
                    System.out.printf("Passenger %d: %s, Age: %d\n", i + 1, p.getName(), p.getAge());
                    System.out.print("Do you want to modify this passenger's information? (Y/N): ");
                    String choice = scanner.nextLine();
                    if (choice.equalsIgnoreCase("Y")) {
                        System.out.print("Enter new name: ");
                        String newName = scanner.nextLine();
                        System.out.print("Enter new age: ");
                        int newAge = scanner.nextInt();
                        scanner.nextLine();

                        p.setName(newName);
                        p.setAge(newAge);
                    }
                }
                System.out.println("Passenger information updated.");
                break;

            case 2:
                ArrayList<Ticket> tickets = order.getTicketList();
                for (int i = 0; i < tickets.size(); i++) {
                    Ticket t = tickets.get(i);
                    System.out.printf("Ticket %d: %s\n", i + 1, t.toString());
                    System.out.print("Do you want to modify this ticket's type? (Y/N): ");
                    String choice = scanner.nextLine();
                    if (choice.equalsIgnoreCase("Y")) {
                        System.out.println("Select new ticket type:");
                        System.out.println("1. Regular");
                        System.out.println("2. Business");
                        System.out.print("Choose: ");
                        int typeChoice = scanner.nextInt();
                        scanner.nextLine();

                        if (typeChoice == 1) {
                            if (t instanceof BusinessTicket) {
                                BusinessTicket bt = (BusinessTicket) t;
                                double newPrice = system.getTrainTable().get(order.getTrainId() - 1).getPrice();
                                RegularTicket rt = new RegularTicket("Regular", newPrice);
                                tickets.set(i, rt);
                                order.setAmount(order.getAmount() - bt.getPrice() + rt.getPrice());
                                ticketDAO.getTable_ticket().remove(bt);
                                ticketDAO.addTicket(rt);
                            }
                        } else if (typeChoice == 2) {
                            if (t instanceof RegularTicket) {
                                RegularTicket rt = (RegularTicket) t;
                                double newPrice = system.getTrainTable().get(order.getTrainId() - 1).getPrice() * 1.5;
                                BusinessTicket bt = new BusinessTicket("Business", newPrice, "Yes");
                                tickets.set(i, bt);
                                order.setAmount(order.getAmount() - rt.getPrice() + bt.getPrice());
                                ticketDAO.getTable_ticket().remove(rt);
                                ticketDAO.addTicket(bt);
                            }
                        } else {
                            System.out.println("Invalid ticket type selection.");
                        }
                    }
                }
                System.out.println("Ticket types updated.");
                break;

            case 3:

                return;

            default:
                System.out.println("Invalid option.");
        }

   
        system.updateOrderRecord(order);
        System.out.println("Order has been updated.");
    }

    private static void cancelTicket(TrainTicketSystem system, Scanner scanner, User user, TicketDAO ticketDAO, OrderRecordDAO orderRecordDAO) {
        System.out.print("Enter the Order ID you want to cancel: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        OrderRecord order = system.getOrderById(orderId);
        if (order == null || order.getUserId() != Integer.parseInt(user.getId().split("_")[1])) {
            System.out.println("Order not found or you do not have permission to cancel this order.");
            return;
        }

        System.out.print("Are you sure you want to cancel this order? (Y/N): ");
        String confirm = scanner.nextLine();
        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Cancel operation aborted.");
            return;
        }

  
        int trainId = order.getTrainId();
        Train train = system.getTrainTable().get(trainId - 1); 
        int passengerCount = order.getPassengerList().size();
        train.setAvailableSeats(train.getAvailableSeats() + passengerCount);
        system.getTrainTable().set(trainId - 1, train); 

      
        for (Ticket t : order.getTicketList()) {
            ticketDAO.getTable_ticket().remove(t);
        }

        boolean deleted = system.deleteOrderRecord(orderId);
        if (deleted) {
            System.out.println("Order has been successfully canceled.");
        } else {
            System.out.println("Failed to cancel the order.");
        }
    }
}