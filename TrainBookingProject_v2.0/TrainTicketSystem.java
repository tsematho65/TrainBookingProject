import java.time.LocalDate;
import java.util.*;

import DAO.*;
import DataModel.*;

public class TrainTicketSystem {
    private static TrainTicketSystem instance = null;
    private  UserDAO userDAO;
    private  TrainDAO trainDAO;
    private  SeatDAO seatPlanDAO;
	private CustomerServiceDAO customerServiceDAO;
	private OrderRecordDAO orderRecordDAO;
	private User currentUser;

    private TrainTicketSystem() {
        userDAO = new UserDAO();
        trainDAO = new TrainDAO();
        seatPlanDAO = new SeatDAO();
		customerServiceDAO = new CustomerServiceDAO();
		orderRecordDAO = new OrderRecordDAO();
		currentUser = null;
    }

    public static TrainTicketSystem getInstance() {
        if (instance == null) {
            instance = new TrainTicketSystem();
        }
        return instance;
    }

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	// fn to display welcome menu
	public void displayWelcomeMenu() {
		System.out.println("Welcome to Train Ticket System!");
		System.out.println("1. Login");
		System.out.println("2. Register");
		System.out.println("3. Exit");
	}
    
    public User login(String username, String password) {
        User user = userDAO.login(username, password);
        if (user != null) {
            System.out.println("Login successful.");
        } else {
            System.out.println("Invalid username or password.");
        }
        
        System.out.println("");
        return user;
    }

	// 
    public boolean register(String username, String password){
		// Checking if username matches is already done in UserDAO
        // check if the username is already taken		
        // for (User user : userDAO.getTable_user()) {
        //     if (user.getUsername().equals(username)) {
        //         return false;
        //     }
        // }

		return userDAO.register("normal", username, password);
		// Adding new user is also done in UserDAO
        // User user = new User("normal", "userID_" + userDAO.getTable_user().size(), username, password);
        // userDAO.addUser_fromUserTable(user);
        // return true;
    }

	// sign in for reward?
	public boolean signIn() {
        LocalDate today = LocalDate.now();
		LocalDate lastSignInDate = currentUser.getLastSignInDate();
        if (lastSignInDate != null && lastSignInDate.equals(today)) {
            System.out.println("You have already signed in today. Please come back tomorrow.");
            return false; 
        }

        lastSignInDate = today;

		final int POINTS_PER_SIGN_IN = 10;
        currentUser.setPoints(currentUser.getPoints() + 10);
     
        if (isWinningSignIn()) {
            System.out.println(currentUser.getUsername() + " signIn sucessful, you get " + POINTS_PER_SIGN_IN + " points and win a prize!");
        } else {
            System.out.println(currentUser.getUsername() + " signIn sucessful, you get " + POINTS_PER_SIGN_IN + "points but no prize. ");
        }
        
        return true; 
    }

    private boolean isWinningSignIn() {
        
        Random random = new Random();
        return random.nextInt(10) < 1; 
    }

    public int displayTrains_available() {
        ArrayList<Train> availableTrainTable = trainDAO.getTable_train();
        int availableTrain_count = 0;

        // display a table to show all available trains
        for (int i = 0; i < availableTrainTable.size(); i++) {
            Train train = availableTrainTable.get(i);
            if(train.getStatus() == "active" && train.getAvailableSeats() > 0 ) {
                availableTrain_count++;
                System.out.println((i+1) + ": " + train.toString());
            }
        }
        return availableTrain_count;
    }

