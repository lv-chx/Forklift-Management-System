package cn.zwqh.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.zwqh.springboot.model.UserEntity;

@Controller

public class jikoController {
	
	   @RequestMapping(value ="/index",method=RequestMethod.GET)
	   public String printHello(Model model) {
		   
	      model.addAttribute("name", "zhangsan");
	      model.addAttribute("tel", "1232457987");
	      model.addAttribute("sex", "F");
	      model.addAttribute("age", "88");
	      model.addAttribute("marriage", "1");
	      model.addAttribute("fristlike", "kkkkkk");
	      
	      
	      return "index";
	   }
	   
}
