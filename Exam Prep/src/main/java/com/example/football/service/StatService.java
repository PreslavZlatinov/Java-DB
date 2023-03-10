package com.example.football.service;

import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

//ToDo - Implement all methods
public interface StatService {
    boolean areImported();

    String readStatsFileContent() throws IOException;

    String importStats() throws FileNotFoundException, JAXBException;

}
