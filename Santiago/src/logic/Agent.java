package logic;

import java.util.Vector;
import utils.Pair;
import static utils.Statics.*;
import utils.TilePlacement;

public class Agent {

    public static int generateLicitation(Player p, int logic, Vector<Player> vp) {
        int money = 0;

        switch (logic) {
            case LOGIC_RANDOM:
                money = RANDOM_GENERATOR.nextInt(p.getEscudos());
                break;
            case LOGIC_SPENDER:
                money = getSpenderLicitation(p, vp);
                break;
            case LOGIC_SAVER:
                money = getSaverLicitation(p, vp);
                break;
        }

        return money;
    }

    public static TilePlacement generatePlantChoice(Player p, int logic, Board b, Vector<Tile> tiles) {
        TilePlacement tile = null;

        switch (logic) {
            case LOGIC_RANDOM:
                tile = randomTilePlacement(tiles, b);
                break;
            case LOGIC_SPENDER:
                tile = spenderTilePlacement(tiles, b);
                break;
            case LOGIC_SAVER:
                tile = saverTilePlacement(tiles, b);
                break;
        }

        return tile;
    }

    public static Pair generateWaterLicitation(Player p, int logic, Vector<Player> vp, Vector<Integer> waterPaths, Board b) {
        int waterChoice = 0, money = 0;

        switch (logic) {
            case LOGIC_RANDOM:
                waterChoice = RANDOM_GENERATOR.nextInt(waterPaths.size());
                money = RANDOM_GENERATOR.nextInt(p.getEscudos());
                break;
            case LOGIC_SPENDER:
                waterChoice = getWaterPositionChoice(p, waterPaths, b);
                money = getSpenderLicitation(p, vp);
                break;
            case LOGIC_SAVER:
                waterChoice = getWaterPositionChoice(p, waterPaths, b);
                money = getSaverLicitation(p, vp);
                break;
        }

        return new Pair(waterChoice, money);
    }

    public static int generateWaterPlacement(Player p, int logic, Vector<Player> vp, Vector<Integer> waterPaths, Vector<Pair> waterLicitations, Board b) {

        int choice = 0;

        switch (logic) {
            case LOGIC_RANDOM:
                choice = RANDOM_GENERATOR.nextInt(waterPaths.size());
                break;
            case LOGIC_SPENDER:
                choice = getSpenderWaterPlacement(waterLicitations, waterPaths, b, p);
                break;
            case LOGIC_SAVER:
                choice = getSaverWaterPlacement(waterLicitations, waterPaths);
                break;
        }

        return choice;

    }

    private static int getSpenderLicitation(Player p, Vector<Player> vp) {
        int maxEscudos = getMaxEscudos(p, vp);

        if (maxEscudos < 0.8 * p.getEscudos()) {
            return maxEscudos + 1;
        }

        if (0.8 * maxEscudos < p.getEscudos()) {
            return (int) Math.ceil(0.8 * maxEscudos);
        }

        if (0.6 * maxEscudos < p.getEscudos()) {
            return (int) Math.ceil(0.6 * maxEscudos);
        }
        if (0.4 * maxEscudos < p.getEscudos()) {
            return (int) Math.ceil(0.4 * maxEscudos);
        }
        if (0.2 * maxEscudos < p.getEscudos()) {
            return (int) Math.ceil(0.2 * maxEscudos);
        }
        return 0;
    }

    private static int getSaverLicitation(Player p, Vector<Player> vp) {
        int maxEscudos = getMaxEscudos(p, vp);

        if (maxEscudos < 0.2 * p.getEscudos()) {
            return maxEscudos;
        }

        return 0;
    }

    private static int getMaxEscudos(Player p, Vector<Player> vp) {
        int maxEscudos = 0;
        for (Player otherP : vp) {
            if (!otherP.getColor().equals(p.getColor())) {
                if (otherP.getEscudos() > maxEscudos) {
                    maxEscudos = otherP.getEscudos();
                }
            }
        }
        return maxEscudos;
    }

    private static TilePlacement randomTilePlacement(Vector<Tile> tiles, Board b) {
        int index = RANDOM_GENERATOR.nextInt(tiles.size());
        int x, y;
        do {
            x = RANDOM_GENERATOR.nextInt(8);
            y = RANDOM_GENERATOR.nextInt(6);
        } while (b.getTiles()[y][x].can_plant());

        return new TilePlacement(tiles.get(index), x, y);
    }

