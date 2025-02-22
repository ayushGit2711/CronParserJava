package com.company;

import java.util.List;

public class CronExpression {
    private List<Integer> minutes;
    private List<Integer> hours;
    private List<Integer> daysOfMonth;
    private List<Integer> months;
    private List<Integer> daysOfWeek;

    public CronExpression(List<Integer> minutes, List<Integer> hours, List<Integer> daysOfMonth,
                          List<Integer> months, List<Integer> daysOfWeek) {
        this.minutes = minutes;
        this.hours = hours;
        this.daysOfMonth = daysOfMonth;
        this.months = months;
        this.daysOfWeek = daysOfWeek;
    }

    public List<Integer> getMinutes() { return minutes; }
    public List<Integer> getHours() { return hours; }
    public List<Integer> getDaysOfMonth() { return daysOfMonth; }
    public List<Integer> getMonths() { return months; }
    public List<Integer> getDaysOfWeek() { return daysOfWeek; }

    @Override
    public String toString() {
        return "Minutes: " + minutes + "\n" +
                "Hours: " + hours + "\n" +
                "Days of Month: " + daysOfMonth + "\n" +
                "Months: " + months + "\n" +
                "Days of Week: " + daysOfWeek;
    }
}
