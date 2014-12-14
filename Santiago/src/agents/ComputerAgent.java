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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.Board;
import logic.Player;
import logic.Tile;
import static utils.Statics.*;
import static utils.ConsolePrints.*;
import static logic.Agent.*;
import userInterface.GUI;
import utils.GuiUtils;
import static utils.GuiUtils.*;
import utils.Pair;
import utils.TilePlacement;

public class ComputerAgent extends Agent {
    
    private int interface_type, playerLogic, licitation_type;
    protected Vector<Color> order = new Vector<>();
    
    class ComputerAgentBehaviour extends SimpleBehaviour {
        
        private Color myColor = null;
        private boolean endGame = false;
        private Player myInfo;
        private int money, backupMoney;
        
        private Vector<Player> allPlayers = new Vector<>();
        //private Board gameBoard = new Board();
        //private Vector<Tile> chooseTiles = new Vector<>();
        private Vector<Pair> waterLicitations = new Vector<>();
        // construtor do behaviour
        public ComputerAgentBehaviour(Agent a) {
            super(a);
        }
        
        // método action
        @Override
        public void action() {
            String manager, content;
            
            ACLMessage msg = blockingReceive();
            manager = msg.getSender().getLocalName();
            content = msg.getContent();
            
            if(manager.equals("manager")){
                switch (msg.getPerformative()){
                    case ACLMessage.ACCEPT_PROPOSAL:
                        ACL_AcceptProposal(content);
                        break;
                    case ACLMessage.INFORM:
                        ACL_Inform(msg);
                        break;
                    case ACLMessage.REJECT_PROPOSAL:
                        ACL_RejectProposal(content);
                        break;
                    case ACLMessage.REQUEST:
                        ACL_Request(content);
                        break;
                    case ACLMessage.CONFIRM:
                        ACL_Confirm(content);
                        break;
                    case ACLMessage.CANCEL:
                        ACL_Cancel(content);
                        break;   
                }
            }
        }
        
        private void saveMyInfo(Serializable obj) {
            allPlayers = (Vector<Player>) obj;
            GAME_PLAYERS = allPlayers;
            for(Player p : allPlayers){
                if(p.getColor().equals(myColor)){
                    if(myInfo != null)
                        backupMoney = myInfo.getEscudos();
                    myInfo = p;
                }
            }
        }
        
        private void sendTileLicitation() {
            money = generateTileLicitation(myInfo, playerLogic, allPlayers);
            sendMessage(ACLMessage.INFORM, money);
        }
        
        private void sendTilePlacement() {
            TilePlacement tp = generateTilePlacement(myInfo, playerLogic, GAME_BOARD, ROUND_TILES);
            sendMessage(ACLMessage.INFORM, tp);
        }
        
        private void sendWaterLicitation() {
            Pair waterL = generateWaterLicitation(myInfo, playerLogic, allPlayers, GAME_BOARD.getWaterPossiblePaths(), GAME_BOARD);
            
            sendMessage(ACLMessage.INFORM, waterL);
        }
        
        private void sendWaterPlacement() {
            int choice = generateWaterPlacement(myInfo, playerLogic, allPlayers, GAME_BOARD.getWaterPossiblePaths(), waterLicitations, GAME_BOARD);
            
            sendMessage(ACLMessage.INFORM, choice);
        }
        
        //ACL Messages
        private void ACL_RejectProposal(String content) {
            if(content.equals(EXIT_GAME)){
                String infoBefore = "*Color: " + myInfo.getName() + "\tLogic: " + playerLogic + "\tMoney: " + backupMoney;
                String infoAfter = "Color: " + myInfo.getName() + "\tLogic: " + playerLogic + "\tMoney: " + myInfo.getEscudos();
                writeToFile(infoBefore);
                writeToFile(infoAfter);
                endGame = true;
            }
        }
        
