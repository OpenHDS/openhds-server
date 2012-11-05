package org.openhds.webservice.resources;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhds.controller.service.RoundService;
import org.openhds.domain.model.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rounds")
public class RoundResource {

    private RoundService roundService;

    @Autowired
    public RoundResource(RoundService roundService) {
        this.roundService = roundService;
    }

    @XmlRootElement
    private static class Rounds {
        private List<Round> rounds;

        @XmlElement(name = "round")
        public List<Round> getRounds() {
            return rounds;
        }

        public void setRounds(List<Round> rounds) {
            this.rounds = rounds;
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Rounds getAllRounds() {
        Rounds rounds = new Rounds();
        rounds.setRounds(roundService.getAllRounds());
        return rounds;
    }

}
