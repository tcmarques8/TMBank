package project.tmarque.TMBank;

public class CurrentAccount extends Account {

	public CurrentAccount(String label, Client client) {
		super(label, client);
		this.balance = 0;
	}
	
	public CurrentAccount(String label, double balance, int accountNumber, Client client) {
		super(label, balance, accountNumber, client);
	}

}
