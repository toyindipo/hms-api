package com.oasis.hms.dto;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by Toyin on 2/6/19.
 */
public class DiagnosisResultDto {
    @NotNull
    private Set<DiagnosisDto> diagnosisSet;
    private String labReport;

    public Set<DiagnosisDto> getDiagnosisSet() {
        return diagnosisSet;
    }

    public void setDiagnosisSet(Set<DiagnosisDto> diagnosisSet) {
        this.diagnosisSet = diagnosisSet;
    }

    public String getLabReport() {
        return labReport;
    }

    public void setLabReport(String labReport) {
        this.labReport = labReport;
    }
}
