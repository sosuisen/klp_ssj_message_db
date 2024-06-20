package com.example;

import java.sql.SQLException;
import java.util.logging.Level;

import com.example.model.ErrorBean;
import com.example.model.message.MessageDTO;
import com.example.model.message.MessagesDAO;
import com.example.model.user.LoginUserModel;
import com.example.model.user.UserDTO;
import com.example.model.user.UsersModel;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

/**
 * Jakarta MVCのコンロトーラクラスです。@Controllerアノテーションを付けましょう。
 * 
 * コントローラクラスはCDI beanであることが必須で、必ず@RequestScopedを付けます。
 * 
 * CDI beanには引数のないコンストラクタが必須なので、
 * Lombokの@NoArgsConstructorで引数のないコンストラクタを作成します。
 * 
 * @Path はこのクラス全体が扱うURLのパスで、JAX-RSのアノテーションです。
 * これは @ApplicationPath からの相対パスとなります。
 * パスの先頭の/と末尾の/はあってもなくても同じです。
 */
@Controller
@RequestScoped
@NoArgsConstructor(force = true)
@Log
@Path("/")
public class MyController {
	private final Models models;
	
	private final LoginUserModel loginUserModel;

	private final ErrorBean errorBean;
	
	private final UsersModel usersModel;
	
	private final MessagesDAO messagesDAO;

	// @Injectはコンストラクタインジェクションを用いるのが定石です。
	@Inject
	public MyController(Models models, LoginUserModel loginUserModel, 
			ErrorBean errorBean, UsersModel usersModel, MessagesDAO messagesDAO,
			HttpServletRequest req) {
		this.models = models;
		this.loginUserModel = loginUserModel;
		this.errorBean = errorBean;
		this.usersModel = usersModel;
		this.messagesDAO = messagesDAO;
		log.log(Level.INFO, "[ip]%s [url]%s".formatted(
				req.getRemoteAddr(),
				req.getRequestURL().toString()));
	}

	/**
	 * @Path がないため、このメソッドはクラス全体が扱うURLのパスを扱います。
	 */
	@GET
	public String home() {
		models.put("appName", "メッセージアプリ");
		return "index.jsp";
	}

	@GET
	@Path("list")
	public String getMessage() {
		if(loginUserModel.getName() == null) {
			return "redirect:login";
		}
		try {
			models.put("messages", messagesDAO.getAll());
			return "list.jsp";
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Error in getMessage()", e);
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
	}

	@POST
	@Path("list")
	public String postMessage(@BeanParam MessageDTO mes) {
		mes.setName(loginUserModel.getName());
		try {
			messagesDAO.create(mes);
			// 	リダイレクトは "redirect:リダイレクト先のパス"
			return "redirect:list";
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Error in postMessage()", e);
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
	}

	@GET
	@Path("clear")
	public String clearMessage() {
		try {
			messagesDAO.deleteAll();
			return "redirect:list";
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Error in clearMessage()", e);
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
	}

	@GET
	@Path("login")
	public String getLogin() {
		loginUserModel.setName(null);		
		return "login.jsp";
	}

	@POST
	@Path("login")
	public String postLogin(@BeanParam UserDTO userDTO) {
		var name = userDTO.getName();
		if(usersModel.auth(name, userDTO.getPassword())){
			loginUserModel.setName(name);
			return "redirect:list";
		}
		errorBean.setMessage("ユーザ名またはパスワードが異なります");
		return "redirect:login";
	}
}
