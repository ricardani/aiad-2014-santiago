package utils;

import java.io.Serializable;
import logic.Tile;

public class TilePlacement implements Serializable{
    
    private final Tile t;
    private final Pair coord;

    public Tile getTile() {
        return t;
    }

    public Pair getCoord() {
        return coord;
    }

    public TilePlacement(Tile t, Pair coord) {
        this.t = t;
        this.coord = coord;
    }
    
    public TilePlacement(Tile t, int x, int y) {
        this.t = t;
        this.coord = new Pair(y,x);
    }
    
    
    
    
}
