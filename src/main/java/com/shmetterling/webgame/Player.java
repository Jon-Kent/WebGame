/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shmetterling.webgame;

/**
 *
 * @author qac
 */
public class Player {
   
    private String name;
    private String color = "red";
    private boolean beast;
    private int x = 15;
    private int y = 15;
    private boolean alive = true;
    
    public Player(){};
    
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Player(String name, String color, boolean beast) {
        this.name = name;
        this.color = color;
        this.beast = beast;
        this.x = 50;
        this.y = 450;
    }
    
    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getColor() {
        return color;
    }
    
    public String getName(){
        return this.name;
    }
    
    public boolean isBeast(){
        return beast;
    }
    
    public void setAlive(boolean alive){
        this.alive = alive;
    }
    
    public boolean isAlive(){
        return alive;
    }     
}
