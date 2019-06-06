package org.openhds.web.ui;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class LifeCycleListener implements PhaseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void afterPhase(PhaseEvent event) {
		System.out.println("[after] "+event.getPhaseId()+" - "+event.getSource());
	}

	public void beforePhase(PhaseEvent event) {
		System.out.println("[before] "+event.getPhaseId()+" - "+event.getSource());
	}

	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}
