package com.jfs.pms.controller;

import com.jfs.pms.domain.Clients;
import com.jfs.pms.domain.User;
import com.jfs.pms.exception.UnAuthorizedException;
import com.jfs.pms.service.client.IClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.jfs.pms.constants.Constants.INVALID_ATTEMPT;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);
    private final IClientService clientService;

    public ClientController(IClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addClient(@RequestBody Clients client) {
        log.info("Request to add client: {}", client);
        var loggedInUser = getLoggedInUser();
        return new ResponseEntity<>(clientService.addClient(loggedInUser.getPublicId().toString(), client), CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateClient(@RequestBody Clients client) {
        log.info("Request to update client: {}", client);
        var loggedInUser = getLoggedInUser();
        return new ResponseEntity<>(clientService.updateClient(loggedInUser.getPublicId().toString(), client), OK);
    }

    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("Invalid user attempted to add new project, auth used: {}", authentication);
            throw new UnAuthorizedException(INVALID_ATTEMPT, UNAUTHORIZED);
        }
        return (User) authentication.getPrincipal();
    }
}
