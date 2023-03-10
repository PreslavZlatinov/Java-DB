package softuni.exam.models.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class CityImportDTO {
    @Size(min = 2, max = 60)
    private String cityName;

    @Size(min = 2)
    private String description;

    @Min(value = 500)
    private int population;

    private long country;

    public CityImportDTO() {
    }

    public String getCityName() {
        return cityName;
    }

    public String getDescription() {
        return description;
    }

    public int getPopulation() {
        return population;
    }

    public long getCountry() {
        return country;
    }
}
