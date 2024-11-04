package DataModel;
import java.util.*;

public class seatPlan {
    private String trainID;
    private ArrayList<String> seatList;
    private int seatCount;

    public seatPlan(String trainID) {
        this.trainID = trainID;
        seatCount = 24;

        this.seatList = new ArrayList<String>();
        String[] rows = {"A", "B", "C", "D"};

        for (int i = 0; i < 4 ; i++) { // by default, each train has 24 seats, 4 rows and 6 columns
            for (int j = 1; j <= 6; j++) {
                String seatIndex = rows[i] + j;
                this.seatList.add(seatIndex);
            }
        }
    }

    public String getTrainID() {
        return trainID;
    }

    public ArrayList<String> getSeatList() {
        return seatList;
    }

    public void setTrainID(String trainID) {
        this.trainID = trainID;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int val) {
        seatCount = val;
    }

    public void setSeatList(ArrayList<String> seatList) {
        this.seatList = seatList;
    }

    public void updateSeat(int index, String value) {
        seatList.set(index, value);
    }

    public String toString() {
        String result = "";
        result += "\nTrain ID: " + trainID + "\n";
        result += "Seat List: \n";

        for (int i = 1; i <= seatList.size(); i++) {
            result += (seatList.get(i-1));
            
            if (i % 6 == 0) {
                result += "\n";
            
            } else {
                result += " | ";
            }
        }
        
        return result;
    }

}
