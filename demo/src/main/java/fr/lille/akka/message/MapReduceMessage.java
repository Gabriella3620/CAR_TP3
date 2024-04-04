package fr.lille.akka.message;

import java.io.Serializable;
import java.util.Map;

import akka.actor.ActorRef;

public class MapReduceMessage implements Serializable{
    private static final long serialVersionUID = 11L;
	private final Map<String, ActorRef> reducers;
    private final String ligne;
    
    public MapReduceMessage(Map<String, ActorRef> reducers, String ligne) {
        this.reducers = reducers;
        this.ligne = ligne;
    }
    
    public Map<String, ActorRef> getReducers() {
        return reducers;
    }
    
    public String getLigne() {
        return ligne;
    }
}

