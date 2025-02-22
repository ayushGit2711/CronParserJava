package com.company;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CronParser {

    private static final Map<Integer, String> DAY_NAMES = Map.of(
            0, "Sunday", 1, "Monday", 2, "Tuesday", 3, "Wednesday", 4, "Thursday", 5, "Friday", 6, "Saturday"
    );
    private static final Map<Integer, String> MONTH = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(1, "January"),
            new AbstractMap.SimpleEntry<>(2, "February"),
            new AbstractMap.SimpleEntry<>(3, "March"),
            new AbstractMap.SimpleEntry<>(4, "April"),
            new AbstractMap.SimpleEntry<>(5, "May"),
            new AbstractMap.SimpleEntry<>(6, "June"),
            new AbstractMap.SimpleEntry<>(7, "July"),
            new AbstractMap.SimpleEntry<>(8, "August"),
            new AbstractMap.SimpleEntry<>(9, "September"),
            new AbstractMap.SimpleEntry<>(10, "October"),
            new AbstractMap.SimpleEntry<>(11, "November"),
            new AbstractMap.SimpleEntry<>(12, "December")
    );

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

        // Handle wildcard "*"
        if (field.equals("*")) {
            for (int i = min; i <= max; i++) {
                values.add(i);
            }
            return values;
        }

        // Handle comma-separated values (e.g., "1,5,10")
        if (field.contains(",")) {
            String[] splitValues = field.split(",");
            for (String value : splitValues) {
                values.addAll(parseField(value, min, max));
            }
            return values;
        }

        // Handle step values (e.g., "1-20/2")
        if (field.contains("/")) {
            String[] stepParts = field.split("/");
            int step = Integer.parseInt(stepParts[1]);
            String rangePart = stepParts[0];

            int start = min, end = max;
            if (rangePart.contains("-")) {
                String[] rangeBounds = rangePart.split("-");
                start = Integer.parseInt(rangeBounds[0]);
                end = Integer.parseInt(rangeBounds[1]);
            } else if (!rangePart.equals("*")) {
                start = Integer.parseInt(rangePart);
                end = start;
            }

            for (int i = start; i <= end; i += step) {
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

        // Handle single values (e.g., "5")
        int singleValue = Integer.parseInt(field);
        if (singleValue >= min && singleValue <= max) {
            values.add(singleValue);
        } else {
            throw new IllegalArgumentException("Value " + singleValue + " is out of bounds (" + min + "-" + max + ")");
        }

        return values;
    }

    public static String generateDescription(CronExpression expression) {
        StringBuilder desc = new StringBuilder("At ");

        // Minutes
        if (expression.getMinutes().size() == 1) {
            desc.append("minute ").append(expression.getMinutes().get(0));
        } else if (expression.getMinutes().size() > 1) {
            desc.append("every ").append(expression.getMinutes().get(1) - expression.getMinutes().get(0))
                    .append("th minute");
        }

        // Hours
        if (!expression.getHours().isEmpty()) {
            desc.append(" past hour ");
            desc.append(formatList(expression.getHours()));
        }

        // Days of the Week
        if (!expression.getDaysOfWeek().isEmpty()) {
            desc.append(" on ").append(formatList(expression.getDaysOfWeek().stream()
                    .map(DAY_NAMES::get)
                    .collect(Collectors.toList())));
        }

        return desc.toString();
    }


    private static String formatList(List<?> list) {
        if (list.size() == 1) {
            return list.get(0).toString();
        }
        return list.subList(0, list.size() - 1).stream()
                .map(Object::toString)
                .collect(Collectors.joining(", ")) +
                " and " + list.get(list.size() - 1);
    }


    public static List<LocalDateTime> getNextExecutions(CronExpression expression, LocalDateTime fromTime, int count) {
        List<LocalDateTime> nextExecutions = new ArrayList<>();
        LocalDateTime current = fromTime.truncatedTo(ChronoUnit.MINUTES);

        while (nextExecutions.size() < count) {
            current = current.plusMinutes(1);
            if (isValidExecutionTime(expression, current)) {
                nextExecutions.add(current);
            }
        }

        return nextExecutions;
    }

    private static boolean isValidExecutionTime(CronExpression expression, LocalDateTime time) {
        return expression.getMinutes().contains(time.getMinute()) &&
                expression.getHours().contains(time.getHour()) &&
                (expression.getDaysOfMonth().contains(time.getDayOfMonth()) || expression.getDaysOfMonth().contains(-1)) &&
                (expression.getDaysOfWeek().contains(time.getDayOfWeek().getValue()) || expression.getDaysOfWeek().contains(-1));
    }
}
