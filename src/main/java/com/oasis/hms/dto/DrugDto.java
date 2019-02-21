package com.oasis.hms.dto;

import com.oasis.hms.model.Drug;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Created by Toyin on 2/4/19.
 */
public class DrugDto {
    @NotNull
    private String name;
    @Size(max = 255)
    private String description;
    @NotNull
    private BigDecimal amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Drug toDrug() {
        Drug drug = new Drug();
        drug.setName(name);
        drug.setAmount(amount);
        drug.setDescription(description);
        return drug;
    }
}
