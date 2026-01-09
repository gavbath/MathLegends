package com.gavbath.mathlegends;

public class Player { //creates the player class, making private instance variables for 4 fields.
    private String name;
    private int score;
    private long timeUsed;
    private String summary;
    
    public Player(String name) { //the paramaterized constructor for Player, taking in only a name
                                 // since thats all we know about the user at first.
        this.name = name;
        this.score = 0;
        this.summary = "";
        this.timeUsed = 0;
    }
    
    //below are the getters for all 4 private instance variables.
    
    public String getName() { 
        return name;
    }
    public int getScore() {
        return score;
    }
    public long getTimeUsed() {
        return timeUsed;
    }
    public String getSummary() {
        return summary;
    }
    
    //below are the setters for all 3 private instance variables, excluding 
    //name since that doesn't need to change later.
    
    public void setScore(int score) {
        this.score = score;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public void setTimeUsed(long timeTaken) {
        this.timeUsed = timeTaken;
    }
}