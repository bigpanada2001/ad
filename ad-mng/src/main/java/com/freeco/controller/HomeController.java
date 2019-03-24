package com.freeco.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by yangyibo on 17/1/18.
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request,
			HttpServletResponse response){
        model.addAttribute("msg", "11111111111111111111");
        return "/index";	
    }
    @RequestMapping("/home")
    public String home(Model model, HttpServletRequest request,
			HttpServletResponse response){
        model.addAttribute("msg", "11111111111111111111");
        return "/home";	
    }
}
