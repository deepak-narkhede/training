import java.util.Random;

public class BankTransaction {
	private	double transID;
	private int accountNum;
	private	float transAmount;

	public void generateAccountNum() {
		Random randomNum = new Random();
		accountNum = 100000 + randomNum.nextInt(900000);
	}

	public double getTransID() {
		return transID;
	}

	public int getAccountNum() {
		return accountNum;
	}
	
	public float getAmount() {
		return transAmount;
	}

	public void setTransID(int TransID) {
		transID = TransID;
		
	}

	public void setTransAmount(float amount) {
		transAmount = amount;	
	}

}
