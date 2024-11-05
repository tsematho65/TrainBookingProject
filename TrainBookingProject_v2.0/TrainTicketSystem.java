import java.time.LocalDate;
import java.util.*;

import DAO.*;
import DataModel.*;

public class TrainTicketSystem {
    private static TrainTicketSystem instance = null;
    private UserDAO userDAO;
    private TrainDAO trainDAO;
	private CustomerServiceDAO customerServiceDAO;
	private User currentUser;

    private TrainTicketSystem() {
        userDAO = new UserDAO();
        trainDAO = new TrainDAO();
		customerServiceDAO = new CustomerServiceDAO();
    }

    public static TrainTicketSystem getInstance() {
        if (instance == null) {
            instance = new TrainTicketSystem();
        }
        return instance;
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
		ArrayList<OrderRecord> orderRecordList = userDAO.getUser_fromUserTable(id).getOrderRecordList();
		
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
			System.out.println("\nFinished Orders:");
			System.out.println("===============================================");
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
			System.out.println("===============================================");
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
	    ArrayList<OrderRecord> orderRecordList = userDAO.getUser_fromUserTable(id).getOrderRecordList();

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
		System.out.println(recommendedTrainIds.size());
		if (recommendedTrainIds.isEmpty()) {
			System.out.println("No recommendations available.");
		} else {
			System.out.println("\n=============================================================================================================");
			System.out.println("Top 3 Train Recommendations:");
			ArrayList<Train> availableTrains = new ArrayList<>();
			for (int i = 0; i < recommendedTrainIds.size(); i++) {
				Train train = trainDAO.getTrain_fromTrainTable(recommendedTrainIds.get(i));
				
				if (train.getStatus().equals("active") && train.getAvailableSeats() > 0) {
					availableTrains.add(train);
				}
			}
			
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
			System.out.println("\n=============================================================================================================");
		}
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

//  fn to order tickets
//  ...
}
