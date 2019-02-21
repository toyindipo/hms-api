package com.oasis.hms.dao.predicate;

/**
 * Created by Toyin on 2/1/19.
 */
public enum Operation {
    LIKE("string"), EQUALS("number"), LESS("number"), GREATER("number"), STRING_EQUALS("string"), BETWEEN("date"),
    ENUM("enum");

    private final String type;

    Operation(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
