import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulateDataSet {

	private static final int MAX_INT = 655556;
	private static AtomicInteger currID = new AtomicInteger(10000);
	private static AtomicInteger currTransNum = new AtomicInteger(0);
	
	public static BankTransaction generateTrans(DataSetProperty tuneDataSet) {
		BankTransaction newTrans = new BankTransaction();
		float amount;
		int TransID;
		
		/*
		 * Now get Trans ID which is atomically maintained to avoid
		 * data inconsistency.
		 */
		TransID = getUniqueTransID();
		newTrans.setTransID(TransID);
		
		/*
		 * Unique ID is generated for Bank Account numbers.
		 */
		newTrans.generateAccountNum();
	
		/*
		 * Note: Min and Max range is automatically adjusted according to property
		 * specified for high or low value transaction(s). 
		 */
		
		amount = generateTransAmount(tuneDataSet);
		newTrans.setTransAmount(amount);
		
		return newTrans;
		
	}
	
	public static float generateTransAmount(DataSetProperty tuneDataSet) {
		
	    int minAmtRange = 1;
	    int maxAmtRange = MAX_INT;
		float Threshold;
		float transAmt;
		Random random = new Random();
	    
		Threshold = tuneDataSet.getLowThreshold();
		
		/*
	     * As top value is exclusive hence 1 is added to it.
	     */
		transAmt = random.nextFloat() * (maxAmtRange - minAmtRange + 1) + minAmtRange;
		
		/*
		 * So if randomly generated amount is more  then threshold and has
		 * exceeded the limit of high value transactions then we need to re-adjust 
		 * the amount.
		 */
		if ((transAmt > Threshold) &&
				(currTransNum.get() > tuneDataSet.getHighValueLimit())) {

			maxAmtRange = (int) Threshold; 
			transAmt = random.nextFloat() * (maxAmtRange - minAmtRange + 1) + minAmtRange;
		}
		 
	    return transAmt;
	}
	

	public static int getUniqueTransID() {
		currTransNum.getAndIncrement();
	 	return currID.getAndIncrement();
	}
	
	public static void writeToFile(BankTransaction Trans) {
		 try {
	            FileWriter writer = new FileWriter("SimuatedDataSet.txt", true);
	            BufferedWriter bufferedWriter = new BufferedWriter(writer);
	            
	            int trans = new Integer((int)Trans.getTransID());
	            float amount = new Float(Trans.getAmount());
	            int account = new Integer((int)Trans.getAccountNum());
	            
	            String writeStr = "TransID: " + Integer.toString(trans) + " " + 
	            		"AccountNo: " + Integer.toString(account) + " " +
	            		"Amount: " + Float.toString(amount);
	            
	            System.out.println(writeStr);	            
	            bufferedWriter.write(writeStr);
	            bufferedWriter.newLine();
	            bufferedWriter.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	public static void parseTransProperties(DataSetProperty tuneDataSet) {
		
		try {
			
			/*
			 * some sanity cleanups are done here.
			 */
			FileWriter outputFile = new FileWriter("SimuatedDataSet.txt");
			outputFile.write("");
			outputFile.close();
			
			FileInputStream fileInput = new FileInputStream("src/DataSet.properties");
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();
			
			/*
			 * Run through the keys and take appropriate action on transactions.
			 */
			Enumeration<Object> numKeys = properties.keys();
			while (numKeys.hasMoreElements()) {
				String key = (String) numKeys.nextElement();
				String value = properties.getProperty(key);
				tuneDataSet.setTransProperty(key, value);
				System.out.println(key + ": " + value);
			}
			
		} catch (FileNotFoundException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		
		/*
		 * Get the Properties for simulation of data set.
		 */
		DataSetProperty tuneDataSet = new DataSetProperty();
		parseTransProperties(tuneDataSet);

		/*
		 * Now generate the transactions as per specified tunables.
		 */	
		int counter = tuneDataSet.getTotalNumOfTrans();
		while (counter > 0) {
			BankTransaction Trans = new BankTransaction();
			Trans = generateTrans(tuneDataSet);
			writeToFile(Trans);
			counter--;
		}
	}	
}
