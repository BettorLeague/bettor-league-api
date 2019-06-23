package com.bettorleague.server.rest;

import com.bettorleague.server.dto.contest.ContestRequest;
import com.bettorleague.server.dto.contest.MessageRequest;
import com.bettorleague.server.event.ParticipantRepository;
import com.bettorleague.server.exception.ResourceNotFoundException;
import com.bettorleague.server.model.bettor.*;
import com.bettorleague.server.repository.bettor.ContestRepository;
import com.bettorleague.server.repository.bettor.MessageRepository;
import com.bettorleague.server.repository.bettor.PlayerRepository;
import com.bettorleague.server.security.model.CurrentUser;
import com.bettorleague.server.security.model.UserPrincipal;
import com.bettorleague.server.service.ContestService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1")
@Transactional
public class ContestController {

    @Autowired
    private ContestService contestService;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ContestRepository contestRepository;


    @RequestMapping(path = "/contest", method = RequestMethod.GET)
    public ResponseEntity<Page<Contest>> getAllContest(@Valid @RequestParam(value = "type", required = false) ContestType type,
                                                       @Valid @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                       @Valid @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                                                       @Valid @RequestParam(value = "sort", required = false, defaultValue = "totalPlayer") String sort,
                                                       @Valid @RequestParam(value = "sortDirection", required = false, defaultValue = "DESC") Sort.Direction sortDirection) {
        return new ResponseEntity<>(contestService.getContestPage(type, PageRequest.of(page, size, Sort.by(sortDirection.equals(Sort.Direction.ASC) ? Sort.Order.asc(sort) : Sort.Order.desc(sort), Sort.Order.asc("caption")))), HttpStatus.OK);
    }

    @RequestMapping(path = "/contest", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Contest> addContest(@Valid ContestRequest contestRequest,
                                              @ApiIgnore
                                              @ApiParam(hidden = true)
                                              @CurrentUser UserPrincipal userPrincipal) {
        return new ResponseEntity<>(contestService.addContest(contestRequest, userPrincipal.getId()), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/contest/{contestId}", method = RequestMethod.GET)
    public ResponseEntity<Contest> getContestById(@PathVariable("contestId") Long contestId) {
        return new ResponseEntity<>(contestService.getContestById(contestId), HttpStatus.OK);
    }

    @RequestMapping(path = "/contest/{contestId}", method = RequestMethod.DELETE)
    public ResponseEntity<Contest> deleteContest(@PathVariable("contestId") Long contestId) {
        return new ResponseEntity<>(contestService.deleteContest(contestId), HttpStatus.OK);
    }

    @RequestMapping(path = "/contest/{contestId}/players", method = RequestMethod.GET)
    public ResponseEntity<Set<Player>> getPlayersByContestId(@PathVariable("contestId") Long contestId) {
        return new ResponseEntity<>(contestService.getPlayer(contestId), HttpStatus.OK);
    }


    @RequestMapping(path = "/contest/{contestId}/standings", method = RequestMethod.GET)
    public ResponseEntity<List<StandingPlayer>> getStandingsContest(@PathVariable("contestId") Long contestId) {
        return new ResponseEntity<>(contestService.getStanding(contestId), HttpStatus.OK);
    }

    @RequestMapping(path = "/contest/{contestId}/messages", method = RequestMethod.GET)
    public ResponseEntity<Page<Message>> getMessagesByContestId(@PathVariable("contestId") Long contestId,
                                                                @Valid @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                @Valid @RequestParam(value = "size", required = false, defaultValue = "100") int size,
                                                                @Valid @RequestParam(value = "sort", required = false, defaultValue = "date") String sort,
                                                                @Valid @RequestParam(value = "sortDirection", required = false, defaultValue = "ASC") Sort.Direction sortDirection) {
        return new ResponseEntity<>(contestService.getMessagePage(contestId, PageRequest.of(page, size, Sort.by(sortDirection.equals(Sort.Direction.ASC) ? Sort.Order.asc(sort) : Sort.Order.desc(sort)))), HttpStatus.OK);
    }

    @MessageMapping("/chat/{contestId}/messages")
    @SendTo("/topic/chat/{contestId}/messages")
    public void sendMessage(@Payload MessageRequest messageRequest,
                            @DestinationVariable("contestId") String contestId) {
        Player player = this.playerRepository.findById(messageRequest.getPlayerId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Player", "id", messageRequest.getPlayerId()));


        Contest contest = this.contestRepository.findById(player.getContest().getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Contest", "id", player.getContest().getId()));


        Message messagetoSave = new Message();
        messagetoSave.setType(MessageType.CHAT);
        messagetoSave.setContent(messageRequest.getContent());
        messagetoSave.setPlayer(player);
        messagetoSave.setContest(contest);
        messagetoSave.setDate(new Date());

        messagetoSave = messageRepository.save(messagetoSave);

        messagingTemplate.convertAndSend("/topic/chat/" + player.getContest().getId() + "/messages", messagetoSave);
    }

    @MessageMapping("/chat/{contestId}/participants")
    @SendTo("/topic/chat/{contestId}/participants")
    public Set<Player> retrieveParticipants(@DestinationVariable String contestId) {
        return participantRepository.getParticipant(Long.valueOf(contestId));
    }

    @MessageMapping("/chat/login")
    @SendTo("/topic/chat/login")
    public void login(@Payload MessageRequest messageRequest,
                      SimpMessageHeaderAccessor headerAccessor) {

        Player player = this.playerRepository.findById(messageRequest.getPlayerId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Player", "id", messageRequest.getPlayerId()));


        Set<Player> players = this.participantRepository.getParticipant(player.getContest().getId());

        if(players != null){

            Optional<Player> playerOptional = players.stream()
                    .filter(item -> item.getId().equals(player.getId()))
                    .findAny();

            if (! playerOptional.isPresent()){

                this.participantRepository.add(player.getContest().getId(), player);
                headerAccessor.getSessionAttributes().put("playerId", player.getId());
                headerAccessor.getSessionAttributes().put("contestId", player.getContest().getId());
            }

        }else{

            this.participantRepository.add(player.getContest().getId(), player);
            headerAccessor.getSessionAttributes().put("playerId", player.getId());
            headerAccessor.getSessionAttributes().put("contestId", player.getContest().getId());

        }


        messagingTemplate.convertAndSend("/topic/chat/" + player.getContest().getId() + "/participants", participantRepository.getParticipant(player.getContest().getId()));
    }


}
