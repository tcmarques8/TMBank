package project.tmarque.TMBank;

public class SavingsAccount extends Account {

	public SavingsAccount(String label, Client client) {
		super(label, client);
		this.balance = 0;
	}
	
	public SavingsAccount(String label, double balance, int accountNumber, Client client) {
		super(label, balance, accountNumber, client);
	}

}
