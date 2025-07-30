package com.demo.bankapp.controller;

import com.demo.bankapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.bankapp.configuration.Constants;
import com.demo.bankapp.exception.BadRequestException;
import com.demo.bankapp.model.Wealth;
import com.demo.bankapp.request.RetrieveWealthRequest;
import com.demo.bankapp.response.RetrieveWealthResponse;
import com.demo.bankapp.service.abstractions.UserService;
import com.demo.bankapp.service.abstractions.WealthService;

@RestController
@RequestMapping(value = "/wealth", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WealthController {

	private WealthService wealthService;
	private UserService userService;

	@Autowired
	public WealthController(WealthService wealthService, UserService userService) {
		this.wealthService = wealthService;
		this.userService = userService;
	}

	@PostMapping("/retrieve")
	public RetrieveWealthResponse retrieveWealth(@RequestBody RetrieveWealthRequest request) {

		if (request.getUsername() == null || request.getUsername().equals("")) {
			throw new BadRequestException(Constants.MESSAGE_INVALIDUSERNAME);
		}

		User user = userService.findByUserName(request.getUsername());
		Wealth wealth = wealthService.findWealth(user.getId());

		RetrieveWealthResponse response = new RetrieveWealthResponse();
		response.setWealth(wealth);
		return response;
	}

}
