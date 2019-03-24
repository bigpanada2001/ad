package com.freeco.controller;

import com.freeco.service.ShardingUserService;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RequestMapping(value = "/users")
@RestController
public class UserController {
	@Resource
	ShardingUserService shardingUserServiceImpl;

	@RequestMapping(value = "/listall", method = RequestMethod.GET)
    @ResponseBody
    public Object list(HttpServletRequest request) {
		List rst = shardingUserServiceImpl.getUsers();
		System.out.println(JSON.toJSONString(rst));
		return rst;
    }

	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object detail(@PathVariable Integer id) {
		return "Get a user";
    }
    
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Object create(HttpServletRequest request) {
		return "POST a user";
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public Object update(HttpServletRequest request) {
		return "PUT a user";
    }

}
