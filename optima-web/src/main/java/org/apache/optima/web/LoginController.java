package org.apache.optima.web;
import org.apache.optima.web.component.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class LoginController {
	@Autowired
	private IPersonService personService;
	@RequestMapping("/home")
    public String onSubmit(@RequestParam(value="userId", required=false) String userId,
    		@RequestParam(value="password", required=false) String password,
    		Model model) {
			
	        model.addAttribute("msg", "Hello "+personService.getPersonName() );
	        model.addAttribute("userId", userId);
	        model.addAttribute("password", password);
	        if("optima".equals(userId) && "optima".equals(password)){
	        	return "home";
	        }
	        return "index_bak";
	}
}