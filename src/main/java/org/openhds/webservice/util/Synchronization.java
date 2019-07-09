package org.openhds.webservice.util;
import java.io.Serializable;
import java.util.List;

public class Synchronization implements Serializable {

	private static final long serialVersionUID = -5222233547014252574L;
	private long syncTime;
	private List<SynchronizationError> errors;
	
	public long getSyncTime() {
		return syncTime;
	}
	
	public void setSyncTime(long syncTime) {
		this.syncTime = syncTime;
	}
	
	public List<SynchronizationError> getErrors() {
		return errors;
	}
	
	public void setErrors(List<SynchronizationError> errors) {
		this.errors = errors;
	}
	
}