    private static TilePlacement spenderTilePlacement(Vector<Tile> tiles, Board b) {
        Tile choice = tiles.get(0);
        for (int i = 1; i < tiles.size(); ++i) {
            if (tiles.get(i).getWorkers() > choice.getWorkers()) {
                choice = tiles.get(i);
            }
        }
        String type = choice.getType();
        TilePlacement tp = null;
        if (getCloseField(b, type, choice, tp)) {
            return tp;
        }
        if (getCloseWater(b, type, choice, tp)) {
            return tp;
        }
        return getFirstChoice(b, type, choice);
    }

    private static TilePlacement saverTilePlacement(Vector<Tile> tiles, Board b) {
        Tile choice = tiles.get(0);
        for (int i = 1; i < tiles.size(); ++i) {
            if (tiles.get(i).getWorkers() > choice.getWorkers()) {
                choice = tiles.get(i);
            }
        }
        String type = choice.getType();
        TilePlacement tp = null;

        if (getCloseWater(b, type, choice, tp)) {
            return tp;
        }
        if (getCloseField(b, type, choice, tp)) {
            return tp;
        }
        return getFirstChoice(b, type, choice);
    }

    private static boolean getCloseField(Board b, String type, Tile choice, TilePlacement tp) {
        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 6; ++y) {

                if (b.getTiles()[y][x].can_plant()) {
                    if (x > 0) {
                        if (b.getTiles()[y][x - 1].getType().equals(type)) {
                            tp = new TilePlacement(choice, x, y);
                            return true;
                        }
                    } else if (x < 7) {
                        if (b.getTiles()[y][x + 1].getType().equals(type)) {
                            tp = new TilePlacement(choice, x, y);
                            return true;
                        }
                    } else if (y > 0) {
                        if (b.getTiles()[y - 1][x].getType().equals(type)) {
                            tp = new TilePlacement(choice, x, y);
                            return true;
                        }
                    } else if (y < 5) {
                        if (b.getTiles()[y + 1][x].getType().equals(type)) {
                            tp = new TilePlacement(choice, x, y);
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }

    private static boolean getCloseWater(Board b, String type, Tile choice, TilePlacement tp) {
        for (int i = 0; i < b.getChannels().length; ++i) {
            if (b.getChannels()[i].hasWater()) {
                Vector<Pair<Integer, Integer>> possibleChoices = b.getChannels()[i].getAdjacentTiles();
                for (int j = 0; j < possibleChoices.size(); ++j) {
                    if (b.getTiles()[possibleChoices.get(i).getSecond()][possibleChoices.get(i).getFirst()].can_plant()) {
                        tp = new TilePlacement(choice, possibleChoices.get(i).getFirst(), possibleChoices.get(i).getSecond());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static TilePlacement getFirstChoice(Board b, String type, Tile choice) {
        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 6; ++y) {
                if (b.getTiles()[y][x].can_plant()) {
                    return new TilePlacement(choice, x, y);
                }
            }
        }
        return new TilePlacement(choice, 0, 0);
    }

    private static int getWaterPositionChoice(Player p, Vector<Integer> waterPaths, Board b) {
        for (int i = 0; i < waterPaths.size(); ++i) {
            Vector<Pair<Integer, Integer>> AdjacentTiles = b.getChannels()[waterPaths.get(i)].getAdjacentTiles();
            for (int j = 0; j < AdjacentTiles.size(); ++j) {
                if (b.getTiles()[AdjacentTiles.get(j).getSecond()][AdjacentTiles.get(j).getFirst()]
                        .can_plant()) {
                    return i;
                }
            }
        }

        return RANDOM_GENERATOR.nextInt(waterPaths.size());
    }

    private static int getSaverWaterPlacement(Vector<Pair> waterLicitations, Vector<Integer> waterPaths) {
        int choice = 0, money = (int) waterLicitations.get(0).getSecond();

        for (int i = 1; i < waterLicitations.size(); ++i) {
            if ((int) waterLicitations.get(i).getSecond() > money) {
                choice = i;
                money = (int) waterLicitations.get(i).getSecond();
            }
        }

        return waterPaths.indexOf(waterLicitations.get(choice));
    }

    private static int getSpenderWaterPlacement(Vector<Pair> waterLicitations, Vector<Integer> waterPaths, Board b, Player p) {
        for (int i = 0; i < waterPaths.size(); ++i) {
            Vector<Pair<Integer, Integer>> vec = b.getChannels()[waterPaths.get(i)].getAdjacentTiles();
            for (int j = 0; j < waterPaths.size(); ++j) {
                if (!b.isIrrigated(vec.get(j).getFirst(), vec.get(j).getSecond()) &&
                        b.getTiles()[vec.get(j).getSecond()][vec.get(j).getFirst()].getColor().equals(p.getColor())) {
                    return i;
                }
            }
        }
        
        return getSaverWaterPlacement(waterLicitations, waterPaths);
    }
}
