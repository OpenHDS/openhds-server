package org.openhds.webservice.resources.json;
import java.util.ArrayList;
import java.util.List;

import org.openhds.service.FieldWorkerService;
import org.openhds.domain.FieldWorker;
import org.openhds.domain.wrappers.FieldWorkers;
import org.openhds.util.ShallowCopier;
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

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
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