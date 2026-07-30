package edu.utsa.cs3443.group6_project.model;


/**
 * Model class representing an individual workout event logged by the user.
 * Encapsulates workout metrics and supports CSV data conversion.
 *
 * CS 3443 - Final Application
 */
public class Workout {
    private String date;
    private String type;
    private String name;
    private double metric1;
    private int metric2;

    public Workout(String date, String type, String name, double metric1, int metric2) {
        this.date = date;
        this.type = type;
        this.name = name;
        this.metric1 = metric1;
        this.metric2 = metric2;
    }

    public String getDate() { return date; }
    public String getType() { return type; }
    public String getName() { return name; }
    public double getMetric1() { return metric1; }
    public int getMetric2() { return metric2; }

    public String toCSVRow() {
        return date + "," + type + "," + name + "," + metric1 + "," + metric2;
    }

    public static Workout fromCSVRow(String row) {
        String[] tokens = row.split(",");
        if (tokens.length < 5) return null;
        try {
            return new Workout(tokens[0], tokens[1], tokens[2],
                    Double.parseDouble(tokens[3]), Integer.parseInt(tokens[4]));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}