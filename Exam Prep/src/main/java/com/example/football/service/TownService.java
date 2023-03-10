package com.example.football.service;


import org.springframework.stereotype.Service;

import java.io.IOException;

//ToDo - Implement all methods
public interface TownService {

    boolean areImported();

    String readTownsFileContent() throws IOException;
	
	String importTowns() throws IOException;

}
