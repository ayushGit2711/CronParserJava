package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter cron expression:");
        String cronExpression = scanner.nextLine();

        try {
            CronExpression parsedCron = CronParser.parse(cronExpression);
            System.out.println("Parsed Cron Expression:");
            System.out.println(parsedCron);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}
