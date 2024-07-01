package project.tmarque.TMBank;

public abstract class Account {
	protected String label;
	protected double balance;
	protected int accountNumber;
	protected Client client;
	private static int count = 1;
	
	public Account(String label, Client client) {
		this.label = label;
		this.client = client;
		this.accountNumber = count++;
	}
	
	public Account(String label, double balance, int accountNumber, Client client) {
		this.label = label;
		this.balance = balance;
		this.accountNumber = accountNumber;
		this.client = client;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(Flow flow) {
		if (flow instanceof Credit) {
			this.balance += flow.getAmount();
		} else if (flow instanceof Debit) {
			this.balance -= flow.getAmount();
		} else if (flow instanceof Transfer) {
			Transfer transfer = (Transfer) flow;
			if (transfer.getAccountNumber() == this.accountNumber) {
				this.balance -= flow.getAmount();
			} 
			if (transfer.getTargetAccountnumber() == this.accountNumber) {
				this.balance -= flow.getAmount();
			}
		}
	}

	public int getAccountNumber() {
		return accountNumber;
	}
	public Client getClient() {
		return client;
	}
	
	@Override
	public String toString() {
		return "Account Number - " + this.accountNumber + " || " + this.client + " || " + this.label + " || Balance - " + this.balance;
	}
}