    public ArrayList<Train> getTrainTable(){
        return trainDAO.getTable_train();
    }

//  fn to get finished orders
	public ArrayList<OrderRecord> getFinishedOrders(String id) {
		ArrayList<OrderRecord> finishedOrders = new ArrayList<OrderRecord>();
		// ArrayList<OrderRecord> orderRecordList = userDAO.getUser_fromUserTable(id).getOrderRecordList();
		ArrayList<OrderRecord> orderRecordList = orderRecordDAO.getOrdersByUserId(id);

		for (int i = 0; i < orderRecordList.size(); i++) {
			OrderRecord orderRecord = orderRecordList.get(i);
			String status = trainDAO.getTrain_fromTrainTable(orderRecord.getTrainId()).getStatus();
			int rating = orderRecord.getRating();
			
			if (status.equals("active") && rating == 0) {
				finishedOrders.add(orderRecord);
			}
		}
		return finishedOrders;
	}
	
	
//	fn to display finished orders
	public void displayFinishedOrders(String id, Scanner sc) {
		ArrayList<OrderRecord> finishedOrders = getFinishedOrders(id);
		if (finishedOrders.size() == 0) {
			System.out.println("\nNo finished orders.");
		} else {
			System.out.println("\n===============================================");
			System.out.println("Finished Orders:");
			for (int i = 0; i < finishedOrders.size(); i++) {
				OrderRecord finishedOrder = finishedOrders.get(i);
				Train train = trainDAO.getTrain_fromTrainTable(finishedOrder.getTrainId());
				
//				print finishedOrder details
				System.out.println((i + 1) +": " + finishedOrder.getOrderId());
				System.out.println("\nTrain Number: " + train.getTrainNumber());
				System.out.println("Journey: " + "from " + train.getDeparture() + " to " + train.getArrival());
				System.out.println("Date: " + train.getDate() + ", " + train.getTime());
				System.out.println("Price: " + train.getPrice());
				
				System.out.print("\nPlease rate this order: (1-5, 5 is the best): ");
				int rating = sc.nextInt();
				finishedOrder.setRating(rating);
			}
			System.out.println("\n===============================================");
		}
	}
	
//	fn to display main menu
	public void displayMainMenu() {
		System.out.println("1. Order Ticket");
        System.out.println("2. Check Tickets");
        System.out.println("3. Edit Ticket");
        System.out.println("4. Cancel Ticket");
        System.out.println("5. Edit Profile");
        System.out.println("6. Logout");
		System.out.println("7. Customer Service");
	}

//  fn to provide recommendations
	public ArrayList<String> recommendTrains(String id) {
	    // ArrayList<OrderRecord> orderRecordList = userDAO.getUser_fromUserTable(id).getOrderRecordList();
		ArrayList<OrderRecord> orderRecordList = orderRecordDAO.getOrdersByUserId(id);

	    if (orderRecordList.isEmpty()) {
	        return new ArrayList<>();
	    } else {
	        Map<String, Integer> trainRating = new HashMap<>();
	        for (OrderRecord orderRecord : orderRecordList) {
	            String trainId = orderRecord.getTrainId();
	            int rating = orderRecord.getRating();
	            if (rating > 0) {
	                trainRating.put(trainId, rating);
				} else {
					trainRating.put(trainId, 0);
				}
	        }

	        List<String> sortedTrainIds = trainRating.entrySet().stream()
	                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
	                .map(Map.Entry::getKey)
	                .toList();

	        int endIndex = Math.min(3, sortedTrainIds.size());
	        return new ArrayList<>(sortedTrainIds.subList(0, endIndex));
	    }
	}
	
//  recommend train according to user's order history
	public void displayRecommendations(String id, String location) {
		ArrayList<String> recommendedTrainIds = recommendTrains(id);
		ArrayList<Train> availableTrains = new ArrayList<>();
		
		for (int i = 0; i < recommendedTrainIds.size(); i++) {
			Train train = trainDAO.getTrain_fromTrainTable(recommendedTrainIds.get(i));
			
			if (train.getStatus().equals("active") && train.getAvailableSeats() > 0) {
				availableTrains.add(train);
			}
		}
		
		System.out.println("\n=============================================================================================================");
		System.out.println("Top 3 Train Recommendations:");
		
		if (availableTrains.size() == 0) {
			System.out.println("No recommendations available.");
		} else {
			if (location.equals("None")) {
				for (int i = 0; i < availableTrains.size(); i++) {
					System.out.println(availableTrains.get(i).toString());
				}
			} else {
				for (int i = 0; i < availableTrains.size(); i++) {
					if (availableTrains.get(i).getDeparture().equals(location) || availableTrains.get(i).getArrival().equals(location)) {
						System.out.println(availableTrains.get(i).toString());
					}
				}
			}
		}

		System.out.println("\n=============================================================================================================");
	}

	// arrange single seat
	public ArrayList<String> arrangeSeat(String trainNumber) {
		
		seatPlan target_seatPlan = seatPlanDAO.getSeatPlan_ByTrainID(trainNumber);
		String result = seatPlanDAO.getOneAvailableSeat(target_seatPlan);
		
		// test
		System.out.println("test_seats result: " + result);
		System.out.println("test_seats target_seatPlan: " + target_seatPlan.toString());
		ArrayList<String> seatNumbers = new ArrayList<>();

		if(result != null) {
			Train targetTrain = trainDAO.getTrain_fromTrainTable(trainNumber);
			int availableSeats = targetTrain.getAvailableSeats();
			availableSeats--;
			targetTrain.setAvailableSeats(availableSeats);
			seatNumbers.add(result);
			return seatNumbers;
		}

		return null;
	}

