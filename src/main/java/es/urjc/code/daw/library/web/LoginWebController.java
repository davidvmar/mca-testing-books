package es.urjc.code.daw.library.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginWebController {
	
	@RequestMapping("/login")
	public String login(Model model) {

		return "login";
	}

	@RequestMapping("/loginerror")
	public String loginerror(Model model) {
		return "loginerror";
	}


	

}
