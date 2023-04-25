package com.example.model;

import jakarta.ws.rs.FormParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * メッセージ情報の受け渡しに用いるDTO（Data Transfer Object）です。
 * 次の3か所で利用されます。
 * 1) list.jspで表示したフォームからPOSTされたデータを、
 *   MyControllerのpostMessageメソッドの@BeanParamへ注入。
 * 2) posetMessageメソッド内でmessagesに追加。
 * 3) list.jspでmessagesから取り出されて表示。
 * 
 * MessageDAOクラス内では、new でMessageDTOのインスタンスを作りたいので、
 * @AllArgsConstructor で全フィールドを引数にもつコンストラクタを追加
 * 
 * 上記のようにコンストラクタを明示的に追加した場合に限り、
 * @BeanParam 指定の際に必要なデフォルトコンストラクタ作成のための
 * アノテーション @NoArgsConstructor も明示的に追加する必要があります。
 *
 * @BeanParam でデータを渡すためには
 * list.jspのフォーム内でのinput要素のnameと
 * クラスのフィールドとの対応付けを
 * @FormParam で指定しておく必要があります。
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class MessageDTO {
	@FormParam("id")
	private int id;
	@FormParam("name")
	private String name;
	@FormParam("message")
	private String message;
}