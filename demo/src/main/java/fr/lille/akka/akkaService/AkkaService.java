
package com.example.demo.akkaService;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;


@Service

public class AkkaService {
	
    private static final ActorSystem system = ActorSystem.create("MapReduceSystem");

    private static final ActorRef[] mappers = new ActorRef[3];
    private static final ActorRef[] reducers = new ActorRef[2];
    private static List<ActorRef>reducerTabs;
    
    
    
    public static void initialize() {
    	reducerTabs=new ArrayList <> ();
        for (int i = 0; i < 2; i++) {
            reducers[i] = system.actorOf(Props.create(Reducer.class), "reducer" + i);
            reducerTabs.add(reducers[i]);
        }
        
        for (int i = 0; i < 3; i++) {
            mappers[i] = system.actorOf(Props.create(Mapper.class,reducerTabs), "mapper" + i);
        }

    }
    //à continuer
}