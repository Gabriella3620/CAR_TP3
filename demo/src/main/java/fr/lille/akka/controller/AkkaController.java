package fr.lille.akka.controller;

import java.io.File;
import java.io.IOException;

import fr.lille.akka.model.Mot;
import fr.lille.akka.akkaService.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("akka")
public class AkkaController {

	@Autowired
	private AkkaService akkaService;

	@GetMapping("/start")
	public String initApi(Model model, HttpSession session) {
		if (session.getAttribute("init") == null) {
			akkaService.initialize();
			session.setAttribute("init", true);
		}
		model.addAttribute("dispatched", akkaService.isDispatched());
		return "akka";
	}

	@GetMapping("/reset")
	public String resetApi(Model model, HttpSession session) {
		akkaService.clean();
		session.invalidate();
		model.addAttribute("dispatched", akkaService.isDispatched());
		return "akka";
	}

	@GetMapping("/home")
	public String welcome(Model model) {
		model.addAttribute("dispatched", akkaService.isDispatched());
		return "akka";
	}

	@PostMapping(value = "/dispatch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String dispatchLines(@RequestParam("file") MultipartFile file, Model model) throws IOException {

		akkaService.setReader(file);
		akkaService.dispatchLinesToMapper();
		model.addAttribute("dispatched", akkaService.isDispatched());
		return "akka";
	}

	@PostMapping(path = "/search", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String searchWord(Mot mot, Model model) throws Exception {
		int nb = akkaService.getOccurrenceCount(mot.contenu());
		model.addAttribute("nb", nb);
		model.addAttribute("mot", mot.contenu());
		model.addAttribute("dispatched", akkaService.isDispatched());
		return "akka";
	}

}
