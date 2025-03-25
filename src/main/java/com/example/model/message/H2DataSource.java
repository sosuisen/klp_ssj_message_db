package com.example.model.message;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Produces;

/*
 * MessagesDAOクラスが使用するDataSourceを提供するクラスです。
 */
@ApplicationScoped
public class H2DataSource {
	/*
	 * JNDIで管理されたDataSourceオブジェクトは、@Resourceアノテーションで注入することができます。
	 * ここでは@Resource(lookup = "jdbc/__default")のようにlookup属性でJNDI名を渡しますが、
	 * 省略すると"jdbc/__default"となります。
	 * 
	 * Payara Serverのデフォルト設定では、"jdbc/__default"は
	 * 次の場所にあるH2 Dabtaseファイルを指します。
	 * Payara6インストール先/glassfish/domains/ドメイン名/lib/databases/embedded_default
	 * ドメイン名はデフォルトではdomain1です。
	 */ 
	@Resource
	private DataSource ds;

	/*
	 * @Producesアノテーションのついたメソッドは、CDIのプロデューサメソッドです。
	 * このメソッドは、DataSource型のインジェクションポイントにマッチすることができ、
	 * 戻り値のdsオブジェクトを注入します。
	 * （プロデューサメソッドはデータ型が重要であってメソッド名は任意です。）
	 */
	@Produces
	public DataSource getDataSource() {
		return ds;
	}

	/*
	 * アプリケーション起動時に呼ばれます。
	 * H2 Databaseファイルが存在しなければ作成するための処理です。
	 * 
	 * 実行していることはDataSourceへ接続してすぐに閉じるだけの処理ですが、
	 * H2 DatabaseはDataSourceの指すファイルが存在しない場合、
	 * 接続が試みられたときに自動生成する性質を利用しています。
	 */
	public void onStart(@Observes @Initialized(ApplicationScoped.class) Object event) {
		
	    try (Connection con = ds.getConnection()) {
	        System.out.println("Database created at: " + con.getMetaData().getURL());
	    } catch (SQLException e) {
	        throw new IllegalStateException(e);
	    }
	}
}
