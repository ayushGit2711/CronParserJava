package com.company;

import java.util.ArrayList;
import java.util.List;

public class CronParser {

    public static CronExpression parse(String cronExpression) {
        String[] parts = cronExpression.split("\\s+");

        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid cron format. Expected 5 fields.");
        }

        return new CronExpression(
                parseField(parts[0], 0, 59),  // Minutes
                parseField(parts[1], 0, 23),  // Hours
                parseField(parts[2], 1, 31),  // Day of Month
                parseField(parts[3], 1, 12),  // Month
                parseField(parts[4], 0, 6)    // Day of Week
        );
    }

    private static List<Integer> parseField(String field, int min, int max) {
        List<Integer> values = new ArrayList<>();

        // Handle comma-separated values (e.g., "1,5,10")
        if (field.contains(",")) {
            String[] splitValues = field.split(",");
            for (String value : splitValues) {
                values.addAll(parseField(value, min, max));
            }
            return values;
        }

        // Handle step values (e.g., "*/15")
        if (field.contains("/")) {
            String[] stepParts = field.split("/");
            int step = Integer.parseInt(stepParts[1]);
            for (int i = min; i <= max; i += step) {
                values.add(i);
            }
            return values;
        }

        // Handle range values (e.g., "1-5")
        if (field.contains("-")) {
            String[] range = field.split("-");
            int start = Integer.parseInt(range[0]);
            int end = Integer.parseInt(range[1]);
            for (int i = start; i <= end; i++) {
                values.add(i);
            }
            return values;
        }

        // Handle wildcard "*"
        if (field.equals("*")) {
            for (int i = min; i <= max; i++) {
                values.add(i);
            }
            return values;
        }

        // Handle single values (e.g., "5")
        int singleValue = Integer.parseInt(field);
        if (singleValue >= min && singleValue <= max) {
            values.add(singleValue);
        } else {
            throw new IllegalArgumentException("Value " + singleValue + " is out of bounds (" + min + "-" + max + ")");
        }

        return values;
    }
}
