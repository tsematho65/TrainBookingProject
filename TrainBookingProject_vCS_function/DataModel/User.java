package DataModel;

import java.util.ArrayList;

public class User {
    private String id;
    private String username;
    private String password;
    private String role;
    private String status;
    private ArrayList<OrderRecord> orderRecordList;

    public User(String role, String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        orderRecordList = new ArrayList<OrderRecord>();
        status = "active";
    }

    // Getters and setters
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
	public ArrayList<OrderRecord> getOrderRecordList() {
		return orderRecordList;
	}
	
	public void setOrderRecordList(ArrayList<OrderRecord> orderRecordList) {
		this.orderRecordList = orderRecordList;
	}

	public void addOrderRecord(OrderRecord orderRecord) {
		orderRecordList.add(orderRecord);
	}

	public void removeOrderRecord(OrderRecord orderRecord) {
		orderRecordList.remove(orderRecord);
	}

	public void updateOrderRecord(OrderRecord orderRecord) {
        for (OrderRecord or : orderRecordList) {
            if (or.getOrderId().equals(orderRecord.getOrderId())) {
                or = orderRecord;
            }
        }
    }

	public OrderRecord getOrderRecordById(String orderId) {
		for (OrderRecord orderRecord : orderRecordList) {
            if (orderRecord.getOrderId().equals(orderId)) {
                return orderRecord;
            }
        }
        return null;
	}

}