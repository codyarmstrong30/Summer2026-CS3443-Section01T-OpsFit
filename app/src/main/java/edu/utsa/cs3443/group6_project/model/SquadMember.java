package edu.utsa.cs3443.group6_project.model;

public class SquadMember {
    private String name;
    private int xp;
    private int rankLevel;
    private String avatarColor;

    public SquadMember(String name, int xp, int rankLevel, String avatarColor) {
        this.name = name;
        this.xp = xp;
        this.rankLevel = rankLevel;
        this.avatarColor = avatarColor;
    }

    public String getName() { return name; }
    public int getXp() { return xp; }
    public int getRankLevel() { return rankLevel; }
    public String getAvatarColor() { return avatarColor; }

    public String getRankName() {
        if (xp >= 1000) return "Corporal";
        if (xp >= 500) return "Private";
        if (rankLevel == 2) return "Recruit Rank 2";
        return "Recruit Rank 1";
    }
}