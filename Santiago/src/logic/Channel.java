/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.io.Serializable;
import utils.Pair;
import java.util.Vector;

/**
 *
 * @author Utilizador
 */
public class Channel implements Serializable{

    private boolean has_water;
    private final Vector<Pair<Integer, Integer>> adjacent_tiles = new Vector<>();
    private final Vector<Integer> adjacent_channels = new Vector<>();

    Channel() {
        has_water = false;
    }
    
    void add_adjacentTiles(int x, int y)
    {
        adjacent_tiles.add(new Pair(x,y));
    }
    
    void add_adjacentChannel(int c)
    {
        adjacent_channels.add(c);
    }
    
    public void fill()
    {
        has_water = true;
    }
    
    public boolean hasWater(){
        return has_water;
    }
    
    public Vector<Integer> getAdjacentChannels(){
        return adjacent_channels;
    }
    
    public Vector<Pair<Integer, Integer>> getAdjacentTiles(){
        return adjacent_tiles;
    }
    
}
