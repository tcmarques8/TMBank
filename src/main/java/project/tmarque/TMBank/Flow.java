package project.tmarque.TMBank;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Debit.class, name = "Debit"),
    @JsonSubTypes.Type(value = Credit.class, name = "Credit"),
    @JsonSubTypes.Type(value = Transfer.class, name = "Transfer")
})

public abstract class Flow {
	private String comment;
	private int identifier;
	private double amount;
	private int targetAccountnumber;
	private boolean effect;
	private LocalDate date;
	private static int count = 1;
	
	public Flow(String comment, double amount, int target) {
		this.comment = comment;
		this.identifier = count++;
		this.amount = amount;
		this.targetAccountnumber = target;
		this.date = LocalDate.now().plusDays(2);
	}
	
	public Flow(String comment, double amount, int targetAccountNumber, boolean effect, String date) {
		this.comment = comment;
		this.identifier = count++;
		this.amount = amount;
		this.targetAccountnumber = targetAccountNumber;
		this.effect = effect;
		this.date = LocalDate.parse(date).plusDays(2);
	}

	public String getComment() {
		return comment;
	}

	public int getIdentifier() {
		return identifier;
	}

	public int getTargetAccountnumber() {
		return targetAccountnumber;
	}

	public boolean isEffect() {
		return effect;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public double getAmount() {
		return amount;
	}

	public void setTargetAccountnumber(int targetAccountnumber) {
		this.targetAccountnumber = targetAccountnumber;
	}

	public void setEffect(boolean effect) {
		this.effect = effect;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	
}
