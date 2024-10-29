
package DB_init;

import DataModel.*;
import java.util.*;

public class Database {

    
    private static Database instance = null;
    
   
    private ArrayList<User> table_user;
    private ArrayList<Train> table_train;
    private ArrayList<OrderRecord> table_orderRecord;
    private ArrayList<Passenger> table_passenger;
    private ArrayList<Ticket> table_ticket; 

   
    private Database() {
        table_user = new ArrayList<>();
        table_train = new ArrayList<>();
        table_orderRecord = new ArrayList<>();
        table_passenger = new ArrayList<>();
        table_ticket = new ArrayList<>();
        DB_initialize();
    }

 
    private void DB_initialize() {
 
        table_user.add(new User("admin", "userID_1", "admin", "admin"));
        table_user.add(new User("normal", "userID_2", "q", "q"));


        table_train.add(new Train("trainID_1", "LA", "Chicago", "2024-10-01", "12:00", 20, 1000));
        table_train.add(new Train("trainID_2", "Washington DC", "Miami", "2024-10-02", "14:00", 20, 1500));
        table_train.add(new Train("trainID_3", "Washington DC", "Miami", "2024-10-02", "14:00", 20, 1500));
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