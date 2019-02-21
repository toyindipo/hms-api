package com.oasis.hms.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by Toyin on 2/3/19.
 */
public class DiagnosisDto {
    @NotNull
    private Long diagnosisId;
    @NotNull
    private Boolean status;

    public Long getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(Long diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiagnosisDto that = (DiagnosisDto) o;

        return diagnosisId.equals(that.diagnosisId);
    }

    @Override
    public int hashCode() {
        return diagnosisId.hashCode();
    }
}
