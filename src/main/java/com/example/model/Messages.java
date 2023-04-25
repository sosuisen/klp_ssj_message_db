package com.example.model;

import java.io.Serializable;
import java.util.ArrayList;

import jakarta.inject.Named;
import jakarta.mvc.RedirectScoped;

/**
 * MyControllerからJSPへのデータ受け渡しを担います。
 * リダイレクト後のページで用いることがあるので @RedirectScoped とします。
 */
@RedirectScoped
@Named
public class Messages extends ArrayList<MessageDTO> implements Serializable {
}
