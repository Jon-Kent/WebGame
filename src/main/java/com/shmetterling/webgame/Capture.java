/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shmetterling.webgame;

import java.util.Map;

/**
 *
 * @author Jon Kent
 */
public class Capture {
    
    private static int playerRadius = 10;
    private static int beastRadius = 20;
   
    public static void checkCapture(Map<String, Player> players){
        for(Player b : players.values()){
            if(b.isBeast()){
                for(Player p : players.values()){
                    if(!p.isBeast()){
                        double distance = findDistance(b.getX(), b.getY(), p.getX(), p.getY());
                        //System.out.println("distance= " + distance);
                        if(distance < playerRadius + beastRadius){
                            p.setAlive(false);
                        }
                    }
                }
            }
        } 
    }
    
    private static double findDistance(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow(x2 - x1,2) + Math.pow(y2 - y1, 2));
    }
    
}
