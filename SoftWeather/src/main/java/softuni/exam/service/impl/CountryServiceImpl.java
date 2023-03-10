package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CountryImportDTO;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;

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
public class CountryServiceImpl implements CountryService {

    private  final CountryRepository countryRepository;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper modelMapper;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;

        this.gson = new GsonBuilder().create();

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

        this.modelMapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "countries.json");

        return Files.readString(path);
    }

    @Override
    public String importCountries() throws IOException {
        String json = this.readCountriesFromFile();

        CountryImportDTO[] countryImportDTOS = this.gson.fromJson(json, CountryImportDTO[].class);

        List<String> result = new ArrayList<>();

        for (CountryImportDTO countryImportDTO : countryImportDTOS) {
            Set<ConstraintViolation<CountryImportDTO>> validationErrors = this.validator.validate(countryImportDTO);

            if (validationErrors.isEmpty()) {
                Optional<Country> optionalCountry = this.countryRepository.findByCountryName(countryImportDTO.getCountryName());

                if (optionalCountry.isEmpty()) {
                    Country country = this.modelMapper.map(countryImportDTO, Country.class);

                    this.countryRepository.save(country);

                    String msg = String.format("Successfully imported country %s - %s", country.getCountryName(), country.getCurrency());

                    result.add(msg);
                } else {
                    result.add("Invalid country");
                }
            } else {
                result.add("Invalid country");
            }
        }
        return String.join("\n", result);
    }
}
