package org.openhds.webservice.resources.xml;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.openhds.service.IndividualService;
import org.openhds.domain.Individual;
import org.openhds.domain.wrappers.Individuals;
import org.openhds.util.ShallowCopier;
import org.openhds.task.support.FileResolver;
import org.openhds.webservice.CacheResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/individuals")
public class IndividualResource {
    private static final Logger logger = LoggerFactory.getLogger(IndividualResource.class);
    private IndividualService individualService;
    private FileResolver fileResolver;

    @Autowired
    public IndividualResource(IndividualService individualService, FileResolver fileResolver) {
        this.individualService = individualService;
        this.fileResolver = fileResolver;
    }

    @RequestMapping(value = "/{extId}", method = RequestMethod.GET)
    public ResponseEntity<? extends Serializable> getIndividualById(@PathVariable String extId) {
        Individual individual = individualService.findIndivById(extId);
        if (individual == null) {
            return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Individual>(ShallowCopier.shallowCopyIndividual(individual), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Individuals getAllIndividuals() {
        List<Individual> allIndividual = individualService.getAllIndividuals();
        List<Individual> copies = new ArrayList<Individual>(allIndividual.size());
        for (Individual individual : allIndividual) {
            Individual copy = ShallowCopier.shallowCopyIndividual(individual);
            copies.add(copy);
        }

        Individuals individuals = new Individuals();
        individuals.setIndividuals(copies);

        return individuals;
    }

    @RequestMapping(value = "/cached", method = RequestMethod.GET)
    public void getCachedIndividuals(HttpServletResponse response) {
        try {
            CacheResponseWriter.writeResponse(fileResolver.resolveIndividualXmlFile(), response);
        } catch (IOException e) {
            logger.error("Problem writing individual xml file: " + e.getMessage());
        }
    }
    
    @RequestMapping(value = "/zipped", method = RequestMethod.GET)
    public void getZippedIndividuals(HttpServletResponse response) {
            try {
                CacheResponseWriter.writeResponse(fileResolver.resolveIndividualZipFile(), response);
            } catch (IOException e) {
                logger.error("Problem writing individual zip file: " +  e.getMessage());
            }
   }
}
