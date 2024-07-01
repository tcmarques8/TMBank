package project.tmarque.TMBank;

public class Debit extends Flow {

	public Debit(String comment,  double amount, int target) {
		super(comment, amount, target);
	}
	
	public Debit(String comment, double amount, int targetAccountNumber, boolean effect, String date) {
		super(comment, amount, targetAccountNumber, effect, date);
	}
	
	@Override
	public String toString() {
		return "Debit - " + super.getComment() + " || " + super.getAmount() + " || " + super.getDate();
	}

}
