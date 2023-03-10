package softuni.exam.models.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class PartImportDTO {
    @Size(min = 2, max = 19)
    private String partName;

    @DecimalMin(value = "10")
    @DecimalMax(value = "2000")
    private double price;

    @Positive
    private int quantity;

    public PartImportDTO() {
    }

    public String getPartName() {
        return partName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
