package org.openhds.report.beans;

public class MaritalRecordBean extends ReportRecordBean {

	String name;
	Long count;
	Double rate;
	
	public MaritalRecordBean() { }

	public MaritalRecordBean(String name, Long count, Long denominator) {
		this.name = name;
		this.count = count;
		if (denominator == 0) {
			rate = 0.0;
		}
		else {
			this.rate = (double) (count / denominator);
		}
	}

	public MaritalRecordBean(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}
}
