
package fr.lille.akka.akkaService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import fr.lille.akka.actors.MapperActor;
import fr.lille.akka.actors.ReducerActor;
import fr.lille.akka.message.CleanReducerMessage;
import fr.lille.akka.message.CountRequest;
import fr.lille.akka.message.MapReduceMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import jakarta.annotation.PreDestroy;
import scala.concurrent.duration.Duration;

@Service

public class AkkaService {

	private ActorRef mapper1, mapper2, mapper3, red1, red2;
	private ActorSystem aSystem, system2;
	private Map<String, ActorRef> reducers;
	private boolean initialized = false;
	private File fichier;
	private boolean dispatched;

	private BufferedReader bReader;
	private List<String> ALLOWED_EXTENSION;

	public void initialize() {
		aSystem = ActorSystem.create("aSystem", ConfigFactory.load("resources/application1.conf"));
		system2 = ActorSystem.create("system2", ConfigFactory.load("resources/application2.conf"));

		this.mapper1 = aSystem.actorOf(Props.create(MapperActor.class), "mapper1");
		this.mapper2 = system2.actorOf(Props.create(MapperActor.class), "mapper2");
		this.mapper3 = aSystem.actorOf(Props.create(MapperActor.class), "mapper3");

		ActorRef reducerConsonants, reducerVowels;
		reducerConsonants = system2.actorOf(Props.create(ReducerActor.class), "reducerConsonants");
		reducerVowels = aSystem.actorOf(Props.create(ReducerActor.class), "reducerVowels");

		this.reducers = new HashMap<>();
		this.reducers.put("vowels", reducerVowels);
		this.reducers.put("consonants", reducerConsonants);

		this.initialized = true;
		this.initialized = false;

		ALLOWED_EXTENSION = new ArrayList<>();
		ALLOWED_EXTENSION.add("txt");
		ALLOWED_EXTENSION.add("csv");

	}

	public int getOccurrenceCount(String word) throws Exception {
		if (word == null || word.isEmpty()) {
			return 0;
		}
		final Inbox inbox = Inbox.create(aSystem);

		inbox.send(this.mapper1, new CountRequest(word, inbox.getRef(), this.reducers));

		final Object response = inbox.receive(Duration.create(5, TimeUnit.SECONDS));

		this.aSystem.stop(inbox.getRef());

		if (response instanceof Integer) {
			return (Integer) response;
		} else {
			throw new RuntimeException("format no numerique");
		}

	}

	public void dispatchLinesToMapper() throws IOException {

		String text;
		MapReduceMessage mapReduceMessage;
		int i = 1;
		while ((text = bReader.readLine()) != null) {
			mapReduceMessage = new MapReduceMessage(reducers, text);

			switch (i) {
				case 1:
					this.mapper1.tell(mapReduceMessage, ActorRef.noSender());
					break;
				case 2:
					this.mapper2.tell(mapReduceMessage, ActorRef.noSender());
					break;
				case 3:
					this.mapper3.tell(mapReduceMessage, ActorRef.noSender());
					break;
				default:
					break;
			}

			i = i == 3 ? 1 : i + 1;
		}

		bReader.close();
		this.dispatched = true;

	}

	private void cleanReducers() throws TimeoutException {

		final Inbox inbox = Inbox.create(aSystem);
		CleanReducerMessage msg = new CleanReducerMessage(true);
		inbox.send(this.mapper1, msg);

		final Object response = inbox.receive(Duration.create(5, TimeUnit.SECONDS));

		this.aSystem.stop(inbox.getRef());

		reducers.get("vowels").tell(msg, ActorRef.noSender());
		reducers.get("consonants").tell(msg, ActorRef.noSender());
	}

	public void setReader(MultipartFile file) throws IOException {
		String fileName = file.getOriginalFilename();

		if (!file.isEmpty() && ALLOWED_EXTENSION.contains(getFileExtension(fileName))) {
			InputStream inputStream = file.getInputStream();
			bReader = new BufferedReader(new InputStreamReader(inputStream));
		}

	}

	private String getFileExtension(String fileName) {
		return fileName != null ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;
	}

	@PreDestroy
	// liberer les ressources avant la destruction du service
	public void clean() {
		if (this.aSystem == null || this.system2 == null) {
			return;
		}
		this.aSystem.stop(mapper1);
		this.aSystem.stop(mapper3);
		this.aSystem.stop(this.reducers.get("vowels"));

		this.system2.stop(mapper2);
		this.system2.stop(this.reducers.get("consonants"));

		this.aSystem.shutdown();
		this.system2.shutdown();
		this.dispatched = false;
		this.initialized = false;
	}

	public boolean isDispatched() {
		return dispatched;
	}

	public void setDispatched(boolean dispatched) {
		this.dispatched = dispatched;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

}