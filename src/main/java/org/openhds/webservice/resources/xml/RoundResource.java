package org.openhds.webservice.resources.xml;


import java.io.Serializable;

import org.openhds.exception.ConstraintViolations;
import org.openhds.service.RoundService;
import org.openhds.domain.Round;
import org.openhds.domain.wrappers.Rounds;
import org.openhds.util.ShallowCopier;
import org.openhds.webservice.WebServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping(method = RequestMethod.GET, produces = "application/xml")
    @ResponseBody
    public Rounds getLastRound() {
        Rounds rounds = new Rounds();
        rounds.setRounds(roundService.getLastRound());
        return rounds;
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "application/xml")
    public ResponseEntity<? extends Serializable> insert(@RequestBody Round round) {
      
        try {
        	roundService.createRound(round);
        } catch (ConstraintViolations e) {
            return new ResponseEntity<WebServiceCallException>(new WebServiceCallException(e), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Round>(ShallowCopier.copyRound(round), HttpStatus.CREATED);
    }
}
