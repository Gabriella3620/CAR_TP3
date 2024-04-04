package fr.lille.akka.controllers;

import akka.actor.ActorRef;
import akka.util.Timeout;

import akka.message.*;

import akka.actors.MapperActor;
import akka.actors.ReducerActor;
import akka.akkaService.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import scala.concurrent.duration.Duration;
import javax.servlet.http.HttpServletRequest;
import java.util.List;




@Controller
@RequestMapping("akka")
public class AkkaController {
    List<String> save;
    @Autowired
    private AkkaService akkaService;

   

    @PostMapping("/init")
    public String init() {
        AkkaService.initialize();
        System.out.println("initialisation de Akka ");
        return "redirect:/akka";
    }

    @GetMapping("/start")
	public String initApi( Model model, HttpSession session) {
		 
			
		 
		return "home";	
	}
	@GetMapping("/reset")
	public String resetApi( Model model, HttpSession session) {
		 
		return "home";
	}
	
	@GetMapping("/home")
	public String welcome(Model model) {
		 
		return "home";
	}

	@PostMapping(value = "/dispatch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String dispatchLines(@RequestParam("file") MultipartFile file, Model model) throws IOException {

		akkaService.setReader(file);
		akkaService.dispatchLinesToMapper();
		model.addAttribute("dispatched", akkaService.isDispatched());
		return "home";
	}

	public void dispatchLinesToMapper() throws IOException {
		 
		 
		String text;
		MapReduceMessage mapReduceMessage ;
		int i = 1;
		while( (text = bReader.readLine() ) != null) {
			mapReduceMessage = new MapReduceMessage(reducers, text);
			
			switch(i) {
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
			
			i = i==3 ? 1 : i+1;
		}
	   
		bReader.close();
		this.dispatched=true;
	   
   }

}


 //à continuer