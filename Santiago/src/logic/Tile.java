package logic;

import java.awt.Color;
import java.io.Serializable;
import java.util.Objects;
import static utils.Statics.*;

public class Tile implements Serializable{

    private String type;
    private int workers;
    private Color color;

    public Tile() {

        type = TYPE_GROUND;
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
        type = TYPE_DESERT;
        workers = 0;
        color = null;
    }

    public boolean can_plant() {
        return type.equals(TYPE_GROUND);
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
        return !(type.equals(TYPE_DESERT) || type.equals(TYPE_GROUND));
    }

    boolean isDesert() {
        return type.equals(TYPE_DESERT);
    }

    boolean isGround() {
        return type.equals(TYPE_GROUND);
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
