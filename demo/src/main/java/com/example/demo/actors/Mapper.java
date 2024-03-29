package com.example.demo.actors;
import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class Mapper extends UntypedActor {

    private final ActorRef[] reducers;

    public Mapper() {
        this.reducers = null;
    }

    public Mapper(ArrayList<ActorRef> reducers) {

        this.reducers = reducers.toArray(new ActorRef[reducers.size()]);
    }

    @Override
    public void onReceive(Object message) {
//à continuer
}
}
 