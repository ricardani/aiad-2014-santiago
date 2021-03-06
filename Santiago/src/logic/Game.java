package logic;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import utils.Pair;
import static utils.Statics.*;
import utils.TilePlacement;

public class Game {

    private final String[] terrains = {TYPE_BANANA, TYPE_BEAN, TYPE_POTATO, TYPE_SUGARCANE, TYPE_CHILLI};
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

    public Player getPlayer(Color c) {
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

    public void clearWaterLicitationForRound() {
        this.waterLicitationForRound.clear();
    }

    public void addWaterLicitationForRound(Pair p) {
        this.waterLicitationForRound.add(p);
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
        return b.getWaterPossiblePaths();
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

    private boolean contains(Vector<Vector<TilePlacement>> v, TilePlacement t) {
        for (int i = 0; i < v.size(); ++i) {
            for (int j = 0; j < v.get(i).size(); ++j) {
                if (v.get(i).get(j).getCoord().equals(t.getCoord())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void fillVec(Vector<Vector<TilePlacement>> v, int y, int x, String type, int index) {
        TilePlacement tile = new TilePlacement(b.getTiles()[y][x], x, y);
        if (!contains(v, tile)) {

            if (index < 0) {
                Vector<TilePlacement> aux = new Vector<>();
                aux.add(tile);
                v.add(aux);
                index = v.size() - 1;
            } else {
                if (b.getTiles()[y][x].getType().equals(type)) {
                    v.get(index).add(tile);
                } else {
                    return;
                }
            }
            if (y < 5) {
                fillVec(v, y + 1, x, type, index);
            }
            if (y > 0) {
                fillVec(v, y - 1, x, type, index);
            }
            if (x < 7) {
                fillVec(v, y, x + 1, type, index);
            }
            if (x > 0) {
                fillVec(v, y, x - 1, type, index);
            }
        }
    }

    public void calculateFinalResults() {
        Vector<Vector<TilePlacement>> vec = new Vector<>();
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
            for (Vector<TilePlacement> vec1 : vec) {
                int fields = vec1.size();
                int workers = 0;
                for (int j = 0; j < fields; ++j) {
                    if (vec1.get(j).getTile().getColor().equals(p.getColor())) {
                        workers += vec1.get(j).getTile().getWorkers();
                    }
                }
                p.receiveMoney(fields * workers);
            }
        }

        Vector<Player> playersVec = getPlayersVector();

        Collections.sort(playersVec);
        Collections.reverse(playersVec);

        order.clear();

        for (Player p : playersVec) {
            order.add(p.getColor());
        }

    }

    public boolean removeTileFromRound(Tile tileToRemove) {

        boolean result = false;

        for (Tile t : tilesForRound) {
            if (t.equals(tileToRemove)) {
                tilesForRound.remove(t);
                result = true;
                break;
            }

        }

        return result;
    }

    public void giveMoneyBonus() {
        for (Player player : players.values()) {
            player.receiveMoney(MONEY_BONUS);
        }
    }
}
