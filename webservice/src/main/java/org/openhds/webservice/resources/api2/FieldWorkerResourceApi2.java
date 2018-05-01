package org.openhds.webservice.resources.api2;

import java.util.ArrayList;
import java.util.List;

import org.openhds.controller.service.FieldWorkerService;
import org.openhds.domain.model.FieldWorker;
import org.openhds.domain.model.wrappers.FieldWorkers;
import org.openhds.domain.util.ShallowCopier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/fieldworkers2")
public class FieldWorkerResourceApi2 {

	private FieldWorkerService fieldWorkerService;

	@Autowired
	public FieldWorkerResourceApi2(FieldWorkerService fieldWorkerService) {
		this.fieldWorkerService = fieldWorkerService;
	}

	@RequestMapping(method = RequestMethod.GET, produces = "application/xml")
	@ResponseBody
	public FieldWorkers getAllFieldWorkers() {
		List<FieldWorker> allFieldWorkers = fieldWorkerService.getAllFieldWorkers();
		List<FieldWorker> copies = new ArrayList<FieldWorker>();
		for (FieldWorker fw : allFieldWorkers) {
			copies.add(ShallowCopier.shallowCopyFieldWorker(fw));
		}

		FieldWorkers fws = new FieldWorkers();
		fws.setFieldWorkers(copies);

		return fws;
	}
}
