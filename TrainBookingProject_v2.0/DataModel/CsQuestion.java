package DataModel;
import java.util.*;
public class CsQuestion {
    private ArrayList<String> questionList;
    private String answer;

    public CsQuestion(ArrayList<String> questionList, String answer) {
        this.questionList = questionList;
        this.answer = answer;
    }

    public ArrayList<String> getQuestion() {
        return questionList;
    }

    public void setQuestion(String question) {
        questionList.add(question);
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
