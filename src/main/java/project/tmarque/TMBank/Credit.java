package project.tmarque.TMBank;

public class Credit extends Flow {

	public Credit(String comment, double amount, int target) {
		super(comment, amount, target);
	}

	public Credit(String comment, double amount, int targetAccountNumber, boolean effect, String date) {
		super(comment, amount, targetAccountNumber, effect, date);
	}
	
	@Override
	public String toString() {
		return "Credit - " + super.getComment() + " || " + super.getAmount() + " || " + super.getDate();
	}

}
