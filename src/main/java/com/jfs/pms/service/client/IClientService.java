package com.jfs.pms.service.client;

import com.jfs.pms.domain.Clients;

public interface IClientService {

    Clients addClient(String loggedInUserPublicId, Clients client);
    Clients updateClient(String loggedInUserPublicId, Clients client);
    boolean deleteClient(String loggedInUserPublicId, String publicId);
}
