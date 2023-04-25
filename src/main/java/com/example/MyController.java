package com.example;

import com.example.model.ErrorBean;
import com.example.model.LoginUser;
import com.example.model.MessageDTO;
import com.example.model.MessagesDAO;
import com.example.model.UserDTO;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import lombok.NoArgsConstructor;

@Controller
@RequestScoped
@NoArgsConstructor(force = true)
@Path("/")
public class MyController {
	private final MessagesDAO messagesDAO;

	private final LoginUser loginUser;

	private final ErrorBean errorBean;

	@Inject
	public MyController(MessagesDAO messagesDAO, LoginUser loginUser, ErrorBean errorBean) {
		this.messagesDAO = messagesDAO;
		this.loginUser = loginUser;
		this.errorBean = errorBean;
	}

	@Inject
	private Models models;

	@GET
	public String home() {
		models.put("appName", "メッセージアプリ");
		return "index.jsp";
	}

	@GET
	@Path("list")
	public String getMessage() {
		if (loginUser.getName() == null) {
			return "redirect:login";
		}
		messagesDAO.getAll();
		return "list.jsp";
	}

	@POST
	@Path("list")
	public String postMessage(@BeanParam MessageDTO mes) {
		mes.setName(loginUser.getName());
		messagesDAO.create(mes);
		return "redirect:list";
	}

	@GET
	@Path("clear")
	public String clearMessage() {
		messagesDAO.deleteAll();
		return "redirect:list";
	}

	@GET
	@Path("login")
	public String getLogin() {
		loginUser.setName(null);
		return "login.jsp";
	}

	@POST
	@Path("login")
	public String postLogin(@BeanParam UserDTO userDTO) {
		if (userDTO.getName().equals("kcg") && userDTO.getPassword().equals("foo")) {
			loginUser.setName(userDTO.getName());
			return "redirect:list";
		}

		errorBean.setMessage("ユーザ名またはパスワードが異なります");
		return "redirect:login";
	}

	@POST
	@Path("search")
	public String postSearch(@FormParam("keyword") String keyword) {
		messagesDAO.search(keyword);
		// messages が @RedirectScoped なので、リダイレクト先でも参照可能。
		return "redirect:list";
	}

}
