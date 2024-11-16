package DataModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class User implements MessageObserver {

    private String id;
    private String username;
    private String password;
    private String role;
    private Member member;
    private int points;
    private LocalDate lastSignInDate;
    private ArrayList<OrderRecord> orderRecordList;
    private ArrayList<Coupon> couponList;
    // private static final int POINTS_PER_SIGN_IN = 10;

    private String latestAnnouncement;

    public User(String role, String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.member = new NormalMember();
        orderRecordList = new ArrayList<OrderRecord>();
        lastSignInDate = null;
        points = 0;
        latestAnnouncement = null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getMemberType() {
        return member.getMemberType();
    }

    public double getDiscount() {
        return member.getDiscount();
    }

    public LocalDate getLastSignInDate() {
        return lastSignInDate;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public void setLastSignInDate(LocalDate lastSignInDate) {
        this.lastSignInDate = lastSignInDate;
    }

    public void addCoupon(Coupon coupon) {
        couponList.add(coupon);
    }

    public void removeCoupon(Coupon coupon) {
        couponList.remove(coupon);
    }

    public ArrayList<Coupon> getCouponList() {
        return couponList;
    }

    public Member getMember() {
        return member;
    }

    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role + ", member="
                + member + ", points=" + points + ", lastSignInDate=" + lastSignInDate + ", orderRecordList="
                + orderRecordList + "]";
    }

    public String getLatestAnnouncement() {
        return latestAnnouncement;
    }

    // update the latest announcement from Admin
    @Override
    public void update(String message) {
        latestAnnouncement = message;
    }

    public boolean isReceivedAnnouncement() {
        return latestAnnouncement != null;
    }

    // Moved to TrainTicketSystem.java
    // public boolean signIn() {
    // LocalDate today = LocalDate.now();
    // if (lastSignInDate != null && lastSignInDate.equals(today)) {
    // System.out.println("You have already signed in today. Please come back
    // tomorrow.");
    // return false;
    // }

    // lastSignInDate = today;

    // points += POINTS_PER_SIGN_IN;

    // if (isWinningSignIn()) {
    // System.out.println(username + " signIn sucessful, you get " +
    // POINTS_PER_SIGN_IN + " points and win a prize!");
    // } else {
    // System.out.println(username + " signIn sucessful, you get " +
    // POINTS_PER_SIGN_IN + "points but no prize. ");
    // }

    // return true;
    // }

    // private boolean isWinningSignIn() {

    // Random random = new Random();
    // return random.nextInt(10) < 1;
    // }

    // Already exist in OrderRecordDAO
    // public ArrayList<OrderRecord> getOrderRecordList() {
    // return orderRecordList;
    // }

    // public void setOrderRecordList(ArrayList<OrderRecord> orderRecordList) {
    // this.orderRecordList = orderRecordList;
    // }

    // public void addOrderRecord(OrderRecord orderRecord) {
    // orderRecordList.add(orderRecord);
    // }

    // public void removeOrderRecord(OrderRecord orderRecord) {
    // orderRecordList.remove(orderRecord);
    // }

    // public void updateOrderRecord(OrderRecord orderRecord) {
    // for (OrderRecord or : orderRecordList) {
    // if (or.getOrderId().equals(orderRecord.getOrderId())) {
    // or = orderRecord;
    // }
    // }
    // }

    // public OrderRecord getOrderRecordById(String orderId) {
    // for (OrderRecord orderRecord : orderRecordList) {
    // if (orderRecord.getOrderId().equals(orderId)) {
    // return orderRecord;
    // }
    // }
    // return null;
    // }

}