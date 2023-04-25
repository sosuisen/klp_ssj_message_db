package com.example.model;

import java.io.Serializable;
import java.util.ArrayList;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

/**
 * MessageDAO から JSP へのデータ受け渡しを担うだけなので、
 * @RequestScoped です。 
 */
@RequestScoped
@Named
public class Messages extends ArrayList<MessageDTO> implements Serializable {
}
