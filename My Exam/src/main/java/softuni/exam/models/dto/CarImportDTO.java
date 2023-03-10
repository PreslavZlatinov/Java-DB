package softuni.exam.models.dto;

import softuni.exam.models.entity.CarType;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CarImportDTO {
    @XmlElement
    @Size(min = 2, max = 30)
    private String carMake;
    @XmlElement
    @Size(min = 2, max = 30)
    private String carModel;
    @XmlElement
    @Positive
    private int year;
    @XmlElement
    @Size(min = 2, max = 30)
    private String plateNumber;
    @XmlElement
    @Positive
    private int kilometers;
    @XmlElement
    @DecimalMin(value = "1.00")
    private double engine;
    @XmlElement
    private String carType;

    public CarImportDTO() {
    }

    public String getCarMake() {
        return carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public int getYear() {
        return year;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public int getKilometers() {
        return kilometers;
    }

    public double getEngine() {
        return engine;
    }

    public String getCarType() {
        return carType;
    }
}
