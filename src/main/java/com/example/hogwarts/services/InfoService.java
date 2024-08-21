package com.example.hogwarts.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
//@Profile("!test")
public class InfoService {

    @Value("${server.port}")
    private String numberPort;

    public String getPort() {
        return numberPort;
    }

}
