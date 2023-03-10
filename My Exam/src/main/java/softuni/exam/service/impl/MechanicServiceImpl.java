package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.MechanicImportDTO;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.service.MechanicService;

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

@Service
public class MechanicServiceImpl implements MechanicService {

    private final MechanicRepository mechanicRepository;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper modelMapper;

    @Autowired
    public MechanicServiceImpl(MechanicRepository mechanicRepository) {
        this.mechanicRepository = mechanicRepository;

        this.gson = new GsonBuilder().create();

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

        this.modelMapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.mechanicRepository.count() > 0;
    }

    @Override
    public String readMechanicsFromFile() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "mechanics.json");

        return Files.readString(path);
    }

    @Override
    public String importMechanics() throws IOException {
        String json = this.readMechanicsFromFile();

        MechanicImportDTO[] mechanicImportDTOs = this.gson.fromJson(json, MechanicImportDTO[].class);

        List<String> result = new ArrayList<>();

        for (MechanicImportDTO mechanicImportDTO : mechanicImportDTOs) {
            Set<ConstraintViolation<MechanicImportDTO>> validationErrors = this.validator.validate(mechanicImportDTO);

            if (validationErrors.isEmpty()) {
                Optional<Mechanic> optionalMechanic = this.mechanicRepository.findByEmail(mechanicImportDTO.getEmail());

                if (optionalMechanic.isEmpty()) {
                    Mechanic mechanic = this.modelMapper.map(mechanicImportDTO, Mechanic.class);

                    this.mechanicRepository.save(mechanic);

                    String msg = String.format("Successfully imported mechanic %s %s", mechanic.getFirstName(), mechanic.getLastName());

                    result.add(msg);
                } else {
                    result.add("Invalid mechanic");
                }
            } else {
                result.add("Invalid mechanic");
            }
        }
        return String.join("\n", result);
    }
}
