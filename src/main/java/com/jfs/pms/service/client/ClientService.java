package com.jfs.pms.service.client;

import com.jfs.pms.domain.Clients;
import com.jfs.pms.exception.AlreadyExistsException;
import com.jfs.pms.exception.NotFoundException;
import com.jfs.pms.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class ClientService implements IClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Clients addClient(String loggedInUserPublicId, Clients client) {
        try {
            if(clientAlreadyExists(client.getClientDomain())) {
                throw new AlreadyExistsException("Client already exists", BAD_REQUEST);
            }
            client.setCreatedBy(loggedInUserPublicId);
            return clientRepository.save(client);
        } catch (AlreadyExistsException exception) {
            log.error("Error occurred while adding client, Client already exists: {}", exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            log.error("Error occurred while adding client: {}", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public Clients updateClient(String loggedInUserPublicId, Clients client) {
        try {
            return clientRepository.save(client);
        } catch (Exception exception) {
            log.error("Error occurred while updating client: {}", exception.getMessage());
            throw exception;
        }
    }

    @Override
    public boolean deleteClient(String loggedInUserPublicId, String publicId) {
        try {
            var uuid = UUID.fromString(publicId);
            var client = clientRepository.findByPublicId(uuid)
                    .orElseThrow(() -> new NotFoundException("Client not found", BAD_REQUEST));
            clientRepository.delete(client);
            return true;
        } catch (NotFoundException exception) {
            log.error("Error occurred while deleting client, Client not found: {}", exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            log.error("Error occurred while deleting client: {}", exception.getMessage());
            throw exception;
        }
    }

    private boolean clientAlreadyExists(String clientDomain) {
        return clientRepository.existsByClientDomain(clientDomain);
    }
}
