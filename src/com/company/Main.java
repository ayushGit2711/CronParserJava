package com.company;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter cron expression:");
        String cronExpression = scanner.nextLine();

        try {
            CronExpression parsedCron = CronParser.parse(cronExpression);
            System.out.println(parsedCron);
            String description = CronParser.generateDescription(parsedCron);
            List<LocalDateTime> nextExecutions = CronParser.getNextExecutions(parsedCron, LocalDateTime.now(), 5);

            System.out.println("\nCron Description: " + description);
            System.out.println("\nNext 5 Execution Times:");
            for (LocalDateTime time : nextExecutions) {
                System.out.println(" - " + time);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}
