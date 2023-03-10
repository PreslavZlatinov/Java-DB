package softuni.exam.service.impl;

import com.sun.xml.bind.api.JAXBRIContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ForecastImportDTO;
import softuni.exam.models.dto.ForecastRootImportDTO;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.DayOfWeek;
import softuni.exam.models.entity.Forecast;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.ForecastService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ForecastServiceImpl implements ForecastService {

    private final Path path = Path.of("src", "main", "resources", "files", "xml", "forecasts.xml");
    private final ForecastRepository forecastRepository;

    private final CityRepository cityRepository;
    private final Unmarshaller unmarshaller;
    private final Validator validator;
    private final ModelMapper modelMapper;

    @Autowired
    public ForecastServiceImpl(ForecastRepository forecastRepository, CityRepository cityRepository) throws JAXBException {
        this.forecastRepository = forecastRepository;
        this.cityRepository = cityRepository;

        JAXBContext context = JAXBRIContext.newInstance(ForecastRootImportDTO.class);
        this.unmarshaller = context.createUnmarshaller();

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

        this.modelMapper = new ModelMapper();

//        this.modelMapper.addConverter(ctx -> LocalDate.parse(ctx.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
//                String.class, LocalDate.class);

        this.modelMapper.addConverter(ctx -> LocalTime.parse(ctx.getSource(), DateTimeFormatter.ofPattern("HH:mm:ss")), String.class, LocalTime.class);
    }

    @Override
    public boolean areImported() {
        return this.forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files.readString(path);
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        ForecastRootImportDTO forecastDTOs = (ForecastRootImportDTO) this.unmarshaller.unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return forecastDTOs.getForecasts().stream().map(this::importForecast).collect(Collectors.joining("\n"));
    }

    private String importForecast(ForecastImportDTO forecastImportDTO) {
        Set<ConstraintViolation<ForecastImportDTO>> errors = this.validator.validate(forecastImportDTO);

        if(!errors.isEmpty()){
            return "Invalid forecast";
        }


        Optional<City> city = this.cityRepository.findById(forecastImportDTO.getCity());

        if(city.isPresent()) {

            Forecast forecast = this.modelMapper.map(forecastImportDTO, Forecast.class);

//            String[] splitLocalDateSunrise = forecastImportDTO.getSunrise().split(":");
//            LocalTime sunrise = LocalTime.of(Integer.parseInt(splitLocalDateSunrise[0]), Integer.parseInt(splitLocalDateSunrise[1]), Integer.parseInt(splitLocalDateSunrise[2]));
//
//            String[] splitLocalDateSunset = forecastImportDTO.getSunset().split(":");
//            LocalTime sunset = LocalTime.of(Integer.parseInt(splitLocalDateSunset[0]), Integer.parseInt(splitLocalDateSunset[1]), Integer.parseInt(splitLocalDateSunset[2]));
//
//            forecast.setSunrise(sunrise);
//
//            forecast.setSunset(sunset);

            forecast.setCity(city.get());

            this.forecastRepository.save(forecast);

            return "Successfully imported forecast " + forecast.getDayOfWeek() + " - " + forecast.getMaxTemperature();
        } else {
            return "Invalid forecast";
        }
    }

    @Override
    public String exportForecasts() {

        List<Forecast> forecasts = this.forecastRepository.findAllByDayOfWeekAndCityPopulationLessThanOrderByMaxTemperatureDescIdAsc(DayOfWeek.SUNDAY, 150000);

        return forecasts.stream().map(forecast -> String.format("City: %s:%n-min temperature: %.2f%n--max temperature: %.2f%n---sunrise: %s%n----sunset: %s",
                forecast.getCity().getCityName(),forecast.getMinTemperature(),forecast.getMaxTemperature(),forecast.getSunrise(),forecast.getSunset())).collect(Collectors.joining(System.lineSeparator()));
    }
}
