package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PartImportDTO;
import softuni.exam.models.entity.Part;
import softuni.exam.repository.PartRepository;
import softuni.exam.service.PartService;

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
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper modelMapper;

    @Autowired
    public PartServiceImpl(PartRepository partRepository) {
        this.partRepository = partRepository;

        this.gson = new GsonBuilder().create();

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

        this.modelMapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.partRepository.count() > 0;
    }

    @Override
    public String readPartsFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "parts.json");

        return Files.readString(path);
    }

    @Override
    public String importParts() throws IOException {
        String json = this.readPartsFileContent();

        PartImportDTO[] partImportDTOs = this.gson.fromJson(json, PartImportDTO[].class);

        List<String> result = new ArrayList<>();

        for (PartImportDTO partImportDTO : partImportDTOs) {
            Set<ConstraintViolation<PartImportDTO>> validationErrors = this.validator.validate(partImportDTO);

            if (validationErrors.isEmpty()) {
                Optional<Part> optionalPart = this.partRepository.findByPartName(partImportDTO.getPartName());

                if (optionalPart.isEmpty()) {
                    Part part = this.modelMapper.map(partImportDTO, Part.class);

                    this.partRepository.save(part);

                    String msg = String.format("Successfully imported part %s - %.2f", part.getPartName(), part.getPrice());

                    result.add(msg);
                } else {
                    result.add("Invalid part");
                }
            } else {
                result.add("Invalid part");
            }
        }
        return String.join("\n", result);
    }
}
