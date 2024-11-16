package DataModel;

public interface SubjectMessageCenter {
    void registerObserver(MessageObserver o);

    void removeObserver(MessageObserver o);

    void notifyObservers();

}
