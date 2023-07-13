package model;

public class Message {
	private int id;
	private String guestName;
	private String password;
	private String message;
	public int getId() {
		return id;
	}
	public String getGuestName() {
		return guestName;
	}
	public String getPassword() {
		return password;
	}
	public String getMessage() {
		return message;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public boolean hasPassword() {
		return password != null && !password.isEmpty();
	}
	
}
