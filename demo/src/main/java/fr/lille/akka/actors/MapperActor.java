package fr.lille.akka.actors;
import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import fr.lille.akka.message.CountRequest;
import fr.lille.akka.message.CountResponse;
import fr.lille.akka.message.MapReduceMessage;

public class MapperActor extends UntypedActor {

    final static String CONSONNES_FR = "bcdfghjklmnpqrstvwxyz";

    public MapperActor() {
        this.reducers = null;
    }

    private void sendMsg(String mot, ActorRef actorRef) {
		actorRef.tell(mot, getSelf());
	}
	private ActorRef partition(String mot, Map<String, ActorRef> reducerMap) {
		  mot = mot.toLowerCase();
		boolean commenceParConsonne = CONSONNES_FR.indexOf(Character.toLowerCase(mot.charAt(0))) != -1;
		ActorRef reducerSelected;
		if(commenceParConsonne) {
			reducerSelected= reducerMap.get("consonants");
			 
		}else {
			 reducerSelected= reducerMap.get("vowels");
		}
		return reducerSelected;
	}

    @Override
	public void onReceive(Object line) throws Exception {
		 
		if (line instanceof MapReduceMessage) {
			MapReduceMessage mpMessage = ((MapReduceMessage) line);
			String[]  mots =    mpMessage.getLigne().split(" ");
			for (String mot : mots) {
				  
				ActorRef actor  =this.partition(mot.toLowerCase(), mpMessage.getReducers());
				this.sendMsg(mot.toLowerCase(), actor);
			}
		}else if(line instanceof CountRequest) {
			CountRequest cRequest = (CountRequest) line;
			//le reducer a contacter
			ActorRef actor = this.partition(cRequest.getWord(), cRequest.getReducers());
			actor.tell( cRequest, getSelf());
		}else if (line instanceof CountResponse) {
			CountResponse cResponse = (CountResponse) line;
		}
		
	}


}
 