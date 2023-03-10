package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CityImportDTO;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CityService;

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
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    private final CountryRepository countryRepository;
    private final Gson gson;
    private final Validator validator;
    private final ModelMapper modelMapper;

    public CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;

        this.gson = new GsonBuilder().create();

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

        this.modelMapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "cities.json");

        return Files.readString(path);
    }

    @Override
    public String importCities() throws IOException {
        String json = this.readCitiesFileContent();

        CityImportDTO[] cityImportDTOS = this.gson.fromJson(json, CityImportDTO[].class);

        List<String> result = new ArrayList<>();

        for (CityImportDTO cityImportDTO : cityImportDTOS) {
            Set<ConstraintViolation<CityImportDTO>> validationErrors = this.validator.validate(cityImportDTO);

            if (validationErrors.isEmpty()) {
                Optional<City> optionalCity = this.cityRepository.findByCityName(cityImportDTO.getCityName());

                if (optionalCity.isEmpty()) {
                    City city = this.modelMapper.map(cityImportDTO, City.class);

                    Optional<Country> country = this.countryRepository.findById(cityImportDTO.getCountry());

                    city.setCountry(country.get());

                    this.cityRepository.save(city);

                    String msg = String.format("Successfully imported city %s - %d", city.getCityName(), city.getPopulation());

                    result.add(msg);
                } else {
                    result.add("Invalid city");
                }
            } else {
                result.add("Invalid city");
            }
        }
        return String.join("\n", result);
    }
}
