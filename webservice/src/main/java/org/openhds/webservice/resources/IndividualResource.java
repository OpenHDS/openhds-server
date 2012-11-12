package org.openhds.webservice.resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.controller.service.IndividualService;
import org.openhds.domain.model.Individual;
import org.openhds.domain.model.Location;
import org.openhds.domain.model.Membership;
import org.openhds.domain.model.Residency;
import org.openhds.domain.model.SocialGroup;
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

    private IndividualService individualService;

    @Autowired
    public IndividualResource(IndividualService individualService) {
        this.individualService = individualService;
    }

    @XmlRootElement
    private static class Individuals {
        private List<Individual> individuals;

        @XmlElement(name = "individual")
        public List<Individual> getIndividuals() {
            return individuals;
        }

        public void setIndividuals(List<Individual> individuals) {
            this.individuals = individuals;
        }
    }

    @RequestMapping(value = "/{extId}", method = RequestMethod.GET)
    public ResponseEntity<? extends Serializable> getIndividualById(@PathVariable String extId) {
        Individual individual = individualService.findIndivById(extId);
        if (individual == null) {
            return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Individual>(copy(individual), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Individuals getAllIndividuals() {
        List<Individual> allIndividual = individualService.getAllIndividuals();
        List<Individual> copies = new ArrayList<Individual>(allIndividual.size());
        for (Individual individual : allIndividual) {
            Individual copy = copy(individual);
            copies.add(copy);
        }

        Individuals individuals = new Individuals();
        individuals.setIndividuals(copies);
        return individuals;
    }

    protected Individual copy(Individual individual) {
        Individual copy = new Individual();
        copy.setDob(individual.getDob());
        copy.setDobAspect(individual.getDobAspect());
        copy.setExtId(individual.getExtId());

        copy.setFather(copyExtId(individual.getFather()));
        copy.setFirstName(individual.getFirstName());
        copy.setGender(individual.getGender());
        copy.setLastName(individual.getLastName());
        String middleName = individual.getMiddleName() == null ? "" : individual.getMiddleName();
        copy.setMiddleName(middleName);
        copy.setMother(copyExtId(individual.getMother()));

        for (Membership membership : individual.getAllMemberships()) {
            Membership memCopy = new Membership();

            SocialGroup sgCopy = new SocialGroup();
            sgCopy.setExtId(membership.getSocialGroup().getExtId());

            memCopy.setSocialGroup(sgCopy);
            memCopy.setbIsToA(membership.getbIsToA());
            copy.getAllMemberships().add(memCopy);
        }

        if (individual.getCurrentResidency() != null) {
            Residency resCopy = new Residency();
            Location locCopy = new Location();
            locCopy.setLocationLevel(null);
            locCopy.setExtId(individual.getCurrentResidency().getLocation().getExtId());
            resCopy.setLocation(locCopy);
            copy.getAllResidencies().add(resCopy);
        }
        return copy;
    }

    protected Individual copyExtId(Individual individual) {
        if (individual == null) {
            return null;
        }

        Individual copy = new Individual();
        copy.setExtId(individual.getExtId());
        return copy;
    }
}
