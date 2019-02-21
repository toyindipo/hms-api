package com.oasis.hms.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by Toyin on 2/3/19.
 */
public class PrescriptionDto {
    @NotNull
    private Long drugId;
    private int count;
    private String dosage;

    public Long getDrugId() {
        return drugId;
    }

    public void setDrugId(Long drugId) {
        this.drugId = drugId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrescriptionDto that = (PrescriptionDto) o;

        return drugId.equals(that.drugId);
    }

    @Override
    public int hashCode() {
        return drugId.hashCode();
    }
}
