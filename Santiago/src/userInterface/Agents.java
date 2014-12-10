package userInterface;

import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import java.util.Scanner;

public class Agents {
    
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
        
        if(type == 1){
            
            try{
                
                //Manager Agent
                Object[] args = {"3"};
                AgentController manager =
                        mainContainer.createNewAgent("manager", "agents.Manager", args);
                // start the agent
                manager.start();
                
                //Player Agents
                
                //Agent Random
                Object[] argsP1 = {"1", "0"};
                AgentController player1 =
                        mainContainer.createNewAgent("p1", "agents.ComputerAgent", argsP1);

                //Agent Spender
                Object[] argsP2 = {"0", "1"};
                AgentController player2 =
                        mainContainer.createNewAgent("p2", "agents.ComputerAgent", argsP2);
                
                //Agent Saver
                Object[] argsP3 = {"0", "2"};
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
        }else if(type == 2){
            
            try{
                
                //Manager Agent
                Object[] args = {"5"};
                AgentController manager =
                        mainContainer.createNewAgent("manager", "agents.Manager", args);
                // start the agent
                manager.start();
                
                //Player Agents
                
                //Agent Random
                Object[] argsP1 = {"1", "0"};
                AgentController player1 =
                        mainContainer.createNewAgent("p1", "agents.ComputerAgent", argsP1);
                
                //Agent Spender
                Object[] argsP2 = {"0", "1"};
                AgentController player2 =
                        mainContainer.createNewAgent("p2", "agents.ComputerAgent", argsP2);
                
                //Agent Saver
                Object[] argsP3 = {"0", "1"};
                AgentController player3 =
                        mainContainer.createNewAgent("p3", "agents.ComputerAgent", argsP3);
                
                //Agent Spender
                Object[] argsP4 = {"0", "2"};
                AgentController player4 =
                        mainContainer.createNewAgent("p4", "agents.ComputerAgent", argsP4);
                
                //Agent Random
                Object[] argsP5 = {"0", "2"};
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
    
}
