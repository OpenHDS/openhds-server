package org.openhds.report.beans;

public class MortalityRecordBean {
	
	String ageGroupName;
	
	double neoNatalMale;
	double neoNatalFemale;
	
	double postNatalMale;
	double postNatalFemale;
	
	double infantMale;
	double infantFemale;
	
	double totalMaleOutcomes;
	double totalFemaleOutcomes;
	
	double min;
	double max;

	public MortalityRecordBean(String ageGroupName, double min, double max) {
		this.ageGroupName = ageGroupName;
		this.min = min;
		this.max = max;
	}
	
	public void addNeoNatalMale() {
		neoNatalMale++;
	}
	
	public void addNeoNatalFemale() {
		neoNatalFemale++;
	}
	
	public void addPostNatalMale() {
		postNatalMale++;
	}
	
	public void addPostNatalFemale() {
		postNatalFemale++;
	}
	
	public void addInfantMale() {
		infantMale++;
	}
	
	public void addInfantFemale() {
		infantFemale++;
	}

	public double getNeoNatalMale() {
		return neoNatalMale;
	}

	public void setNeoNatalMale(double neoNatalMale) {
		this.neoNatalMale = neoNatalMale;
	}

	public double getNeoNatalFemale() {
		return neoNatalFemale;
	}

	public void setNeoNatalFemale(double neoNatalFemale) {
		this.neoNatalFemale = neoNatalFemale;
	}

	public double getPostNatalMale() {
		return postNatalMale;
	}

	public void setPostNatalMale(double postNatalMale) {
		this.postNatalMale = postNatalMale;
	}

	public double getPostNatalFemale() {
		return postNatalFemale;
	}

	public void setPostNatalFemale(double postNatalFemale) {
		this.postNatalFemale = postNatalFemale;
	}
	
	public double getInfantMale() {
		return infantMale;
	}

	public void setInfantMale(double infantMale) {
		this.infantMale = infantMale;
	}

	public double getInfantFemale() {
		return infantFemale;
	}

	public void setInfantFemale(double infantFemale) {
		this.infantFemale = infantFemale;
	}

	
	public double getTotalMaleOutcomes() {
		return totalMaleOutcomes;
	}

	public void setTotalMaleOutcomes(double totalMaleOutcomes) {
		this.totalMaleOutcomes = totalMaleOutcomes;
	}

	public double getTotalFemaleOutcomes() {
		return totalFemaleOutcomes;
	}

	public void setTotalFemaleOutcomes(double totalFemaleOutcomes) {
		this.totalFemaleOutcomes = totalFemaleOutcomes;
	}
	
	public double getTotalOutcomes() {
		return totalMaleOutcomes + totalFemaleOutcomes;
	}
		
	public double getNeoNatalTotal() {
		return neoNatalMale + neoNatalFemale;
	}

	public double getPostNatalTotal() {
		return postNatalMale + postNatalFemale;
	}
	
	public double getInfantTotal() {
		return infantMale + infantFemale;
	}
	
	public double getNeoNatalMaleRatio() {
		double total = getTotalMaleOutcomes();
		if (total != 0)
			return ((neoNatalMale * 1000) / total);
		return 0;
	}
	
	public double getNeoNatalFemaleRatio() {
		double total = getTotalFemaleOutcomes();
		if (total != 0) {
			double value = ((neoNatalFemale * 1000) / total);
			return value;
		}
		return 0;
	}
	
	public double getNeoNatalRatioTotal() {
		double total = getTotalOutcomes();
		if (total != 0)
			return ((neoNatalMale + neoNatalFemale * 1000) / total);
		return 0;
	}
	
	public double getPostNatalMaleRatio() {
		double total = getTotalMaleOutcomes();
		if (total != 0)
			return ((postNatalMale * 1000) / total);
		return total;
	}
	
	public double getPostNatalFemaleRatio() {
		double total = getTotalFemaleOutcomes();
		if (total != 0) 
			return ((postNatalFemale * 1000) / total);
		return 0;
	}
	
	public double getPostNatalRatioTotal() {
		double total = getTotalOutcomes();
		if (total != 0)
			return ((postNatalMale + postNatalFemale * 1000) / total);
		return 0;
	}
	
	public double getInfantMaleRatio() {
		double total = getTotalMaleOutcomes();
		if (total != 0)
			return ((infantMale * 1000) / total);
		return total;
	}
	
	public double getInfantFemaleRatio() {
		double total = getTotalFemaleOutcomes();
		if (total != 0) 
			return ((infantFemale * 1000) / total);
		return 0;
	}
	
	public double getInfantRatioTotal() {
		double total = getTotalOutcomes();
		if (total != 0)
			return ((infantMale + infantFemale * 1000) / total);
		return 0;
	}
	
	public String getAgeGroupName() {
		return ageGroupName;
	}

	public void setAgeGroupName(String ageGroupName) {
		this.ageGroupName = ageGroupName;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}
}
