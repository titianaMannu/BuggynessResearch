package classification.arff;

public class FileBean {
	
	private int versionId;
	
	private String versionName;
	
	private String fileName;
	
	private long size;
	
	private long lOCTouched; 
	
	private long nr; // revision Number
	
	private long authNum;
	
	private long churn;
	
	private long avgChurn;
	
	private long maxChurn;
	
	private long changeSetSize;
	
	private long maxChangeSetSize;
	
	private long avgChangeSetSize;
	
	private long age;
	
	private boolean defectiveness;

	public int getVersionId() {
		return versionId;
	}

	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getlOCTouched() {
		return lOCTouched;
	}

	public void setlOCTouched(long lOCTouched) {
		this.lOCTouched = lOCTouched;
	}

	public long getNr() {
		return nr;
	}

	public void setNr(long nr) {
		this.nr = nr;
	}

	public long getAuthNum() {
		return authNum;
	}

	public void setAuthNum(long authNum) {
		this.authNum = authNum;
	}

	public long getChurn() {
		return churn;
	}

	public void setChurn(long churn) {
		this.churn = churn;
	}

	public long getAvgChurn() {
		return avgChurn;
	}

	public void setAvgChurn(long avgChurn) {
		this.avgChurn = avgChurn;
	}

	public long getMaxChurn() {
		return maxChurn;
	}

	public void setMaxChurn(long maxChurn) {
		this.maxChurn = maxChurn;
	}

	public long getChangeSetSize() {
		return changeSetSize;
	}

	public void setChangeSetSize(long changeSetSize) {
		this.changeSetSize = changeSetSize;
	}

	public long getMaxChangeSetSize() {
		return maxChangeSetSize;
	}

	public void setMaxChangeSetSize(long maxChangeSetSize) {
		this.maxChangeSetSize = maxChangeSetSize;
	}

	public long getAvgChangeSetSize() {
		return avgChangeSetSize;
	}

	public void setAvgChangeSetSize(long avgChangeSetSize) {
		this.avgChangeSetSize = avgChangeSetSize;
	}

	public long getAge() {
		return age;
	}

	public void setAge(long age) {
		this.age = age;
	}

	public boolean isDefectiveness() {
		return defectiveness;
	}

	public void setDefectiveness(boolean defectiveness) {
		this.defectiveness = defectiveness;
	}

	@Override
	public String toString() {
		return "FileBean [versionId=" + versionId + ", versionName=" + versionName + ", fileName=" + fileName
				+ ", size=" + size + ", lOCTouched=" + lOCTouched + ", nr=" + nr + ", authNum=" + authNum + ", churn="
				+ churn + ", avgChurn=" + avgChurn + ", maxChurn=" + maxChurn + ", changeSetSize=" + changeSetSize
				+ ", maxChangeSetSize=" + maxChangeSetSize + ", avgChangeSetSize=" + avgChangeSetSize + ", age=" + age
				+ ", defectiveness=" + defectiveness + "]";
	}
	
	
}
