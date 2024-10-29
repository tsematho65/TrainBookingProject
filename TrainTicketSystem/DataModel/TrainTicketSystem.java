// TrainTicketSystem.java
package DataModel;

import java.util.ArrayList;
import DAO.*;

public class TrainTicketSystem {
    private static TrainTicketSystem instance = null;
    private UserDAO userDAO;
    private TrainDAO trainDAO;
    private TicketDAO ticketDAO;
    private OrderRecordDAO orderRecordDAO;

    private TrainTicketSystem() {
        userDAO = new UserDAO();
        trainDAO = new TrainDAO();
        ticketDAO = new TicketDAO();
        orderRecordDAO = new OrderRecordDAO();
    }

    public static TrainTicketSystem getInstance() {
        if (instance == null) {
            instance = new TrainTicketSystem();
        }
        return instance;
    }

    public User login(String username, String password) {
        User user = userDAO.user_login(username, password);
        if (user != null ) {
            System.out.println("Login successful.");
        } else {
            System.out.println("Invalid username or password.");
        }
        
        System.out.println("");
        return user;
    }

    public boolean register(String username, String password){

        for (User user : userDAO.getTable_user()) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        User user = new User("normal", "userID_" + userDAO.getTable_user().size(), username, password);
        userDAO.addUser_fromUserTable(user);
        return true;
    }

    public int displayTrains_available() {
        ArrayList<Train> availableTrainTable = trainDAO.getTable_train();
        int availableTrain_count = 0;

        for (int i = 0; i < availableTrainTable.size(); i++) {
            Train train = availableTrainTable.get(i);
            if(train.getStatus().equals("active") && train.getAvailableSeats() > 0 ) {
                availableTrain_count++;
                System.out.println(i + ": " + train.toString());
            }
        }
        return availableTrain_count;
    }

    public ArrayList<Train> getTrainTable(){
        return trainDAO.getTable_train();
    }

    public ArrayList<OrderRecord> getOrdersByUserId(int userId) {
        return orderRecordDAO.getOrdersByUserId(userId);
    }

    public OrderRecord getOrderById(int orderId) {
        return orderRecordDAO.getOrderById(orderId);
    }

    public boolean updateOrderRecord(OrderRecord updatedOrder) {
        return orderRecordDAO.updateOrderRecord(updatedOrder);
    }

    public boolean deleteOrderRecord(int orderId) {
        return orderRecordDAO.deleteOrderRecord(orderId);
    }


}