package agents;

import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.lang.acl.UnreadableException;
import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.Game;
import logic.Player;
import utils.Pair;
import static utils.Statics.*;
import utils.TilePlacement;

// classe do agente
public class Manager extends Agent {
    
    protected int numberPlayers, numberOfPlays;
    protected Timestamp time = new Timestamp(-1);
    protected long initTime, endTime;
    
    // classe do behaviour
    class ManagerBehaviour extends SimpleBehaviour {
        private final Map<String, Color> player_color = new HashMap<>();
        private boolean gameStart = false, endGame = false, tilePlacement = false;
        private final Game g = new Game();
        private int gameLoop = GAME_START, playerOrder = PLAYER_1, lastIndex, firstIndex;
        Pair<Color, Integer>[] newOrder;
        
        // construtor do behaviour
        public ManagerBehaviour(Agent a) {
            super(a);
        }
        
        // método action
        @Override
        public void action() {
            String player, content;
            ACLMessage msg = blockingReceive();
            player = msg.getSender().getLocalName();
            content = msg.getContent();
            
            if (msg.getPerformative() == ACLMessage.PROPOSE) {
                ACL_Propose(content, player);
            }
            
            if(gameStart && gameLoop == GAME_START){
                startGame();
            }else if(gameLoop == GAME_TILES_BIDDING){
                ACL_Inform_tilesBidding(msg, player);
            }else if(gameLoop == GAME_TILES_PLACEMENT){
                ACL_Inform_tilesPlacement(msg, player);
            }else if(gameLoop == GAME_WATER_BIDDING){
                ACL_Inform_waterBidding(msg, player);
            }else if(gameLoop == GAME_WATER_PLACEMENT){
                ACL_Inform_waterPlacement(msg, player);
            }
            
        }
        
        private void startGame() {
            
            initTime = time.getTime();
            
            g.setNumberOfPlayers(numberPlayers);
            gameLoop = GAME_TILES_BIDDING;
            startNewRound();
            sendRequestTileLicitation(playerOrder);
        }
        
        private void startNewRound(){
            numberOfPlays++;
            
            g.newTilesForRound();
            sendBoardAndPlayers();
            
            lastIndex = g.getOrder().size();
            firstIndex = 0;
            newOrder = new Pair[lastIndex];
            
            for (int i = 0; i < lastIndex; ++i) {
                newOrder[i] = new Pair();
            }
            
            sendTilesForRound();
        }
        
        private void sendTilesForRound() {
            for ( String player : player_color.keySet() )
                sendMessage(ACLMessage.INFORM, g.getTilesForRound(), player);
        }
        
        private void sendBoardAndPlayers(){
            for ( String player : player_color.keySet() ) {
                sendMessage(ACLMessage.INFORM, g.getBoard(), player);
                
                sendMessage(ACLMessage.INFORM, g.getOrder(), player);
                sendMessage(ACLMessage.INFORM, g.getPlayersVector(), player);
            }
        }
        
        private void sendAllWaterLicitations() {
            for ( String player : player_color.keySet() ) {
                sendMessage(ACLMessage.INFORM, g.getWaterLicitationForRound(), player);
            }
        }
        
        private void sendRequestTileLicitation(int playerID){
            
            Color c;
            
            for (String player : player_color.keySet() ) {
                c = g.getOrder().get(playerID);
                if(player_color.get(player).equals(c)){
                    sendMessage(ACLMessage.REQUEST, REQUEST_TILE_LICITATION, player);
                }
            }
            
        }
        
        private void sendRequestTilePlacement(int playerID) {
            Color c;
            
            for (String player : player_color.keySet() ) {
                c = g.getOrder().get(playerID);
                if(player_color.get(player).equals(c)){
                    sendMessage(ACLMessage.REQUEST, REQUEST_TILE_PLACEMENT, player);
                }
            }
        }
        
        private void sendRequestWaterLicitation(int playerID) {
            Color c;
            
            for (String player : player_color.keySet() ) {
                c = g.getOrder().get(playerID);
                if(player_color.get(player).equals(c)){
                    sendMessage(ACLMessage.REQUEST, REQUEST_WATER_LICITATION, player);
                }
            }
        }
        