	// arrange seats together, > 1 passengers
	public ArrayList<String> arrangeSeat(String trainNumber, int passengerCount) {
		
		seatPlan target_seatPlan = seatPlanDAO.getSeatPlan_ByTrainID(trainNumber);
		ArrayList<String> result = seatPlanDAO.getMultipleAvailableSeat(target_seatPlan, passengerCount);
		
		// test
		System.out.println("test_seats result: " + result);
		System.out.println("test_seats target_seatPlan: " + target_seatPlan.toString());

		if(result != null) {
			Train targetTrain = trainDAO.getTrain_fromTrainTable(trainNumber);
			int availableSeats = targetTrain.getAvailableSeats();
			availableSeats -= passengerCount;
			targetTrain.setAvailableSeats(availableSeats);
			return result;
		}

		return null;
	}

	// fn to find answer by keyword
	public String getAnswer(String question) {
		ArrayList<CsQuestion> questionList = customerServiceDAO.getTable_question();
		String lowerQ = question.toLowerCase();
		for (int i = 0; i < questionList.size(); i++) {
            if (lowerQ.contains(questionList.get(i).getQuestion().toLowerCase())) {
                return questionList.get(i).getAnswer();
            }
        }
		return "Your question seems to be new. Please ask via email for further assistance.";
	}

	// fn to find answer by keyword
	public String addQA(String keyword, String answer) {
		ArrayList<CsQuestion> questionList = customerServiceDAO.getTable_question();
		String lowerK = keyword.toLowerCase();
		String returnWord = "";
		for (int i = 0; i < questionList.size(); i++) {
            if (lowerK.equals(questionList.get(i).getQuestion().toLowerCase())) {
                return "Keyword already exists.";
				
            }
        }
		if(customerServiceDAO.addQA(new CsQuestion(keyword, answer))){
			returnWord = "QA added successfully.";
		}else{
			returnWord = "Failed to add QA.";
		}
		return returnWord;
	}

	// fn to order tickets
	public void orderTicket(Scanner sc) {
		if (orderRecordDAO.getOrdersByUserId(currentUser.getId()).size() > 0) {
			System.out.print("\nDo you need any recommendations? (Y/N) ");
			String preferences = sc.nextLine();
			
			if (preferences.equals("Y")) {
				System.out.print("\nPlease enter the location that you might want to depart or arrive [LA, Washington DC, Miami, Chicago, None]: ");
				String location = sc.nextLine();
				
				displayRecommendations(currentUser.getId(), location);
			}
		}

		// Base Case: No train recommendations
		System.out.println("\n=============================================================================================================");
		System.out.println("Available trains:");
		displayTrains_available();
		System.out.println("\nPlease enter the train number you want to order: ");
		int trainChoice = sc.nextInt() - 1;
		System.out.println("\n=============================================================================================================");

		if (trainChoice < 0 || trainChoice > trainDAO.getTable_train().size()) {
			System.out.println("Invalid train selection.");
			return;
		}
		Train selectedTrain = trainDAO.getTable_train().get(trainChoice);
		
		System.out.print("Enter number of passengers: ");
		int passengerCount = sc.nextInt();

		ArrayList<Passenger> order_passengerList = new ArrayList<>();

		int seatCount = selectedTrain.getAvailableSeats();
		if (passengerCount > seatCount) {
			System.out.println("Not enough seats available.");
		}
		int totalPrice = 0;
		int counter = 0;
		double ticketPrice = selectedTrain.getPrice();
		
		while (counter < passengerCount) {
			System.out.print("Select ticket type (1. Regular, 2. Upgrade(with meal) price = + $30 ): ");
			int ticketTypeChoice = sc.nextInt();
			
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
			String name = sc.next();

			System.out.print("Age: ");
			int age = sc.nextInt();
			// scanner.nextLine(); // Consume newline
			
			order_passengerList.add(new Passenger(name, age));
			counter++;
		}
		

		// Seats arrangement -> ticket Info:
		ArrayList<String> seatNumbersForticket ;
		//System.out.println("Seats will be arranged randomly.");
		if(passengerCount > 1 && passengerCount <= 6) {
			System.out.println("Would you like to arrange seats together? (Y/N)");
			String preference = sc.next();
			if (preference.equals("Y")) {
				seatNumbersForticket = arrangeSeat(selectedTrain.getTrainNumber(), passengerCount);
			}else{
				seatNumbersForticket = arrangeSeat(selectedTrain.getTrainNumber());

			}
		} else if(passengerCount > 6) {
			System.out.println("Seats will be arranged randomly.");
			seatNumbersForticket = arrangeSeat(selectedTrain.getTrainNumber(), passengerCount);
		}
		else {
			System.out.println("Seats will be arranged randomly.");
			seatNumbersForticket = arrangeSeat(selectedTrain.getTrainNumber());
		}
		
		// test:
		// System.out.println("test_seats are: " + seatNumbersForticket);
		// System.out.println("test_seats left: " + selectedTrain.getAvailableSeats());

		// Ticket Info:
		// ...

		OrderRecord orderRecord = new OrderRecord(
			String.format("orderID_%s_%s", currentUser.getId(), orderRecordDAO.getOrdersByUserId(currentUser.getId()).size()),
			currentUser.getId(),
			selectedTrain.getTrainNumber(), 
			new Date(),
			totalPrice, // tmp
			order_passengerList, // tmp
			null// tmp
		);

		// Add through OrderRecordDAO
		// current_LoginedUser.addOrderRecord(orderRecord);
		orderRecordDAO.addOrderRecord(orderRecord);
		
		System.out.println("Order successful.");
//                    System.out.println("Order ID: " + orderRecord.getOrderId());
	}

