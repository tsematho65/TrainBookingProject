package DB_init;

import DataModel.*;

import java.util.*;

public class Database {

    // Singleton
    private static Database instance = null;
    
    // DB contains all tables right here:
    private ArrayList<User> table_user;
    private ArrayList<Train> table_train;
    private ArrayList<OrderRecord> table_orderRecord;
    private ArrayList<Passenger> table_passenger;
    private ArrayList<CsQuestion> table_question;
    private ArrayList<Ticket> table_ticket; 

    // ID values reference for each table


    private Database() {
        table_user = new ArrayList<>();
        table_train = new ArrayList<>();
        table_orderRecord = new ArrayList<>();
        table_passenger = new ArrayList<>();
        table_question = new ArrayList<>();
        DB_initialize();
    }

    private void DB_initialize() {
        table_user.add(new User("admin", "userID_1", "admin", "admin"));
        table_user.add(new User("normal", "userID_2", "q", "q"));

        table_train.add(new Train("trainID_1", "LA", "Chicago", "2024-10-01", "12:00", 20, 1000));
        table_train.add(new Train("trainID_2", "Washington DC", "Miami", "2024-10-02", "14:00", 20, 1500));
        table_train.add(new Train("trainID_3", "Washington DC", "Miami", "2024-10-02", "14:00", 20, 1500));

        table_question.add(new CsQuestion("book ticket", "To book a ticket, you should press 1 in the main menu after login, then follow the instructions to do booking."));
        table_question.add(new CsQuestion("edit ticket", "To edit a ticket, you should press 3 in the main menu after login, then follow the instructions to edit."));
        table_question.add(new CsQuestion("check ticket", "To check a ticket, you should press 2 in the main menu after login, then you can see all ticket you booked."));
        table_question.add(new CsQuestion("cancel ticket", "To cancel a ticket, you should press 4 in the main menu after login, then follow the instructions to cancel."));
        table_question.add(new CsQuestion("edit profile", "To edit your profile, you should press 5 in the main menu after login."));
        table_question.add(new CsQuestion("problem", "If you have any problem, please press 7 to contact customer service."));
        table_question.add(new CsQuestion("customer service", "If you have any problem, please press 7 to contact customer service."));
        table_question.add(new CsQuestion("help", "If you have any problem, please press 7 to contact customer service."));
        table_question.add(new CsQuestion("contact method", "How to find us : email : 123@123.com, phone : 1234567890"));
    }

    public ArrayList<User> getTable_user() {   
        return getInstance().table_user;
    }
    public ArrayList<Train> getTable_train() {   
        return getInstance().table_train;
    }
    public ArrayList<OrderRecord> getTable_orderRecord() {   
        return getInstance().table_orderRecord;
    }
    public ArrayList<CsQuestion> getTable_question() {   
        return getInstance().table_question;
    }
    public ArrayList<Ticket> getTable_ticket() { 
        return getInstance().table_ticket;
    }


    public static Database getInstance () {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

}