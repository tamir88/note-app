package com.tamirHamtzany.enumeration;

public enum Level {
    HIGH (3),
    MEDIUM (2),
    LOW (1);

    private final int level;

    Level(int level){
        this.level = level;
    }

    public int getLevel(){
        return level;
    }

}
