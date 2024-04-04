package fr.lille.akka.message;

import akka.actor.ActorRef;
import scala.Serializable;

import java.util.Map;

public class CountRequest implements Serializable {
    private static final long serialVersionUID = 22L;
    private final String word;
    private ActorRef inbox;
    private final Map<String, ActorRef> reducers;

    public CountRequest(Map<String, ActorRef> reducers, String word) {
        this.word = word.toLowerCase();
        this.reducers=reducers;
    }

    public String getWord() {
        return word;
    }

    public Map<String, ActorRef> getReducers() {
        return reducers;
    }

    public CountRequest(String word, ActorRef inbox, Map<String, ActorRef> reducers) {
        super();
        this.word = word;
        this.inbox = inbox;
        this.reducers = reducers;
    }

    public ActorRef getInbox() {
        return inbox;
    }

    public void setInbox(ActorRef inbox) {
        this.inbox = inbox;
    }


}
