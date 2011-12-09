package org.openhds.datageneration.utils;

public class DataGeneratorUpdateParams {

	private final int minNumberOfEventsPerVisit;
	private final int maxNumberOfEventsPerVisit;
	private final int numberOfInternalInMigrations;
	private final int numberOfExternalInMigrations;
	private final int numberOfOutMigrations;
	private final int numberOfDeaths;
	private final int numberOfPregnancyObservations;
	private final int numberOfPregnancyOutcomes;
	private final UpdateEventGenerationStrategy updateStrategy;
	
	public enum UpdateEventGenerationStrategy {
		BRUTE_FORCE
	}
	
	private DataGeneratorUpdateParams(Builder builder) {
		this.minNumberOfEventsPerVisit = builder.minNumberOfEventsPerVisit;
		this.maxNumberOfEventsPerVisit = builder.maxNumberOfEventsPerVisit;
		this.numberOfInternalInMigrations = builder.numberOfInternalInMigrations;
		this.numberOfExternalInMigrations = builder.numberOfExternalInMigrations;
		this.numberOfOutMigrations = builder.numberOfOutMigrations;
		this.numberOfDeaths = builder.numberOfDeaths;
		this.numberOfPregnancyObservations = builder.numberOfPregnancyObservations;
		this.numberOfPregnancyOutcomes = builder.numberOfPregnancyOutcomes;
		this.updateStrategy = builder.updateStrategy;
	}
	
	public int getMinNumberOfEventsPerVisit() {
		return minNumberOfEventsPerVisit;
	}

	public int getMaxNumberOfEventsPerVisit() {
		return maxNumberOfEventsPerVisit;
	}

	public int getNumberOfInternalInMigrations() {
		return numberOfInternalInMigrations;
	}

	public int getNumberOfExternalInMigrations() {
		return numberOfExternalInMigrations;
	}

	public int getNumberOfOutMigrations() {
		return numberOfOutMigrations;
	}

	public int getNumberOfDeaths() {
		return numberOfDeaths;
	}

	public int getNumberOfPregnancyObservations() {
		return numberOfPregnancyObservations;
	}

	public int getNumberOfPregnancyOutcomes() {
		return numberOfPregnancyOutcomes;
	}

	public UpdateEventGenerationStrategy getUpdateStrategy() {
		return updateStrategy;
	}

	public static class Builder {
		private int minNumberOfEventsPerVisit = 1;
		private int maxNumberOfEventsPerVisit = 10;
		private int numberOfInternalInMigrations = 1000;
		private int numberOfExternalInMigrations = 1000;
		private int numberOfOutMigrations = 1000;
		private int numberOfDeaths = 1000;
		private int numberOfPregnancyObservations = 1000;
		private int numberOfPregnancyOutcomes = 1000;
		private UpdateEventGenerationStrategy updateStrategy = UpdateEventGenerationStrategy.BRUTE_FORCE;
		
		public DataGeneratorUpdateParams build() {
			return new DataGeneratorUpdateParams(this);
		}
		
		public Builder minNumberOfEventsPerVisit(int minNumberOfEventsPerVisit) {
			this.minNumberOfEventsPerVisit = minNumberOfEventsPerVisit;
			return this;
		}
		
		public Builder maxNumberOfEventsPerVisit(int maxNumberOfEventsPerVisit) {
			this.maxNumberOfEventsPerVisit = maxNumberOfEventsPerVisit;
			return this;
		}
		
		public Builder numberOfInternalInMigrations(int numberOfInternalInMigrations) {
			this.numberOfInternalInMigrations = numberOfInternalInMigrations;
			return this;
		}
		
		public Builder numberOfExternalInMigrations(int numberOfExternalInMigrations) {
			this.numberOfExternalInMigrations = numberOfExternalInMigrations;
			return this;
		}
		
		public Builder numberOfOutMigrations(int numberOfOutMigrations) {
			this.numberOfOutMigrations = numberOfOutMigrations;
			return this;
		}
		
		public Builder numberOfDeaths(int numberOfDeaths) {
			this.numberOfDeaths = numberOfDeaths;
			return this;
		}
		
		public Builder numberOfPregnancyObservations(int numberOfPregnancyObservations) {
			this.numberOfPregnancyObservations = numberOfPregnancyObservations;
			return this;
		}
		
		public Builder numberOfPregnancyOutcomes(int numberOfPregnancyOutcomes) {
			this.numberOfPregnancyOutcomes = numberOfPregnancyOutcomes;
			return this;
		}
		
		public Builder updateStrategy(UpdateEventGenerationStrategy updateStrategy) {
			this.updateStrategy = updateStrategy;
			return this;
		}
	}
}
