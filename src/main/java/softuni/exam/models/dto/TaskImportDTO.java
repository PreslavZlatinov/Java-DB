package softuni.exam.models.dto;

import softuni.exam.models.entity.Mechanic;
import softuni.exam.models.entity.Part;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
public class TaskImportDTO {
    @XmlElement
    private String date;

    @XmlElement
    @Positive
    private BigDecimal price;

    @XmlElement
    private TaskImportCarDTO car;

    @XmlElement
    private TaskImportMechanicDTO mechanic;

    @XmlElement
    private TaskImportPartDTO part;

    public TaskImportDTO() {
    }

    public String getDate() {
        return date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public TaskImportMechanicDTO getMechanic() {
        return mechanic;
    }

    public TaskImportPartDTO getPart() {
        return part;
    }

    public TaskImportCarDTO getCar() {
        return car;
    }
}
