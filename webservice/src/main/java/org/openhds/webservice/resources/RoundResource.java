package org.openhds.webservice.resources;


import org.openhds.controller.service.RoundService;
import org.openhds.domain.model.wrappers.Rounds;
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

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Rounds getLastRound() {
        Rounds rounds = new Rounds();
        rounds.setRounds(roundService.getLastRound());
        return rounds;
    }

}
