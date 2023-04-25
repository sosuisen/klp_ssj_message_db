package com.example.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * DAO for messages table
 */
@ApplicationScoped
public class MessagesDAO {
	/**
	 * JNDIで管理されたDataSourceオブジェクトは@Resourceアノテーションで
	 * 取得できます。lookup属性でJNDI名を渡します。
	 */
	@Resource(lookup = "jdbc/__default")
	private DataSource ds;

	// JSP側へデータを渡すために用いる。
	@Inject
	private Messages messages;

	public void getAll() {
		// リダイレクト先で呼ばれた場合は無視する。
		if (messages.size() > 0) return;
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM messages");) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int myId = rs.getInt("id");
				String name = rs.getString("name");
				String message = rs.getString("message");
				messages.add(new MessageDTO(myId, name, message));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void search(String keyword) {
		if (messages.size() > 0) return;
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM messages WHERE message LIKE ?");) {
			// 部分一致で検索する場合、LIKEの後には'%キーワード%'と書く。
			// 以下、文字列に文字列を埋め込むためのフォーマット指定子は%s
			// フォーマット指定子%をエスケープするには%%と書く
			pstmt.setString(1, "%%%s%%".formatted(keyword));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int myId = rs.getInt("id");
				String name = rs.getString("name");
				String message = rs.getString("message");
				messages.add(new MessageDTO(myId, name, message));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void create(MessageDTO mesDTO) {
		try (
				Connection conn = ds.getConnection();
				PreparedStatement pstmt = conn
						.prepareStatement("INSERT INTO messages(name, message) VALUES(?, ?)")) {
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