        private void sendRequestWaterPlacement(int playerID) {
            Color c;
            
            for (String player : player_color.keySet() ) {
                c = g.getOrder().get(playerID);
                if(player_color.get(player).equals(c)){
                    sendMessage(ACLMessage.REQUEST, REQUEST_WATER_PLACEMENT, player);
                }
            }
        }
        
        //ACL Messages
        private void ACL_Inform_tilesBidding(ACLMessage msg, String player) {
            if (msg.getPerformative() == ACLMessage.INFORM) {
                try {
                    Serializable content_obj = msg.getContentObject();
                    
                    if(content_obj instanceof Integer){
                        int money = (Integer) content_obj;
                        Color c = g.getOrder().get(playerOrder);
                        if(player_color.get(player).equals(c)){
                            Player p = g.getPlayer(c);
                            if(p.Pay(money)){
                                sendMessage(ACLMessage.CONFIRM, CONFIRM_TILE_LICITAION, player);
                                playerOrder++;
                                
                                if (money == 0) {
                                    newOrder[--lastIndex].setFirst(p.getColor());
                                    newOrder[lastIndex].setSecond(0);
                                } else {
                                    newOrder[firstIndex].setFirst(p.getColor());
                                    newOrder[firstIndex++].setSecond(money);
                                }
                                
                                if(playerOrder < numberPlayers)
                                    sendRequestTileLicitation(playerOrder);
                                else{
                                    playerOrder = PLAYER_1;
                                    gameLoop = GAME_TILES_PLACEMENT;
                                    
                                    Vector<Color> order = g.getOrder();
                                    sort(newOrder);
                                    
                                    for (int i = 0; i < order.size(); ++i) {
                                        order.set(i, newOrder[i].getFirst());
                                    }
                                    
                                    g.setOrder(order);
                                    
                                    sendBoardAndPlayers();
                                    
                                    sendRequestTilePlacement(playerOrder);
                                    
                                    tilePlacement = true;
                                }
                            }else{
                                sendMessage(ACLMessage.CANCEL, REFUSE_TILE_LICITATION, player);
                            }
                        }else{
                            sendMessage(ACLMessage.CANCEL, REFUSE_TILE_LICITATION, player);
                        }
                    }
                    
                } catch (UnreadableException ex) {
                    Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        private void ACL_Inform_tilesPlacement(ACLMessage msg, String player) {
            if (msg.getPerformative() == ACLMessage.INFORM) {
                try {
                    Serializable content_obj = msg.getContentObject();
                    
                    if(content_obj instanceof TilePlacement){
                        TilePlacement tp = (TilePlacement) content_obj;
                        Color c = g.getOrder().get(playerOrder);
                        if(player_color.get(player).equals(c)){
                            
                            if(g.getTilesForRound().contains(tp.getTile())){
                                if(g.plant(tp.getTile(), c, (Integer)tp.getCoord().getSecond(), (Integer)tp.getCoord().getFirst())){
                                    
                                    g.removeTileFromRound(tp.getTile());
                                    sendMessage(ACLMessage.CONFIRM, CONFIRM_TILE_PLACEMENT, player);
                                    
                                    playerOrder++;
                                    
                                    if(tilePlacement){
                                        
                                        sendBoardAndPlayers();
                                        sendTilesForRound();
                                        
                                        if(playerOrder < numberPlayers)
                                            sendRequestTilePlacement(playerOrder);
                                        else{
                                            playerOrder = PLAYER_1;
                                            
                                            sendRequestTilePlacement(playerOrder);
                                            
                                            tilePlacement = false;
                                        }
                                    }else{
                                        playerOrder = PLAYER_1;
                                        gameLoop = GAME_WATER_BIDDING;
                                        
                                        sendBoardAndPlayers();
                                        
                                        sendRequestWaterLicitation(playerOrder);
                                    }
                                    
                                }else{
                                    sendMessage(ACLMessage.CANCEL, REFUSE_TILE_PLACEMENT, player);
                                }
                            }else{
                                sendMessage(ACLMessage.CANCEL, REFUSE_TILE_PLACEMENT, player);
                            }
                            
                        }else{
                            sendMessage(ACLMessage.CANCEL, REFUSE_TILE_PLACEMENT, player);
                        }
                    }
                    
                } catch (UnreadableException ex) {
                    Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        private void ACL_Inform_waterBidding(ACLMessage msg, String player) {
            if (msg.getPerformative() == ACLMessage.INFORM) {
                try {
                    Serializable content_obj = msg.getContentObject();
                    
                    if(content_obj instanceof Pair){
                        Color c = g.getOrder().get(playerOrder);
                        if(player_color.get(player).equals(c)){
                            Pair waterL = (Pair) content_obj;
                            int waterChoice = (Integer) waterL.getFirst(), money = (Integer) waterL.getSecond();
                            
                            Player p = g.getPlayer(c);
                            Vector<Integer> waterPossibelPaths = g.getWaterPossiblePaths();
                            
                            if(waterChoice < waterPossibelPaths.size() && waterChoice >= 0 && money <= p.getEscudos()){
                                
                                sendMessage(ACLMessage.CONFIRM, CONFIRM_WATER_LICITAION, player);
                                playerOrder++;
                                
                                g.addWaterLicitationForRound(waterL);
                                
                                if(playerOrder < (numberPlayers - 1))
                                    sendRequestWaterLicitation(playerOrder);
                                else{
                                    gameLoop = GAME_WATER_PLACEMENT;
                                    sendAllWaterLicitations();
                                    sendRequestWaterPlacement(playerOrder);
                                }
                            }else{
                                sendMessage(ACLMessage.CANCEL, REFUSE_WATER_LICITATION, player);
                            }
                        }else{
                            sendMessage(ACLMessage.CANCEL, REFUSE_WATER_LICITATION, player);
                        }
                    }else{
                        sendMessage(ACLMessage.CANCEL, REFUSE_WATER_LICITATION, player);
                    }
                    
                } catch (UnreadableException ex) {
                    Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        private void ACL_Inform_waterPlacement(ACLMessage msg, String player) {
            if (msg.getPerformative() == ACLMessage.INFORM) {
                try {
                    Serializable content_obj = msg.getContentObject();
                    
                    if(content_obj instanceof Integer){
                        Color c = g.getOrder().get(playerOrder);
                        if(player_color.get(player).equals(c)){
                            int waterChoice = (Integer) content_obj;
                            
                            if (waterChoice >= 0 && waterChoice < g.getWaterPossiblePaths().size()) {
                                                                
                                g.placeWaterChannel(waterChoice);
                                
                                sendBoardAndPlayers();
                                
                                hasGameEnded();
                                
                            } else {
                                sendMessage(ACLMessage.CANCEL, REFUSE_WATER_PLACEMENT, player);
                            }
                        }else{
                            sendMessage(ACLMessage.CANCEL, REFUSE_WATER_PLACEMENT, player);
                        }
                    }else{
                        sendMessage(ACLMessage.CANCEL, REFUSE_WATER_PLACEMENT, player);
                    }
                    
                } catch (UnreadableException ex) {
                    Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        private void ACL_Propose(String content, String player) {
            if(content.equals(ENTER_GAME) && !gameStart){
                if(player_color.size() < numberPlayers){
                    player_color.put(player, PlayerColor.get(player_color.size()));
                    String color_name = ColorName.get(player_color.get(player).toString());
                    
                    System.out.println("Jogador " + player + " com a cor : " + color_name);
                    
                    sendMessage(ACLMessage.ACCEPT_PROPOSAL, ("Color " + color_name), player);
                    
                    if(player_color.size() == numberPlayers){
                        gameStart = true;
                    }
                }else{
                    System.out.println("Não há vagas!");
                    sendMessage(ACLMessage.REJECT_PROPOSAL, EXIT_GAME, player);
                }
            }
        }
        
        // método done
        @Override
        public boolean done() {
            return endGame;
        }
        
        private void finishGame(){
            for ( String player : player_color.keySet() ) {
                sendMessage(ACLMessage.REJECT_PROPOSAL, EXIT_GAME, player);
            }
            endGame = true;
        }
        
        private void hasGameEnded() {
            
            g.checkTilesIrrigation();
            g.giveMoneyBonus();
            g.clearWaterLicitationForRound();
            
            if(g.finish()){
                g.checkTilesIrrigation();
                g.calculateFinalResults();
                sendBoardAndPlayers();
                finishGame();
                gameLoop = GAME_END;
                endTime = time.getTime();
                showGameInfo();
            }else{
                playerOrder = PLAYER_1;
                gameLoop = GAME_TILES_BIDDING;
                startNewRound();
                sendRequestTileLicitation(playerOrder);
            }
        }
        
        private void showGameInfo() {
            System.out.println();
            System.out.println();
            System.out.println("Jogadas  ==>  " + numberOfPlays);
            System.out.println();
            System.out.println();
            
            long diff = endTime - initTime;
            int seconds = (int) (diff / 1000) % 60 ;
            int minutes = (int) ((diff / (1000*60)) % 60);
            int hours   = (int) ((diff / (1000*60*60)) % 24);
            
            System.out.println("Diff : " + diff);
            
            if(hours > 0)
                System.out.println("Horas : " + hours);
            if(minutes > 0)
                System.out.println("Minutos : " + minutes);
            if(seconds > 0)
                System.out.println("Segundos : " + seconds);
        }
        
    }   // fim da classe ManagerBehaviour
    
    // método setup
    @Override
    protected void setup() {
        
        // obtém argumentos
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            numberPlayers = Integer.parseInt((String) args[0]);
            if(numberPlayers < 3){
                System.out.println("O número minimo de jogares é 3");
                numberPlayers = 3;
            }else if(numberPlayers > 5){
                System.out.println("O número máximo de jogares é 5");
                numberPlayers = 5;
            }else{
                System.out.println("O número de jogares será : " + numberPlayers);
            }
            
        } else {
            System.out.println("O número de jogares será 3");
            numberPlayers = 3;
        }
        
        //preenche maps e vectores static
        fillStaticVars();
        
        jade.wrapper.AgentContainer ac = getContainerController();
        
        // regista agente no DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getName());
        sd.setType("Manager");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            System.err.println("Error register in DF");
        }
        
        // cria behaviour
        ManagerBehaviour b = new ManagerBehaviour(this);
        addBehaviour(b);
        
    }   // fim do metodo setup
    
    // método takeDown
    @Override
    protected void takeDown() {
        // retira registo no DF
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
        }
    }
    
    // envia mensagem 'content' a todos os agentes 'receiver'
    protected void sendMessage(int ACLtype, String content, String receiver){
        
        // pesquisa DF por agentes
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd1 = new ServiceDescription();
        sd1.setType("Player");
        sd1.setName(receiver);
        template.addServices(sd1);
        try {
            DFAgentDescription[] result = DFService.search(this, template);
            
            ACLMessage msg = new ACLMessage(ACLtype);
            
            for (int i = 0; i < result.length; ++i) {
                msg.addReceiver(result[i].getName());
            }
            
            msg.setContent(content);
            send(msg);
        } catch (FIPAException e) {
            System.err.println("Error Sending message");
        }
        
    }
    
    // envia mensagem 'content' e um objecto 'obj' serializado a todos os agentes 'receiver'
    protected void sendMessage(int ACLtype, Serializable content, String receiver){
        
        // pesquisa DF por agentes
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd1 = new ServiceDescription();
        sd1.setType("Player");
        sd1.setName(receiver);
        template.addServices(sd1);
        try {
            DFAgentDescription[] result = DFService.search(this, template);
            
            ACLMessage msg = new ACLMessage(ACLtype);
            
            for (int i = 0; i < result.length; ++i) {
                msg.addReceiver(result[i].getName());
            }
            
            msg.setContentObject(content);
            
            send(msg);
            
        } catch (FIPAException e) {
            System.err.println("Error sending msg to agent " + receiver + "...");
        } catch (IOException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}   // fim da classe Manager

