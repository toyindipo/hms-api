package com.oasis.hms.dao.predicate;

import com.oasis.hms.model.enums.Role;
import com.oasis.hms.model.enums.VisitState;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;

/**
 * Created by Toyin on 2/17/19.
 */
public class EnumBooleanExpression {
    public static  <T> BooleanExpression getExpression(String key, Object value, PathBuilder<T> entityPath) {
        switch (key) {
            case "role":
                return entityPath.getEnum(key, Role.class).eq((Role) value);
            case "state":
                return entityPath.getEnum(key, VisitState.class).eq((VisitState) value);
        }
        return null;
    }
}