	// fn to check tickets (more like check orders)
	public void checkTicket(Scanner sc) {
		System.out.println("Your Order Records:");
		ArrayList<OrderRecord> userOrders = orderRecordDAO.getOrdersByUserId(currentUser.getId());

		if (userOrders.isEmpty()) {
			System.out.println("You currently have no orders.");
		} else {
			for (OrderRecord order : userOrders) {
				System.out.println(order.toString());
				System.out.println("--------------------------------------------------");
			}
		}
	}

	public void editTicket(Scanner sc) {
		System.out.println("Edit Tickets");
	}

	public void cs(Scanner sc) {
		boolean isStay = true;
		System.out.println("\n=============================================================================================================");
		System.out.println("CSer : Hi, welcome to customer service, how can I help you? (type your question or type exit to back to main menu)");
		while(isStay){
			System.out.print("You : ");
			String question = sc.nextLine();
			if(question.equals("exit")){
				isStay = false;
				System.out.println("CSer : Goodbye!");
			}else if(question == null || question.isEmpty()){
				System.out.println("CSer : Please type your question.");
			}else{
				System.out.println("CSer : " + getAnswer(question));
			}
			System.out.println((isStay) ? "CSer : Anything else?(type your question or type exit to back to main menu)" : "");
		}
		System.out.println("\n=============================================================================================================");
	}


	public void csAdmin(Scanner sc) {
		boolean isStay = true;
		System.out.println("\n=============================================================================================================");
		while(isStay){
			boolean isKeyword = false;
			boolean isAnswer = false;
			String answer = null;
			String keyword = null;
			while(!isKeyword){
				System.out.println("Enter keyword:");
				keyword = sc.nextLine();
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
				answer = sc.nextLine();
				if(answer.isEmpty()){
					System.out.println("Answer cannot be empty.");
				}else if(answer.equals("exit")){
					isStay = false;
				}else{
					isAnswer = true;
				}
			}
			System.out.println(addQA(keyword, answer));
		}
		System.out.println("\n=============================================================================================================");
	}

	public void addTrain(Train train) {
	trainDAO.addTrain_fromTrainTable(train);
	}
	
	public boolean removeTrain(String trainNumber) {
		return trainDAO.deleteTrain_fromTrainTable(trainNumber);
	}
	
	
	public void updateTrain(Train train) {
		trainDAO.updateTrain_fromTrainTable(train);
	}
	
	public Train getTrainByNumber(String trainNumber) {
		return trainDAO.getTrain_fromTrainTable(trainNumber);
	}
	
	
	public void displayAllTrains() {
		ArrayList<Train> allTrains = trainDAO.getTable_train();
		if(allTrains.isEmpty()) {
			System.out.println("No trains available.");
		} else {
			for(Train train : allTrains) {
				System.out.println(train.toString());
			}
		}
	}
}
