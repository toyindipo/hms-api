package com.oasis.hms.model.enums;

public enum Sex {
    M, F;

    public static Sex valueOf(char s) {
        switch (s) {
            case  'M': return M;
            case 'F': return F;
            default:
                throw new IllegalArgumentException("Sex should be M or F");
        }
    }
}