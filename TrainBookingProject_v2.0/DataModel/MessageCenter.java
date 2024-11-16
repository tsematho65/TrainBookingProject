package DataModel;

import java.util.ArrayList;
import java.util.List;

public class MessageCenter implements SubjectMessageCenter {
    private List<MessageObserver> observers;
    private String announcement;

    public MessageCenter() {
        observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(MessageObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(MessageObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (MessageObserver observer : observers) {
            observer.update(announcement);
        }
    }

    public void updateAnnouncement(String announcement) {
        this.announcement = announcement;
        notifyObservers();
    }
}
