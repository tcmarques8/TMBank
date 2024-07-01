package project.tmarque.TMBank;

public class Transfer extends Flow {
	private int accountNumber;

	public Transfer(String comment, double amount, int target, int accountNumber) {
		super(comment, amount, target);
		this.accountNumber = accountNumber;
	}
	
	public Transfer(String comment, double amount, int targetAccountNumber, boolean effect, String date, int accountNumber) {
		super(comment, amount, targetAccountNumber, effect, date);
		this.accountNumber = accountNumber;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	@Override
	public String toString() {
		return "Transfer - " + super.getComment() + " || " + super.getAmount() + " || From Account - " + this.accountNumber + " || To Account - " + super.getTargetAccountnumber() + " || " + super.getDate();
	}

}
