/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.awt.Color;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Utilizador
 */
public class Tile implements Serializable{

    private String type;
    private int workers;
    private Color color;

    public Tile() {

        type = "Ground";
        workers = 0;
        color = null;
    }

    public Tile(String t, int w) {
        type = t;
        workers = w;
    }

    public Tile(String t, int w, Color c) {
        type = t;
        workers = w;
        color = c;
    }

    public void setColor(Color c) {
        color = c;
    }

    public void desert() {
        type = "Desert";
        workers = 0;
        color = null;
    }

    public boolean can_plant() {
        return type.equals("Ground");
    }

    @Override
    public String toString(){
        return "Tipo: " + type + "\tTrabalhadores: " + workers;
    }
    
    public String getType()
    {
        return type;
    }
    
    public int getWorkers()
    {
        return workers;
    }

    public Color getColor() {
        return color;
    }
    
    public void notIrrigated(){
        workers--;
        if(workers == 0)
            this.desert();
    }
    
    public boolean hasPlantation(){
        return !(type.equals("Desert") || type.equals("Ground"));
    }

    boolean isDesert() {
        return type.equals("Desert");
    }

    boolean isGround() {
        return type.equals("Ground");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final Tile other = (Tile) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return this.workers == other.workers;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.type);
        hash = 13 * hash + this.workers;
        return hash;
    }
    
    
}
