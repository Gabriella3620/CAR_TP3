package demo.controllers;

import akka.actor.ActorRef;
import akka.util.Timeout;

import demo.message.*;

import demo.actors.Mapper;
import demo.actors.Reducer;
import demo.akkaService.*;
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
}


 //à continuer