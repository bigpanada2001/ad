package com.freeco.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freeco.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController  
@RequestMapping(value = "/test")  
public class TestController {
	@Resource
	TestService testServiceImpl;

    @RequestMapping(value = "/sessionset", method = RequestMethod.GET)
    public Map<String, Object> firstResp (HttpServletRequest request){
        Map<String, Object> map = new HashMap();
        request.getSession().setAttribute("requestUrl", "---------"+request.getRequestURL());
        map.put("requestUrl", request.getSession().getAttribute("request Url"));
        return map;
    }
  
    @RequestMapping(value = "/sessionget", method = RequestMethod.GET)
    public Object sessions (HttpServletRequest request){
        Map<String, Object> map = new HashMap();
        map.put("sessionId", request.getSession().getId());
        map.put("message", request.getSession().getAttribute("requestUrl"));
        return map;
    }
    
    
    @RequestMapping(value = "/getCache", method = RequestMethod.GET)
    public Object getCache(HttpServletRequest request, String key){
        Map<String, Object> map = new HashMap();
        map.put("sessionId", request.getSession().getId());
        map.put("cacheValue", testServiceImpl.getCache(key));
        return map;
    }
    @RequestMapping(value = "/setCache", method = RequestMethod.GET)
    public Object setCache(HttpServletRequest request, String key, String value){
    	testServiceImpl.setCache(key, value);
        Map<String, Object> map = new HashMap();
        map.put("result", "success!");
        return map;
    }
    
    @RequestMapping(value = "/getUsers", method = RequestMethod.GET)
    public Object getUsers(HttpServletRequest request){
    	return testServiceImpl.getUsers();
    }
    @RequestMapping(value = "/testTrans", method = RequestMethod.GET)
    public Object testTrans(HttpServletRequest request) throws Exception {
    	testServiceImpl.testTrans();
    	return "success";
    }

}
