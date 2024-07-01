package project.tmarque.TMBank;

public class Client {
	private String firstName;
	private String lastName;
	private int clientNumber;
	private static int count = 1;

	public Client(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.clientNumber = count++;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getClientNumber() {
		return clientNumber;
	}

	@Override
	public String toString() {
		return "Client Number - " + this.clientNumber + " || Name - " + this.firstName + " " + this.lastName;
	}
}
