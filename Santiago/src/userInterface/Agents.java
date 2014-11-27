package userInterface;

import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;

public class Agents {
    
    private Runtime myRuntime;
    private ContainerController mainContainer;
    
    public Agents(){
        
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
            
            //Manager Agent
            Object[] args = {"3"};
            AgentController manager =
                    mainContainer.createNewAgent("manager", "agents.Manager", args);
            // start the agent
            manager.start();
            
            Object[] argsP = {"1"};
            //Player Agents
            AgentController player1 =
                    mainContainer.createNewAgent("p1", "agents.ComputerAgent", argsP);
            // start the agent
            player1.start();
            
            AgentController player2 =
                    mainContainer.createNewAgent("p2", "agents.ComputerAgent", null);
            // start the agent
            player2.start();
            
            AgentController player3 =
                    mainContainer.createNewAgent("p3", "agents.ComputerAgent", null);
            // start the agent
            player3.start();
        } catch (jade.wrapper.StaleProxyException e) {
            System.err.println("Error launching agent...");
        }
        
    }
   
}