        private void ACL_Inform(ACLMessage msg) {
            Serializable content_obj;
            try {
                content_obj = msg.getContentObject();
                if(content_obj instanceof Board){
                    saveBoard(content_obj);
                    print(BOARD, content_obj);
                }else if(content_obj instanceof Vector){
                    Vector aux = (Vector) content_obj;
                    if(aux.get(0) instanceof Tile){
                        print(TILES, content_obj);
                        saveTiles(content_obj);
                    }else if(aux.get(0) instanceof Integer){
                        print(CHANNELS, content_obj);
                    }else if(aux.get(0) instanceof Color){
                        order = (Vector<Color>) content_obj;
                        GAME_ORDER = (Vector<Color>) content_obj;
                    }else if(aux.get(0) instanceof Player){
                        print(PLAYERS, content_obj);
                        saveMyInfo(content_obj);
                    }else if(aux.get(0) instanceof Pair){
                        print(WATER_LICITATIONS, content_obj);
                    }
                }
            } catch (UnreadableException ex) {
                Logger.getLogger(ComputerAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        private void ACL_AcceptProposal(String content) {
            String arr[] = content.split(" ", 2);
            String firstWord = arr[0];
            
            if(firstWord.equals("Color") && myColor == null){
                
                switch(content){
                    case COLOR_BLUE:
                        myColor = PlayerColor.get(0);
                        break;
                    case COLOR_RED:
                        myColor = PlayerColor.get(1);
                        break;
                    case COLOR_GREEN:
                        myColor = PlayerColor.get(2);
                        break;
                    case COLOR_YELLOW:
                        myColor = PlayerColor.get(3);
                        break;
                    case COLOR_PURPLE:
                        myColor = PlayerColor.get(4);
                        break;
                }
            }
        }
        
        private void ACL_Request(String content) {
            
            //Pause for TIME_TO_WAIT/1000 seconds
            try {
                Thread.sleep(TIME_TO_WAIT);
            } catch (InterruptedException ex) {
                Logger.getLogger(ComputerAgent.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            switch (content) {
                case REQUEST_TILE_LICITATION:
                    sendTileLicitation();
                    break;
                case REQUEST_TILE_PLACEMENT:
                    sendTilePlacement();
                    break;
                case REQUEST_WATER_LICITATION:
                    sendWaterLicitation();
                    break;
                case REQUEST_WATER_PLACEMENT:
                    sendWaterPlacement();
                    break;
            }
        }
        
        private void ACL_Confirm(String content) {
            switch (content) {
                case CONFIRM_TILE_LICITAION:
                    myInfo.Pay(money);
                    money = 0;
                    licitation_type = (licitation_type + 1) % 2;
                    break;
                case CONFIRM_TILE_PLACEMENT:
                    if(ROUND_TILES.size() == 1)
                        ROUND_TILES.clear();
                    break;
                case CONFIRM_WATER_LICITAION:
                    break;
                case CONFIRM_WATER_PLACEMENT:
                    break;
            }
        }
        
        private void ACL_Cancel(String content) {
            switch (content) {
                case REFUSE_TILE_LICITATION:
                    sendTileLicitation();
                    break;
                case REFUSE_TILE_PLACEMENT:
                    sendTilePlacement();
                    break;
                case REFUSE_WATER_LICITATION:
                    sendWaterLicitation();
                    break;
                case REFUSE_WATER_PLACEMENT:
                    sendWaterPlacement();
                    break;
            }
        }
        
        @Override
        public boolean done() {
            return endGame;
        }
        
        private void saveBoard(Serializable content_obj) {
            GAME_BOARD = (Board)content_obj;
        }
        
        private void saveTiles(Serializable content_obj) {
            ROUND_TILES = (Vector<Tile>) content_obj;
        }
        
        private void print(String type, Serializable obj){
            
            if(interface_type != NO_INTERFACE){
                
                switch (type){
                    case BOARD:
                        if(interface_type == CONSOLE_INTERFACE){
                            Board b = (Board) obj;
                            printBoard(b);
                        }else{
                            
                        }
                        break;
                    case TILES:
                        if(interface_type == CONSOLE_INTERFACE){
                            Vector<Tile> tiles = (Vector<Tile>) obj;
                            printTilesForRound(tiles);
                        }else{
                            
                        }
                        break;
                    case PLAYERS:
                        if(interface_type == CONSOLE_INTERFACE){
                            Vector<Player> p = (Vector<Player>) obj;
                            printPlayerInfo(order, p);
                        }else{
                            
                        }
                        break;
                        
                    case CHANNELS:
                        if(interface_type == CONSOLE_INTERFACE){
                            Vector<Integer> channles = (Vector<Integer>) obj;
                            printWaterPossibelPaths(channles);
                        }else{
                            
                        }
                        break;
                        
                    case WATER_LICITATIONS:
                        if(interface_type == CONSOLE_INTERFACE){
                            waterLicitations = (Vector<Pair>) obj;
                            printWaterLicitations(order, allPlayers, waterLicitations, GAME_BOARD.getWaterPossiblePaths());
                        }else{
                            
                        }
                        break;
                }
                
            }
        }
        
    }
    
    @Override
    protected void setup() {
        
        // obtém argumentos
        Object[] args = getArguments();
        if(args != null){
            //Interface
            if (args.length > 0) {
                interface_type = Integer.parseInt((String) args[0]);
                
                if(interface_type != NO_INTERFACE && interface_type != CONSOLE_INTERFACE && interface_type != GRAPHIC_INTERFACE ){
                    interface_type = NO_INTERFACE;
                }
            } else {
                interface_type = NO_INTERFACE;
                
            }
            
            //Player Logic
            if (args.length > 1) {
                playerLogic = Integer.parseInt((String) args[1]);
                
                if(playerLogic != LOGIC_RANDOM && playerLogic != LOGIC_SPENDER && playerLogic != LOGIC_SAVER && playerLogic != LOGIC_MIXED ){
                    playerLogic = LOGIC_RANDOM;
                }
            } else {
                playerLogic = LOGIC_RANDOM;
                
            }
            
            if(interface_type == GRAPHIC_INTERFACE){
                GuiUtils gUtils = new GuiUtils();
                gUtils.initVars();
                GUI.start(null);
            }
            
            
            
            //Is Human
            if (args.length > 2) {
                //Se é humano ou nao
            } else {
                
            }
        }
        
        licitation_type = LICITATION_TILES;
        
        if(interface_type == GRAPHIC_INTERFACE){
            GuiUtils gUtils = new GuiUtils();
            gUtils.initVars();
            GUI.start(null);
        }
        
        
        
        // regista agente no DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("Player");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            System.err.println("Error register in DF");
        }
        
        // cria behaviour
        ComputerAgentBehaviour b = new ComputerAgentBehaviour(this);
        addBehaviour(b);
        
        sendMessage(ACLMessage.PROPOSE, ENTER_GAME);
        
    } 
    
    @Override
    protected void takeDown() {
        // retira registo no DF
        try {
            DFService.deregister(this);
            System.err.print(this);
        } catch (FIPAException e) {
            System.err.println("Error");
        }
    }
    
    // envia mensagem String 'content' a todos os agentes 'receiver'
    protected void sendMessage(int ACLtype, String content){
        
        // pesquisa DF por agentes
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd1 = new ServiceDescription();
        sd1.setType("Manager");
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
    
    // envia mensagem Serializable 'content' a todos os agentes 'receiver'
    protected void sendMessage(int ACLtype, Serializable content){
        
        // pesquisa DF por agentes
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd1 = new ServiceDescription();
        sd1.setType("Manager");
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
            System.err.println("Error Sending message");
        } catch (IOException ex) {
            Logger.getLogger(ComputerAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}   // fim da classe Manager