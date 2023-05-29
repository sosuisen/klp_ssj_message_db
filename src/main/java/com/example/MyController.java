package com.example;

import com.example.model.ErrorBean;
import com.example.model.LoginUserModel;
import com.example.model.MessageDTO;
import com.example.model.MessagesDAO;
import com.example.model.UserDTO;
import com.example.model.UsersModel;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import lombok.NoArgsConstructor;

/**
 * Jakarta MVCのコンロトーラクラスです。@Controllerアノテーションを付けてください。
 * 
 * 加えて、コントローラクラスは必ず@RequestScopedを付けてCDI Beanにします。
 * 
 * CDI beanには引数のないコンストラクタが必須なので、
 * Lombokの@NoArgsConstructorで空っぽのコンストラクタを作成します。
 * ただし、このクラスは宣言時に初期化してないfinalフィールドを持つため、
 * このままだとフィールドが初期化されない可能性があってコンパイルエラーとなります。
 * これを防ぐには(force=true)指定が必要です。
 */
@Controller
@RequestScoped
@NoArgsConstructor(force = true)
@Path("/")
public class MyController {
	private final MessagesDAO messagesDAO;

	private final Models models;

	private final LoginUserModel loginUserModel;

	private final ErrorBean errorBean;
	
	private final UsersModel usersModel;

	@Inject
	public MyController(Models models, MessagesDAO messagesDAO, LoginUserModel loginUserModel,
		ErrorBean errorBean, UsersModel usersModel) {
		this.models = models;
		this.messagesDAO = messagesDAO;
		this.loginUserModel = loginUserModel;
		this.errorBean = errorBean;
		this.usersModel = usersModel;
	}

	@GET
	public String home() {
		models.put("appName", "メッセージアプリ");
		return "index.jsp";
	}

	@GET
	@Path("list")
	public String getMessage() {
		if (loginUserModel.getName() == null) {
			return "redirect:login";
		}
		messagesDAO.getAll();
		return "list.jsp";
	}

	@POST
	@Path("list")
	public String postMessage(@BeanParam MessageDTO mes) {
		mes.setName(loginUserModel.getName());
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
		loginUserModel.setName(null);
		return "login.jsp";
	}

	@POST
	@Path("login")
	public String postLogin(@BeanParam UserDTO userDTO) {
		var name = userDTO.getName();
		var password = userDTO.getPassword();
		if(password.equals(usersModel.getPassword(name))){
			loginUserModel.setName(name);
			return "redirect:list";
		}
		errorBean.setMessage("ユーザ名またはパスワードが異なります");
		return "redirect:login";
	}
}
