/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package logic;

import java.io.Serializable;

/**
 *
 * @author Utilizador
 */
public class Board implements Serializable{
    
    private final Tile[][] tile_array = new Tile[6][8];
    private final Channel[] channel_array = new Channel[31];
    
    public Board() {
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 8; ++j) {
                tile_array[i][j] = new Tile();
            }
        }
        
        addAdjacentTiles();
        
        addAdjacentChannels();
        
    }

    public void desert(int x, int y) {
        tile_array[y][x].desert();
    }
    
    public boolean plant(int x, int y, Tile tile) {
        if (tile_array[y][x].can_plant()) {
            tile_array[y][x] = tile;
            return true;
        }
        return false;
    }
    
    public Tile[][] getTiles(){
        return tile_array;
    }
    
    public Channel[] getChannels(){
        return channel_array;
    }
    
    public void fillWaterChannel(int n){
        channel_array[n].fill();
    }
    
        
    private void addAdjacentTiles() {
        for (int i = 0; i < 4; ++i) {
            channel_array[i] = new Channel();
            channel_array[i].add_adjacentTiles(0, 2 * i);
            channel_array[i].add_adjacentTiles(0, 2 * i + 1);
        }
        
        for (int i = 4; i < 8; ++i) {
            channel_array[i] = new Channel();
            channel_array[i].add_adjacentTiles(1, (i - 4) * 2);
            channel_array[i].add_adjacentTiles(1, (i - 4) * 2 + 1);
            channel_array[i].add_adjacentTiles(2, (i - 4) * 2);
            channel_array[i].add_adjacentTiles(2, (i - 4) * 2 + 1);
        }
        
        for (int i = 8; i < 12; ++i) {
            channel_array[i] = new Channel();
            channel_array[i].add_adjacentTiles(3, (i - 8) * 2);
            channel_array[i].add_adjacentTiles(3, (i - 8) * 2 + 1);
            channel_array[i].add_adjacentTiles(4, (i - 8) * 2);
            channel_array[i].add_adjacentTiles(4, (i - 8) * 2 + 1);
        }
        
        for (int i = 12; i < 16; ++i) {
            channel_array[i] = new Channel();
            channel_array[i].add_adjacentTiles(5, (i - 12) * 2);
            channel_array[i].add_adjacentTiles(5, (i - 12) * 2 + 1);
        }
        
        for (int i = 16; i < 19; ++i) {
            channel_array[i] = new Channel();
            channel_array[i].add_adjacentTiles((i - 16) * 2, 0);
            channel_array[i].add_adjacentTiles((i - 16) * 2 + 1, 0);
        }
        
        for (int i = 19; i < 22; ++i) {
            channel_array[i] = new Channel();
            channel_array[i].add_adjacentTiles((i - 19) * 2, 1);
            channel_array[i].add_adjacentTiles((i - 19) * 2 + 1, 1);
            channel_array[i].add_adjacentTiles((i - 19) * 2, 2);
            channel_array[i].add_adjacentTiles((i - 19) * 2 + 1, 2);
        }
        
        for (int i = 22; i < 25; ++i) {
            channel_array[i] = new Channel();
            channel_array[i].add_adjacentTiles((i - 22) * 2, 3);
            channel_array[i].add_adjacentTiles((i - 22) * 2 + 1, 3);
            channel_array[i].add_adjacentTiles((i - 22) * 2, 4);
            channel_array[i].add_adjacentTiles((i - 22) * 2 + 1, 4);
        }
        
        for (int i = 25; i < 28; ++i) {
            channel_array[i] = new Channel();
            channel_array[i].add_adjacentTiles((i - 25) * 2, 5);
            channel_array[i].add_adjacentTiles((i - 25) * 2 + 1, 5);
            channel_array[i].add_adjacentTiles((i - 25) * 2, 6);
            channel_array[i].add_adjacentTiles((i - 25) * 2 + 1, 6);
        }
        
        for (int i = 28; i < 31; ++i) {
            channel_array[i] = new Channel();
            channel_array[i].add_adjacentTiles((i - 28) * 2, 7);
            channel_array[i].add_adjacentTiles((i - 28) * 2 + 1, 7);
        }
    }
    
    // Melhorar este metodo
    private void addAdjacentChannels(){
        
        channel_array[0].add_adjacentChannel(1);
        channel_array[0].add_adjacentChannel(16);
        channel_array[0].add_adjacentChannel(19);
        
        channel_array[1].add_adjacentChannel(0);
        channel_array[1].add_adjacentChannel(2);
        channel_array[1].add_adjacentChannel(19);
        channel_array[1].add_adjacentChannel(22);
        
        channel_array[2].add_adjacentChannel(1);
        channel_array[2].add_adjacentChannel(3);
        channel_array[2].add_adjacentChannel(22);
        channel_array[2].add_adjacentChannel(25);
        
        channel_array[3].add_adjacentChannel(2);
        channel_array[3].add_adjacentChannel(25);
        channel_array[3].add_adjacentChannel(28);
        
        channel_array[4].add_adjacentChannel(5);
        channel_array[4].add_adjacentChannel(16);
        channel_array[4].add_adjacentChannel(17);
        channel_array[4].add_adjacentChannel(19);
        channel_array[4].add_adjacentChannel(20);
        
        channel_array[5].add_adjacentChannel(4);
        channel_array[5].add_adjacentChannel(6);
        channel_array[5].add_adjacentChannel(19);
        channel_array[5].add_adjacentChannel(20);
        channel_array[5].add_adjacentChannel(22);
        channel_array[5].add_adjacentChannel(23);
        
        channel_array[6].add_adjacentChannel(5);
        channel_array[6].add_adjacentChannel(7);
        channel_array[6].add_adjacentChannel(22);
        channel_array[6].add_adjacentChannel(23);
        channel_array[6].add_adjacentChannel(25);
        channel_array[6].add_adjacentChannel(26);
        
        channel_array[7].add_adjacentChannel(6);
        channel_array[7].add_adjacentChannel(25);
        channel_array[7].add_adjacentChannel(26);
        channel_array[7].add_adjacentChannel(28);
        channel_array[7].add_adjacentChannel(29);
        
        channel_array[8].add_adjacentChannel(9);
        channel_array[8].add_adjacentChannel(17);
        channel_array[8].add_adjacentChannel(18);
        channel_array[8].add_adjacentChannel(20);
        channel_array[8].add_adjacentChannel(21);
        
        channel_array[9].add_adjacentChannel(8);
        channel_array[9].add_adjacentChannel(10);
        channel_array[9].add_adjacentChannel(20);
        channel_array[9].add_adjacentChannel(21);
        channel_array[9].add_adjacentChannel(23);
        channel_array[9].add_adjacentChannel(24);
        
        channel_array[10].add_adjacentChannel(9);
        channel_array[10].add_adjacentChannel(11);
        channel_array[10].add_adjacentChannel(23);
        channel_array[10].add_adjacentChannel(24);
        channel_array[10].add_adjacentChannel(26);
        channel_array[10].add_adjacentChannel(27);
        
        channel_array[11].add_adjacentChannel(10);
        channel_array[11].add_adjacentChannel(26);
        channel_array[11].add_adjacentChannel(27);
        channel_array[11].add_adjacentChannel(29);
        channel_array[11].add_adjacentChannel(30);
        
        channel_array[12].add_adjacentChannel(13);
        channel_array[12].add_adjacentChannel(18);
        channel_array[12].add_adjacentChannel(21);
        
        channel_array[13].add_adjacentChannel(12);
        channel_array[13].add_adjacentChannel(14);
        channel_array[13].add_adjacentChannel(21);
        channel_array[13].add_adjacentChannel(24);
        
        channel_array[14].add_adjacentChannel(13);
        channel_array[14].add_adjacentChannel(15);
        channel_array[14].add_adjacentChannel(24);
        channel_array[14].add_adjacentChannel(27);
        
        channel_array[15].add_adjacentChannel(14);
        channel_array[15].add_adjacentChannel(27);
        channel_array[15].add_adjacentChannel(30);
        
        
        channel_array[16].add_adjacentChannel(0);
        channel_array[16].add_adjacentChannel(4);
        channel_array[16].add_adjacentChannel(17);
        
        channel_array[17].add_adjacentChannel(4);
        channel_array[17].add_adjacentChannel(8);
        channel_array[17].add_adjacentChannel(16);
        channel_array[17].add_adjacentChannel(18);
        
        channel_array[18].add_adjacentChannel(8);
        channel_array[18].add_adjacentChannel(12);
        channel_array[18].add_adjacentChannel(17);
        
        channel_array[19].add_adjacentChannel(0);
        channel_array[19].add_adjacentChannel(1);
        channel_array[19].add_adjacentChannel(4);
        channel_array[19].add_adjacentChannel(5);
        channel_array[19].add_adjacentChannel(20);
        
        channel_array[20].add_adjacentChannel(4);
        channel_array[20].add_adjacentChannel(5);
        channel_array[20].add_adjacentChannel(8);
        channel_array[20].add_adjacentChannel(9);
        channel_array[20].add_adjacentChannel(19);
        channel_array[20].add_adjacentChannel(21);
        
        channel_array[21].add_adjacentChannel(8);
        channel_array[21].add_adjacentChannel(9);
        channel_array[21].add_adjacentChannel(12);
        channel_array[21].add_adjacentChannel(13);
        channel_array[21].add_adjacentChannel(20);
        
        channel_array[22].add_adjacentChannel(1);
        channel_array[22].add_adjacentChannel(2);
        channel_array[22].add_adjacentChannel(5);
        channel_array[22].add_adjacentChannel(6);
        channel_array[22].add_adjacentChannel(23);
        
        channel_array[23].add_adjacentChannel(5);
        channel_array[23].add_adjacentChannel(6);
        channel_array[23].add_adjacentChannel(9);
        channel_array[23].add_adjacentChannel(10);
        channel_array[23].add_adjacentChannel(22);
        channel_array[23].add_adjacentChannel(24);
        
        channel_array[24].add_adjacentChannel(9);
        channel_array[24].add_adjacentChannel(10);
        channel_array[24].add_adjacentChannel(13);
        channel_array[24].add_adjacentChannel(14);
        channel_array[24].add_adjacentChannel(23);
        
        channel_array[25].add_adjacentChannel(2);
        channel_array[25].add_adjacentChannel(3);
        channel_array[25].add_adjacentChannel(6);
        channel_array[25].add_adjacentChannel(7);
        channel_array[25].add_adjacentChannel(26);
        
        channel_array[26].add_adjacentChannel(6);
        channel_array[26].add_adjacentChannel(7);
        channel_array[26].add_adjacentChannel(10);
        channel_array[26].add_adjacentChannel(11);
        channel_array[26].add_adjacentChannel(25);
        channel_array[26].add_adjacentChannel(27);
        
        channel_array[27].add_adjacentChannel(10);
        channel_array[27].add_adjacentChannel(11);
        channel_array[27].add_adjacentChannel(14);
        channel_array[27].add_adjacentChannel(15);
        channel_array[27].add_adjacentChannel(26);
        
        channel_array[28].add_adjacentChannel(3);
        channel_array[28].add_adjacentChannel(7);
        channel_array[28].add_adjacentChannel(29);
        
        channel_array[29].add_adjacentChannel(7);
        channel_array[29].add_adjacentChannel(11);
        channel_array[29].add_adjacentChannel(28);
        channel_array[29].add_adjacentChannel(30);
        
        channel_array[30].add_adjacentChannel(11);
        channel_array[30].add_adjacentChannel(15);
        channel_array[30].add_adjacentChannel(29);
        
        
    }
    
    
    
}
