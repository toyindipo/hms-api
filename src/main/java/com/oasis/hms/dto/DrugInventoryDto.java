package com.oasis.hms.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by Toyin on 2/2/19.
 */
public class DrugInventoryDto {
    @NotNull
    private Long drugId;
    private int count;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugInventoryDto drugInventoryDto = (DrugInventoryDto) o;

        return drugId.equals(drugInventoryDto.drugId);
    }

    @Override
    public int hashCode() {
        return drugId.hashCode();
    }
}
