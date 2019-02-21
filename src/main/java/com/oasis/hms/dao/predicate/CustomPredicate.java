package com.oasis.hms.dao.predicate;

import com.querydsl.core.types.dsl.*;

import java.time.LocalDateTime;

/**
 * Created by Toyin on 2/1/19.
 */
public class CustomPredicate<T> {
    private SearchCriteria criteria;
    private Class<T> clazz;
    private String path;

    public CustomPredicate(SearchCriteria criteria, String path, Class<T> clazz) {
        this.criteria = criteria;
        this.clazz = clazz;
        this.path = path;
    }

    public BooleanExpression getPredicate() {
        if (criteria.getValue() == null) return null;

        PathBuilder<T> entityPath = new PathBuilder<>(clazz, path);

        if (criteria.getOperation().getType().equalsIgnoreCase("number")) {
            NumberPath<Integer> path = entityPath.getNumber(criteria.getKey(), Integer.class);
            int value = Integer.parseInt(criteria.getValue().toString());
            switch (criteria.getOperation()) {
                case EQUALS:
                    return path.eq(value);
                case GREATER:
                    return path.goe(value);
                case LESS:
                    return path.loe(value);
            }
        }
        else if (criteria.getOperation().getType().equalsIgnoreCase("string")){
            StringPath path = entityPath.getString(criteria.getKey());
            switch (criteria.getOperation()) {
                case STRING_EQUALS:
                    return path.equalsIgnoreCase(criteria.getValue().toString());
                case LIKE:
                    return path.containsIgnoreCase(criteria.getValue().toString());
            }
        } else if (criteria.getOperation().getType().equalsIgnoreCase("date")) {
            DateTimePath<LocalDateTime> path = entityPath.getDateTime(criteria.getKey(), LocalDateTime.class);
            if (criteria.getOperation().equals(Operation.BETWEEN)) {
                LocalDateTime[] times = (LocalDateTime[]) criteria.getValue();
                return path.between(times[0], times[1]);
            }
        } else if (criteria.getOperation().getType().equalsIgnoreCase("enum")) {
            return EnumBooleanExpression.getExpression(criteria.getKey(), criteria.getValue(), entityPath);
        }
        return null;
    }
}
