package com.freeco.controller;

import com.freeco.model.security.User;
import com.freeco.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/login")
    public Object login(@AuthenticationPrincipal User loginedUser, @RequestParam(name = "logout", required = false) String logout) {
    	System.out.println("--------------------------------------login go..");
        return "login";
    }
    @RequestMapping(value = "/submitlogin")
    @ResponseBody
    public Object submitlogin(@AuthenticationPrincipal User loginedUser, @RequestParam(name = "logout", required = false) String logout) {
        if (logout != null) {
            return null;
        }
        if (loginedUser != null) {
            return userService.getById(loginedUser.getId());
        }
        return null;
    }
    @RequestMapping(value = "/logininfo")
    @ResponseBody
    public Object logininfo(@AuthenticationPrincipal User loginedUser, @RequestParam(name = "logout", required = false) String logout) {
        if (logout != null) {
            return null;
        }
        if (loginedUser != null) {
            return userService.getById(loginedUser.getId());
        }
        return null;
    }
//    @RequestMapping(value = "/logout")
//    public Object logout(@AuthenticationPrincipal User loginedUser, @RequestParam(name = "logout", required = false) String logout) {
////    	SecurityContextHolder.getContext().
//        return null;
//    }
}
