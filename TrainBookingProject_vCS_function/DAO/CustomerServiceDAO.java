package DAO;

import java.util.*;
import DB_init.*;
import DataModel.*;

public class CustomerServiceDAO {
    private ArrayList<CsQuestion> table_train;
    
    public CustomerServiceDAO() {
        table_train = Database.getInstance().getTable_question();
    }

    public ArrayList<CsQuestion> getTable_question() {
        return table_train;
    }

    public boolean addQA(CsQuestion question) {
        if(table_train.add(question)){
            return true;
        }else{
            return false;
        }
    }
}
