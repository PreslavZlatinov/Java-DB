package com.example.football.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

//ToDo - Implement all methods
public interface TeamService {
    boolean areImported();

    String readTeamsFileContent() throws IOException;

    String importTeams() throws IOException;

}
