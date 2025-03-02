package com.example.travelagency.service;

import com.example.travelagency.entity.Client;
import com.example.travelagency.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;


    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public Client updateClient(Long id, Client clientDetails) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        client.setFirstName(clientDetails.getFirstName());
        client.setLastName(clientDetails.getLastName());
        client.setMiddleName(clientDetails.getMiddleName());
        client.setPhone(clientDetails.getPhone());
        client.setEmail(clientDetails.getEmail());
        client.setBirthDate(clientDetails.getBirthDate());
        return clientRepository.save(client);
    }
    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email); // Метод для поиска по email
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}