import java.time.LocalDate;
import java.util.*;

import DAO.*;
import DataModel.*;

public class TrainTicketSystem {
	private static TrainTicketSystem instance = null;
	private UserDAO userDAO;
	private TrainDAO trainDAO;
	private SeatDAO seatPlanDAO;
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

	public boolean register(String username, String password) {
		// Checking if username matches is already done in UserDAO
		// check if the username is already taken
		// for (User user : userDAO.getTable_user()) {
		// if (user.getUsername().equals(username)) {
		// return false;
		// }
		// }

		return userDAO.register("normal", username, password);
		// Adding new user is also done in UserDAO
		// User user = new User("normal", "userID_" + userDAO.getTable_user().size(),
		// username, password);
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
			System.out.println(currentUser.getUsername() + " signIn sucessful, you get " + POINTS_PER_SIGN_IN
					+ " points and win a prize!");
		} else {
			System.out.println(currentUser.getUsername() + " signIn sucessful, you get " + POINTS_PER_SIGN_IN
					+ "points but no prize. ");
		}

		return true;
	}

	private boolean isWinningSignIn() {

		Random random = new Random();
		return random.nextInt(10) < 1;
	}

	// fn to display finished orders
	public void displayFinishedOrders(String id, Scanner scanner) {
		ArrayList<OrderRecord> finishedOrders = getFinishedOrders(id);
		if (finishedOrders.size() == 0) {
			System.out.println("\nNo finished orders.");
		} else {
			System.out.println("\n===============================================");
			System.out.println("Finished Orders:");
			for (int i = 0; i < finishedOrders.size(); i++) {
				OrderRecord finishedOrder = finishedOrders.get(i);
				Train train = trainDAO.getTrain_fromTrainTable(finishedOrder.getTrainId());

				// print finishedOrder details
				System.out.println((i + 1) + ": " + finishedOrder.getOrderId());
				System.out.println("\nTrain Number: " + train.getTrainNumber());
				System.out.println("Journey: " + "from " + train.getDeparture() + " to " + train.getArrival());
				System.out.println("Date: " + train.getDate() + ", " + train.getTime());
				System.out.println("Price: " + train.getPrice());

				System.out.print("\nPlease rate this order: (1-5, 5 is the best): ");
				int rating = scanner.nextInt();
				finishedOrder.setRating(rating);
			}
			System.out.println("\n===============================================");
		}
	}

	// fn to get finished orders
	public ArrayList<OrderRecord> getFinishedOrders(String id) {
		ArrayList<OrderRecord> finishedOrders = new ArrayList<OrderRecord>();
		// ArrayList<OrderRecord> orderRecordList =
		// userDAO.getUser_fromUserTable(id).getOrderRecordList();
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

	// public ArrayList<Train> getTrainTable(){
	// return trainDAO.getTable_train();
	// }

	// fn to display main menu
	public void displayMainMenu() {
		System.out.println("1. Order Ticket");
		System.out.println("2. View Orders");
		System.out.println("3. Edit Profile");
		System.out.println("4. Customer Service");
		System.out.println("5. Logout");
	}

	// fn to order tickets
	public void orderTicket(Scanner scanner) {
		if (orderRecordDAO.getOrdersByUserId(currentUser.getId()).size() > 0) {
			System.out.print("\nDo you need any recommendations? (Y/N) ");
			String preferences = scanner.nextLine();

			if (preferences.equals("Y")) {
				System.out.print(
						"\nPlease enter the location that you might want to depart or arrive [LA, Washington DC, Miami, Chicago, None]: ");
				String location = scanner.nextLine();

				displayRecommendations(currentUser.getId(), location);
			}
		}

		// Base Case: No train recommendations
		System.out.println(
				"\n=============================================================================================================");
		System.out.println("Available trains:");
		displayTrains_available();
		System.out.println("\nPlease enter the train number you want to order: ");
		int trainChoice = scanner.nextInt() - 1;
		System.out.println(
				"\n=============================================================================================================");

		if (trainChoice < 0 || trainChoice > trainDAO.getTable_train().size()) {
			System.out.println("Invalid train selection.");
			return;
		}
		Train selectedTrain = trainDAO.getTable_train().get(trainChoice);

		System.out.print("Enter number of passengers: ");
		int passengerCount = scanner.nextInt();

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
			int ticketTypeChoice = scanner.nextInt();

			if (ticketTypeChoice == 2) {
				totalPrice += (ticketPrice + 30);
			} else if (ticketTypeChoice == 1) {
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
		ArrayList<String> seatNumbersForticket;
		// System.out.println("Seats will be arranged randomly.");
		if (passengerCount > 1 && passengerCount <= 6) {
			System.out.println("Would you like to arrange seats together? (Y/N)");
			String preference = scanner.next();
			if (preference.equals("Y")) {
				seatNumbersForticket = arrangeSeat(selectedTrain.getTrainNumber(), passengerCount);
			} else {
				seatNumbersForticket = arrangeSeat(selectedTrain.getTrainNumber());

			}
		} else if (passengerCount > 6) {
			System.out.println("Seats will be arranged randomly.");
			seatNumbersForticket = arrangeSeat(selectedTrain.getTrainNumber(), passengerCount);
		} else {
			System.out.println("Seats will be arranged randomly.");
			seatNumbersForticket = arrangeSeat(selectedTrain.getTrainNumber());
		}

		// test:
		// System.out.println("test_seats are: " + seatNumbersForticket);
		// System.out.println("test_seats left: " + selectedTrain.getAvailableSeats());

		// Ticket Info:
		// ...

		OrderRecord orderRecord = new OrderRecord(
				String.format("orderID_%s_%s", currentUser.getId(),
						orderRecordDAO.getOrdersByUserId(currentUser.getId()).size()),
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
		// System.out.println("Order ID: " + orderRecord.getOrderId());
	}

	public int displayTrains_available() {
		ArrayList<Train> availableTrainTable = trainDAO.getTable_train();
		int availableTrain_count = 0;

		// display a table to show all available trains
		for (int i = 0; i < availableTrainTable.size(); i++) {
			Train train = availableTrainTable.get(i);
			if (train.getStatus() == "active" && train.getAvailableSeats() > 0) {
				availableTrain_count++;
				System.out.println((i + 1) + ": " + train.toString());
			}
		}
		return availableTrain_count;
	}

	// recommend train according to user's order history
	public void displayRecommendations(String id, String location) {
		ArrayList<String> recommendedTrainIds = recommendTrains(id);
		ArrayList<Train> availableTrains = new ArrayList<>();

		for (int i = 0; i < recommendedTrainIds.size(); i++) {
			Train train = trainDAO.getTrain_fromTrainTable(recommendedTrainIds.get(i));

			if (train.getStatus().equals("active") && train.getAvailableSeats() > 0) {
				availableTrains.add(train);
			}
		}

		System.out.println(
				"\n=============================================================================================================");
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
					if (availableTrains.get(i).getDeparture().equals(location)
							|| availableTrains.get(i).getArrival().equals(location)) {
						System.out.println(availableTrains.get(i).toString());
					}
				}
			}
		}

		System.out.println(
				"\n=============================================================================================================");
	}

	// fn to provide recommendations
	public ArrayList<String> recommendTrains(String id) {
		// ArrayList<OrderRecord> orderRecordList =
		// userDAO.getUser_fromUserTable(id).getOrderRecordList();
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

	// arrange single seat
	public ArrayList<String> arrangeSeat(String trainNumber) {

		seatPlan target_seatPlan = seatPlanDAO.getSeatPlan_ByTrainID(trainNumber);
		String result = seatPlanDAO.getOneAvailableSeat(target_seatPlan);

		// test
		System.out.println("test_seats result: " + result);
		System.out.println("test_seats target_seatPlan: " + target_seatPlan.toString());
		ArrayList<String> seatNumbers = new ArrayList<>();

		if (result != null) {
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

		if (result != null) {
			Train targetTrain = trainDAO.getTrain_fromTrainTable(trainNumber);
			int availableSeats = targetTrain.getAvailableSeats();
			availableSeats -= passengerCount;
			targetTrain.setAvailableSeats(availableSeats);
			return result;
		}

		return null;
	}

	// fn to check tickets (more like check orders)
	public void checkTicket(Scanner scanner) {
		while (true) {
			ArrayList<OrderRecord> userOrders = orderRecordDAO.getOrdersByUserId(currentUser.getId());
			System.out.println("Your Order Records:");
			displayOrders(userOrders);

			if (userOrders.isEmpty()) {
				return;
			}

			System.out.print("Enter the Order No. to EDIT or CANCEL it, 0 to return to the main menu: ");
			int orderNo = scanner.nextInt(); // not to be confused with Order Id

			if (orderNo == 0) {
				return;
			} else if (orderNo > userOrders.size()) {
				System.out.println("Invalid option.");
			} else {
				System.out.println("1. Edit Order");
				System.out.println("2. Cancel Order");
				System.out.println("3. Return");
				System.out.print("Please select an option: ");
				int option = scanner.nextInt();
				scanner.nextLine();

				switch (option) {
					case 1:
						editTicket(scanner, userOrders.get(orderNo - 1));
						break;

					case 2:
						cancelTicket(scanner, userOrders.get(orderNo - 1));
						break;

					case 3:
						return;

					default:
						System.out.println("Invalid option. Please try again.");
				}
			}
		}
	}

	private int displayOrders(ArrayList<OrderRecord> userOrders) {
		if (userOrders.isEmpty()) {
			System.out.println("You currently have no orders.");
		} else {
			for (int i = 0; i < userOrders.size(); i++) {
				OrderRecord order = userOrders.get(i);
				String orderNoAndId = String.format("%s %d: %s %s", "==========", i + 1, order.getOrderId(),
						"==========");
				System.out.println(orderNoAndId);
				System.out.println(order.toString());
				for (int j = 0; j < orderNoAndId.length(); j++) {
					System.out.print("=");
				}
				System.out.println();
			}
		}
		return userOrders.size();
	}

	private void editTicket(Scanner scanner, OrderRecord order) {
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
				// ArrayList<Ticket> tickets = order.getTicketList();
				// for (int i = 0; i < tickets.size(); i++) {
				// Ticket t = tickets.get(i);
				// System.out.printf("Ticket %d: %s\n", i + 1, t.toString());
				// System.out.print("Do you want to modify this ticket's type? (Y/N): ");
				// String choice = scanner.nextLine();
				// if (choice.equalsIgnoreCase("Y")) {
				// System.out.println("Select new ticket type:");
				// System.out.println("1. Regular");
				// System.out.println("2. Business");
				// System.out.print("Choose: ");
				// int typeChoice = scanner.nextInt();
				// scanner.nextLine();

				// if (typeChoice == 1) {
				// if (t instanceof BusinessTicket) {
				// BusinessTicket bt = (BusinessTicket) t;
				// double newPrice = system.getTrainTable().get(order.getTrainId() -
				// 1).getPrice();
				// RegularTicket rt = new RegularTicket("Regular", newPrice);
				// tickets.set(i, rt);
				// order.setAmount(order.getAmount() - bt.getPrice() + rt.getPrice());
				// ticketDAO.getTable_ticket().remove(bt);
				// ticketDAO.addTicket(rt);
				// }
				// } else if (typeChoice == 2) {
				// if (t instanceof RegularTicket) {
				// RegularTicket rt = (RegularTicket) t;
				// double newPrice = system.getTrainTable().get(order.getTrainId() -
				// 1).getPrice() * 1.5;
				// BusinessTicket bt = new BusinessTicket("Business", newPrice, "Yes");
				// tickets.set(i, bt);
				// order.setAmount(order.getAmount() - rt.getPrice() + bt.getPrice());
				// ticketDAO.getTable_ticket().remove(rt);
				// ticketDAO.addTicket(bt);
				// }
				// } else {
				// System.out.println("Invalid ticket type selection.");
				// }
				// }
				// }
				// System.out.println("Ticket types updated.");
				break;

			case 3:
				return;

			default:
				System.out.println("Invalid option.");
		}

		System.out.println("Order has been updated.");
	}

	private void cancelTicket(Scanner scanner, OrderRecord order) {
		System.out.print("Are you sure you want to cancel this order? (Y/N): ");
		String confirm = scanner.nextLine();
		if (!confirm.equalsIgnoreCase("Y")) {
			System.out.println("Cancel operation aborted.");
			return;
		}

		String trainId = order.getTrainId();
		Train train = trainDAO.getTrain_fromTrainTable(trainId);
		int passengerCount = order.getPassengerList().size();
		train.setAvailableSeats(train.getAvailableSeats() + passengerCount);
		trainDAO.updateTrain_fromTrainTable(train);

		// for (Ticket t : order.getTicketList()) {
		// ticketDAO.getTable_ticket().remove(t);
		// }

		boolean deleted = orderRecordDAO.deleteOrderRecord(order.getOrderId());
		if (deleted) {
			System.out.println("Order has been successfully canceled.");
		} else {
			System.out.println("Failed to cancel the order.");
		}
	}

	public void cs(Scanner scanner) {
		boolean isStay = true;
		System.out.println(
				"\n=============================================================================================================");
		System.out.println(
				"CSer : Hi, welcome to customer service, how can I help you? (type your question or type exit to back to main menu)");
		while (isStay) {
			System.out.print("You : ");
			String question = scanner.nextLine();
			if (question.equals("exit")) {
				isStay = false;
				System.out.println("CSer : Goodbye!");
			} else if (question == null || question.isEmpty()) {
				System.out.println("CSer : Please type your question.");
			} else {
				System.out.println("CSer : " + getAnswer(question));
			}
			System.out.println(
					(isStay) ? "CSer : Anything else?(type your question or type exit to back to main menu)" : "");
		}
		System.out.println(
				"\n=============================================================================================================");
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

	// public void addTrain(Train train) {
	// trainDAO.addTrain_fromTrainTable(train);
	// }

	// public boolean removeTrain(String trainNumber) {
	// return trainDAO.deleteTrain_fromTrainTable(trainNumber);
	// }

	// public void updateTrain(Train train) {
	// trainDAO.updateTrain_fromTrainTable(train);
	// }

	// public Train getTrainByNumber(String trainNumber) {
	// return trainDAO.getTrain_fromTrainTable(trainNumber);
	// }

	// public void displayAllTrains() {
	// ArrayList<Train> allTrains = trainDAO.getTable_train();
	// if (allTrains.isEmpty()) {
	// System.out.println("No trains available.");
	// } else {
	// for (Train train : allTrains) {
	// System.out.println(train.toString());
	// }
	// }
	// }

	// BEGINNING OF ADMIN FUNCTIONS //

	public void manageTrainSchedule(Scanner scanner) {
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
					addTrain(scanner);
					break;

				case 2:
					removeTrain(scanner);
					break;

				case 3:
					updateTrain(scanner);
					break;

				case 4:
					System.out.println("\n--- All Available Trains ---");
					displayTrains_available();
					break;

				case 5:
					managing = false;
					break;

				default:
					System.out.println("Invalid option. Please try again.");
			}
		}
	}

	private void addTrain(Scanner scanner) {
		System.out.println("\n--- Add a New Train ---");

		// Change to auto gen train id according to number of trains in table
		// System.out.print("Enter Train ID: ");
		// String trainID = scanner.nextLine();

		// Train existingTrain = system.getTrainByNumber(trainID);
		// if (existingTrain != null) {
		// System.out.println("Train ID already exists. Please use a different ID.");
		// return;
		// }

		String trainID = ("trainId_" + (trainDAO.getTable_train().size() + 1));

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
		trainDAO.addTrain_fromTrainTable(newTrain);
		System.out.println("Train added successfully.");
	}

	private void removeTrain(Scanner scanner) {
		System.out.println("\n--- Remove an Existing Train ---");
		System.out.print("Enter Train ID to Delete: ");
		String trainID = scanner.nextLine();

		boolean isDeleted = trainDAO.deleteTrain_fromTrainTable(trainID);
		if (isDeleted) {
			System.out.println("Train deleted successfully.");
		} else {
			System.out.println("Train ID not found. Deletion failed.");
		}
	}

	private void updateTrain(Scanner scanner) {
		System.out.println("\n--- Update Train Details ---");
		System.out.print("Enter Train ID to Update: ");
		String trainID = scanner.nextLine();

		Train existingTrain = trainDAO.getTrain_fromTrainTable(trainID);
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

		trainDAO.updateTrain_fromTrainTable(existingTrain);
		System.out.println("Train details updated successfully.");
	}

	public void csAdmin(Scanner scanner) {
		boolean isStay = true;
		System.out.println(
				"\n=============================================================================================================");
		while (isStay) {
			boolean isKeyword = false;
			boolean isAnswer = false;
			String answer = null;
			String keyword = null;
			while (!isKeyword) {
				System.out.println("Enter keyword:");
				keyword = scanner.nextLine();
				if (keyword.isEmpty()) {
					System.out.println("Keyword cannot be empty.");
				} else if (keyword.equals("exit")) {
					isStay = false;
				} else {
					isKeyword = true;
				}
			}
			while (!isAnswer) {
				System.out.println("Enter answer:");
				answer = scanner.nextLine();
				if (answer.isEmpty()) {
					System.out.println("Answer cannot be empty.");
				} else if (answer.equals("exit")) {
					isStay = false;
				} else {
					isAnswer = true;
				}
			}
			System.out.println(addQA(keyword, answer));
		}
		System.out.println(
				"\n=============================================================================================================");
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
		if (customerServiceDAO.addQA(new CsQuestion(keyword, answer))) {
			returnWord = "QA added successfully.";
		} else {
			returnWord = "Failed to add QA.";
		}
		return returnWord;
	}
}