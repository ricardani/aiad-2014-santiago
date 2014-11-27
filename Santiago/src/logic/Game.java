/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package logic;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import utils.Pair;
import static utils.Statics.*;
import static utils.VectorUtils.*;

/**
 *
 * @author Ricardo
 */
public class Game {
    
    private final String[] terrains = {"Banana", "Feijao", "Potato", "Sugar", "Chilli"};
    private Map<String, Player> players;
    private Vector<Color> order = new Vector<>();
    
    private final Board b = new Board();
    
    private final Vector<Tile> tilesForRound = new Vector<>();
    private Vector<Integer> tilesLicitationForRound = new Vector<>();
    private Vector<Pair<Integer, Integer>> waterLicitationForRound = new Vector<>();
    
    public Game() {
        this.players = new HashMap<>();
        fillStaticVars();
        
        b.fillWaterChannel(RANDOM_GENERATOR.nextInt(31));
    }
    
    public boolean setNumberOfPlayers(int numPlayers) {
        
        if (numPlayers < 3 || numPlayers > 5) {
            return false;
        }
        
        for (int i = 0; i < numPlayers; ++i) {
            Player p = new Player(PlayerColor.get(i));
            players.put(p.toString(), p);
            order.add(p.getColor());
        }
        
        return true;
        
    }
    
    public Vector<Color> getOrder() {
        return order;
    }
    
    public Player getPlayer(Color c){
        return players.get(c.toString());
    }
    
    public void setOrder(Vector<Color> o) {
        order = o;
    }
    
    public Map<String, Player> getPlayers() {
        return players;
    }
    
    public Vector<Player> getPlayersVector() {
        
        Vector<Player> p = new Vector<>();
        
        for (Player player : players.values()) {
            p.add(player);
        }
        
        return p;
    }
    
    public void setPlayers(Map<String, Player> p) {
        players = p;
    }
    
    public Tile[][] getBoardTiles() {
        return b.getTiles();
    }
    
    public Board getBoard() {
        return b;
    }
    
    public Vector<Integer> getLicitationForRound() {
        return tilesLicitationForRound;
    }
    
    public void setLicitationForRound(Vector<Integer> licitationForRound) {
        this.tilesLicitationForRound = licitationForRound;
    }
    
    public Vector<Pair<Integer, Integer>> getWaterLicitationForRound() {
        return waterLicitationForRound;
    }
    
    public void setWaterLicitationForRound(Vector<Pair<Integer, Integer>> waterLicitationForRound) {
        this.waterLicitationForRound = waterLicitationForRound;
    }
    
    public void newTilesForRound() {
        tilesForRound.clear();
        for (int i = 0; i <= players.size(); ++i) {
            tilesForRound.add(new Tile(terrains[RANDOM_GENERATOR.nextInt(5)], RANDOM_GENERATOR.nextInt(2) + 1));
        }
    }
    
    public Vector<Tile> getTilesForRound() {
        return tilesForRound;
    }
    
    public Channel[] getWaterChannels() {
        return b.getChannels();
    }
    
    private int emptyTiles() {
        int emptyTiles = 0;
        Tile[][] tiles = getBoardTiles();
        
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (tiles[i][j].can_plant()) {
                    emptyTiles++;
                }
            }
        }
        
        return emptyTiles;
    }
    
    public boolean finish() {
        return emptyTiles() <= players.size();
    }
    
    public boolean plant(Tile t, Color c, int x, int y) {
        t.setColor(c);
        
        return b.plant(x, y, t);
    }
    
    public Vector<Integer> getWaterPossiblePaths() {
        
        Vector<Integer> waterPossibelPaths = new Vector<>();
        Channel[] channels = b.getChannels();
        
        for (Channel channel : channels) {
            if (channel.hasWater()) {
                waterPossibelPaths.addAll(channel.getAdjacentChannels());
            }
        }
        
        waterPossibelPaths = removeDuplicates(waterPossibelPaths);
        
        return waterPossibelPaths;
    }
    
    public void placeWaterChannel(int channel) {
        
        int money = 0;
        Player p;
        Vector<Integer> waterPossibelPaths = getWaterPossiblePaths();
        
        for (int i = 0; i < waterLicitationForRound.size(); i++) {
            if (waterLicitationForRound.get(i).getFirst() == channel) {
                money += waterLicitationForRound.get(i).getSecond();
                p = players.get(order.get(i).toString());
                p.Pay(waterLicitationForRound.get(i).getSecond());
            }
        }
        
        p = players.get(order.get(order.size() - 1).toString());
        
        p.receiveMoney(money);
        
        b.fillWaterChannel(waterPossibelPaths.get(channel));
        
    }
    
    public void checkTilesIrrigation() {
        
        Tile[][] tiles = b.getTiles();
        Channel[] channels = b.getChannels();
        boolean irrigated;
        Pair p;
        
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 6; y++) {
                if (tiles[y][x].hasPlantation()) {
                    irrigated = false;
                    p = new Pair(y, x);
                    
                    for (int i = 0; i < 31; i++) {
                        if (channels[i].hasWater()) {
                            if (channels[i].getAdjacentTiles().contains(p)) {
                                irrigated = true;
                            }
                        }
                    }
                    
                    if (!irrigated) {
                        tiles[y][x].notIrrigated();
                    }
                }
            }
        }
        
    }
    
    private boolean contains(Vector<Vector<Tile>> v, Tile t) {
        for (int i = 0; i < v.size(); ++i) {
            if (v.get(i).contains(t)) {
                return true;
            }
        }
        return false;
    }
    
    private void fillVec(Vector<Vector<Tile>> v, int i, int j, String type, int index) {
        if (!contains(v, b.getTiles()[i][j])) {
            
            if (index < 0) {
                Vector<Tile> aux = new Vector<>();
                aux.add(b.getTiles()[i][j]);
                v.add(aux);
                index = v.size() - 1;
            } else {
                if (b.getTiles()[i][j].getType().equals(type)) {
                    v.get(index).add(b.getTiles()[i][j]);
                } else {
                    return;
                }
            }
            if(i < 7)
                fillVec(v, i + 1, j, type, index);
            if(i > 0)
                fillVec(v, i - 1, j, type, index);
            if(j < 5)
                fillVec(v, i, j + 1, type, index);
            if(j > 0)
                fillVec(v, i, j - 1, type, index);
        }
    }
    
    public void endGame() {
        Vector<Vector<Tile>> vec = new Vector<>();
        for (int i = 0; i < b.getTiles().length; ++i) {
            for (int j = 0; j < b.getTiles()[i].length; ++j) {
                Tile tiles = b.getTiles()[i][j];
                if (!tiles.isDesert() && !tiles.isGround()) {
                    fillVec(vec, i, j, tiles.getType(), -1);
                }
            }
        }
        for (int a = 0; a < order.size(); ++a) {
            Player p = players.get(order.get(a).toString());
            for (Vector<Tile> vec1 : vec) {
                int fields = vec1.size();
                int workers = 0;
                for (int j = 0; j < fields; ++j) {
                    if (vec1.get(j).getColor().equals(p.getColor())) {
                        workers += vec1.get(j).getWorkers();
                    }
                }
                p.receiveMoney(fields * workers);
            }
        }
    }
    
    
    public boolean removeTileFromRound(Tile tileToRemove){
        
        boolean result = false;
        
        for(Tile t : tilesForRound){
            if(t.equals(tileToRemove)){
                tilesForRound.remove(t);
                result = true;
                break;
            }
            
        }
        
        return result;
    }
}
