package userInterface;

import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import java.util.Scanner;
import static utils.Statics.*;

public class Agents {
    private static final int LOGIC_MIX = -1;
    private Runtime myRuntime;
    private ContainerController mainContainer;
    
    public Agents(int type){
        
        Scanner sc = new Scanner(System.in);
        
        // get a JADE runtime
        myRuntime = Runtime.instance();
        // create a default profile
        Profile myProfile = new ProfileImpl();
        // create the Main-container
        mainContainer = myRuntime.createMainContainer(myProfile);
        
        //To create and start an agent:
        
        // create agent
        try {
            //RMA Agent
            AgentController rma =
                    mainContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
            // start the agent
            rma.start();
            
        } catch (jade.wrapper.StaleProxyException e) {
            System.err.println("Error launching agent...");
        }
        
        writeToFile("");
        
        switch(type){
            case 1 :
                threePlayers(sc, LOGIC_MIX);
                break;
            case 2:
                fivePlayers(sc, LOGIC_MIX);
                break;
            case 3:
                threePlayers(sc, LOGIC_RANDOM);
                break;
            case 4:
                threePlayers(sc, LOGIC_SPENDER);
                break;
            case 5:
                threePlayers(sc, LOGIC_SAVER);
                break;
            case 6:
                fivePlayers(sc, LOGIC_RANDOM);
                break;
            case 7:
                fivePlayers(sc, LOGIC_SPENDER);
                break;
            case 8:
                fivePlayers(sc, LOGIC_SAVER);
                break;
                
        }
    }
    
    private void threePlayers(Scanner sc, int logic) {
        String logic1, logic2, logic3;
        if(logic == LOGIC_MIX){
            logic1 = "0";
            logic2 = "1";
            logic3 = "2";
        }else{
            logic1 = Integer.toString(logic);
            logic2 = Integer.toString(logic);
            logic3 = Integer.toString(logic);
        }
        try{
            
            //Manager Agent
            Object[] args = {"3"};
            AgentController manager =
                    mainContainer.createNewAgent("manager", "agents.Manager", args);
            // start the agent
            manager.start();
            
            //Player Agents
            
            Object[] argsP1 = {"1", logic1};
            AgentController player1 =
                    mainContainer.createNewAgent("p1", "agents.ComputerAgent", argsP1);
            
            Object[] argsP2 = {"2", logic2};
            AgentController player2 =
                    mainContainer.createNewAgent("p2", "agents.ComputerAgent", argsP2);
            
            Object[] argsP3 = {"0", logic3};
            AgentController player3 =
                    mainContainer.createNewAgent("p3", "agents.ComputerAgent", argsP3);
            
            System.out.println();
            System.out.println("Start ??");
            sc.nextLine();
            
            // start the agents
            player1.start();
            player2.start();
            player3.start();
            
        } catch (jade.wrapper.StaleProxyException e) {
            System.err.println("Error launching agent...");
        }
    }
    
    private void fivePlayers(Scanner sc, int logic) {
        String logic1, logic2, logic3, logic4, logic5;
        if(logic == LOGIC_MIX){
            logic1 = "0";
            logic2 = "1";
            logic3 = "2";
            logic4 = "1";
            logic5 = "2";
        }else{
            logic1 = Integer.toString(logic);
            logic2 = Integer.toString(logic);
            logic3 = Integer.toString(logic);
            logic4 = Integer.toString(logic);
            logic5 = Integer.toString(logic);
        }
        
        try{
            
            //Manager Agent
            Object[] args = {"5"};
            AgentController manager =
                    mainContainer.createNewAgent("manager", "agents.Manager", args);
            // start the agent
            manager.start();
            
            //Player Agents
            
            Object[] argsP1 = {"1", logic1};
            AgentController player1 =
                    mainContainer.createNewAgent("p1", "agents.ComputerAgent", argsP1);
            
            Object[] argsP2 = {"2", logic2};
            AgentController player2 =
                    mainContainer.createNewAgent("p2", "agents.ComputerAgent", argsP2);
            
            Object[] argsP3 = {"0", logic3};
            AgentController player3 =
                    mainContainer.createNewAgent("p3", "agents.ComputerAgent", argsP3);
            
            Object[] argsP4 = {"0", logic4};
            AgentController player4 =
                    mainContainer.createNewAgent("p4", "agents.ComputerAgent", argsP4);
            
            Object[] argsP5 = {"0", logic5};
            AgentController player5 =
                    mainContainer.createNewAgent("p5", "agents.ComputerAgent", argsP5);
            
            System.out.println();
            System.out.println("Start ??");
            sc.nextLine();
            
            // start the agents
            player1.start();
            player2.start();
            player3.start();
            player4.start();
            player5.start();
            
        } catch (jade.wrapper.StaleProxyException e) {
            System.err.println("Error launching agent...");
        }
    }
}