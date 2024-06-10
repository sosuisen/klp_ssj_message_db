package com.example.model.message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.sql.DataSource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

/*
 * メッセージアプリからデータベースへのリクエストを仲介する
 * DAO（Data Access Object）クラスです。
 */
@ApplicationScoped
@NoArgsConstructor(force = true)
public class MessagesDAO {
	private final DataSource ds;
	/*
	 * DataSource型のオブジェクトをコンストラクタインジェクションしています。
	 * この配布プロジェクトでは、H2DataSourceクラスの提供するオブジェクトが注入されます。
	 * テストの時や、他のデータベースを使いたいときにはDataSourceを変更できます。
	 */
	@Inject
	public MessagesDAO(DataSource ds) {
		this.ds = ds;
	}

	public ArrayList<MessageDTO> getAll() {
		var messagesModel = new ArrayList<MessageDTO>();
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM messages");) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				messagesModel.add(new MessageDTO(
						rs.getInt("id"),
						rs.getString("name"),
						rs.getString("message")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return messagesModel;
	}

	public void create(MessageDTO mesDTO) {
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn
						.prepareStatement("INSERT INTO messages(name, message) VALUES(?, ?)");) {
			pstmt.setString(1, mesDTO.getName());
			pstmt.setString(2, mesDTO.getMessage());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteAll() {
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("DELETE from messages");) {
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
