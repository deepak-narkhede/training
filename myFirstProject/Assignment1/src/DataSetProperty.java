
public class DataSetProperty {
	private float lowThresholdAmt;
	private int totalNumOfTrans;
	private int numOfhighValueTrans;
	private int numOfLowValueTrans;
	
	public void setLowThreshold(float lowThreshold) {
		lowThresholdAmt = lowThreshold;
	}
	
	public void setTotalTransactions(int totalTrans) {
		totalNumOfTrans = totalTrans;
	}
	
	public void setNumOfTransLimit(int highValuePer) {
		numOfhighValueTrans = (highValuePer * totalNumOfTrans)/100;
		numOfLowValueTrans = totalNumOfTrans - numOfhighValueTrans;
	}
	
	public float getLowThreshold() {
		return lowThresholdAmt;
	}
	
	public void setTransProperty(String key, String value) {
		switch (key) {
			case "LowThreshold":
				float setLow = Float.parseFloat(value);
				this.setLowThreshold(setLow);
				break;
			case "highValuePercentage":
				int highValue = Integer.parseInt(value);
				this.setNumOfTransLimit(highValue);
				break;
			case "TotalNumofTrans":
				int setTotal = Integer.parseInt(value);
				this.setTotalTransactions(setTotal);
				break;
			default:
				throw new IllegalArgumentException("Invalid key element: " + key);
		}
	}

	public int getTotalNumOfTrans() {
		return totalNumOfTrans;
	}

	public int getHighValueLimit() {
		return numOfhighValueTrans;
	}
	
	
}
