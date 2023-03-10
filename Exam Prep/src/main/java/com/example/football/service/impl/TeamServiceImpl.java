package com.example.football.service.impl;

import com.example.football.models.dto.ImportTeamDTO;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.TeamService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

//ToDo - Implement all methods
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private final TownRepository townRepository;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper modelMapper;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, TownRepository townRepository) {
        this.teamRepository = teamRepository;

        this.townRepository = townRepository;

        this.gson = new GsonBuilder().create();

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

        this.modelMapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "teams.json");

        return Files.readString(path);
    }

    @Override
    public String importTeams() throws IOException {
        String json = this.readTeamsFileContent();

        ImportTeamDTO[] importTeamDTOs = this.gson.fromJson(json, ImportTeamDTO[].class);

        List<String> result = new ArrayList<>();

        for (ImportTeamDTO importTeamDTO : importTeamDTOs) {
            Set<ConstraintViolation<ImportTeamDTO>> validationErrors = this.validator.validate(importTeamDTO);

            if (validationErrors.isEmpty()) {
                Optional<Team> optTeam = this.teamRepository.findByName(importTeamDTO.getName());

                if (optTeam.isEmpty()) {
                    Team team = this.modelMapper.map(importTeamDTO, Team.class);

                    Optional<Town> town = this.townRepository.findByName(importTeamDTO.getTownName());

                    team.setTown(town.get());

                    this.teamRepository.save(team);

                    String msg = String.format("Successfully imported Team %s - %d", team.getName(), team.getFanBase());

                    result.add(msg);
                } else {
                    result.add("Invalid Team");
                }
            } else {
                result.add("Invalid Team");
            }
        }
        return String.join("\n", result);
    }
}
