package fr.lille.akka.actors;

import java.util.HashMap;
import java.util.Map;

import akka.actor.UntypedActor;
import fr.lille.akka.message.CleanReducerMessage;
import fr.lille.akka.message.CountRequest;

public class ReducerActor extends UntypedActor {

	private Map<String, Integer> occurences = new HashMap<>();

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof String) {
			String mot = ((String) message).toLowerCase();
			int nbOccurences = occurences.getOrDefault(mot, 0);
			occurences.put(mot, nbOccurences + 1);
		} else if (message instanceof CountRequest) {
			String word = ((CountRequest) message).getWord().toLowerCase();
			int count = 0;
			if (occurences.containsKey(word)) {
				count = occurences.get(word);
			}
			((CountRequest) message).getInbox().tell(count, getSelf());
		} else if (message instanceof CleanReducerMessage) {
			this.occurences.clear();
		}

	}

}
