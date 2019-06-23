package com.bettorleague.server.rest;

import com.bettorleague.server.dto.contest.MessageRequest;
import com.bettorleague.server.model.bettor.Message;
import com.bettorleague.server.model.bettor.Player;
import com.bettorleague.server.model.bettor.Pronostic;
import com.bettorleague.server.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @RequestMapping(path = "/player", method = RequestMethod.GET)
    public ResponseEntity<List<Player>> getAll() {
        return new ResponseEntity<>(playerService.getAll(), HttpStatus.OK);
    }

    @RequestMapping(path = "/player/{playerId}/pronostics", method = RequestMethod.GET)
    public ResponseEntity<List<Pronostic>> getPronostics(@PathVariable("playerId") Long playerId) {
        return new ResponseEntity<>(playerService.getPronostics(playerId), HttpStatus.OK);
    }

    @RequestMapping(path = "/player/{playerId}/messages", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> getMessages(@PathVariable("playerId") Long playerId) {
        return new ResponseEntity<>(playerService.getMessages(playerId), HttpStatus.OK);
    }

    @RequestMapping(path = "/player/{playerId}/messages", method = RequestMethod.POST)
    public ResponseEntity<Message> postMessage(@PathVariable("playerId") Long playerId,
                                               @RequestBody MessageRequest messageRequest) {
        return new ResponseEntity<>(playerService.postMessage(playerId,messageRequest), HttpStatus.OK);
    }
}
