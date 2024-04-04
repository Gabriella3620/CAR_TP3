
package fr.lille.akka.akkaService;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import fr.lille.akka.actor.MapperActor;
import fr.lille.akka.actor.ReducerActor;
import fr.lille.akka.message.CleanReducerMessage;
import fr.lille.akka.message.CountRequest;
import fr.lille.akka.message.MapReduceMessage;

@Service

public class AkkaService {
	
  
    private ActorRef mapper1, mapper2, mapper3, red1, red2;
	private ActorSystem aSystem,system2;
	private Map<String,ActorRef> reducers;
	private boolean initialized=false;
	private File fichier;
	private boolean dispatched;
	
	private BufferedReader bReader;
	private List<String> ALLOWED_EXTENSION ;
    

    
    public void initialize() {
        aSystem = ActorSystem.create("aSystem", ConfigFactory.load("resources/application1.conf"));
		system2 = ActorSystem.create("system2",  ConfigFactory.load("resources/application2.conf"));
		
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

    private void cleanReducers() throws TimeoutException {
		
		final Inbox inbox = Inbox.create(aSystem);
		CleanReducerMessage msg = new CleanReducerMessage(true);
        inbox.send(this.mapper1,  msg );
        
        final Object response = inbox.receive(Duration.create(5, TimeUnit.SECONDS));
        
        this.aSystem.stop(inbox.getRef());
		
		reducers.get("vowels").tell(msg, ActorRef.noSender());
		reducers.get("consonants").tell( msg, ActorRef.noSender());
	}

    public void setReader(MultipartFile file) throws IOException {
		String fileName = file.getOriginalFilename();
		 
		  if( !file.isEmpty() && ALLOWED_EXTENSION.contains( getFileExtension(fileName)) ) {
			  InputStream inputStream = file.getInputStream();
			  bReader =  new BufferedReader(new InputStreamReader(inputStream));
		  }
		 
	}
	private String getFileExtension( String fileName) {
	    return fileName != null ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;
	}
    @PreDestroy
    //liberer les ressources avant la destruction du service
	public void clean() {
		if(this.aSystem==null || this.system2==null) {return;}
		this.aSystem.stop(mapper1);
		this.aSystem.stop(mapper3);
		this.aSystem.stop(this.reducers.get("vowels"));
		
		
		this.system2.stop(mapper2);
		this.system2.stop(this.reducers.get("consonants"));
		
		
		this.aSystem.shutdown();
		this.system2.shutdown();
		this.dispatched=false;
		this.initialized=false;
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