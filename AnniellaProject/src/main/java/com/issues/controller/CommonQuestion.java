package com.issues.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


@ManagedBean
@SessionScoped
public class CommonQuestion {
	
	Map<String, String>question = new HashMap<String, String>();
	Map<String,Map<String, String>>answer = new HashMap<String, Map<String, String>>();
	
	
	private String key = "";
	private List<String>answers = new ArrayList<String>();
	
	public CommonQuestion() {
		question = new HashMap<String, String>();
		question.put("How to check airtime balance", "How to check airtime balance");
		question.put("How to use mobile money service", "How to use mobile money service");
		question.put("How to activate voice pack", "How to activate voice pack");
		question.put("How to activate data bundle", "How to activate data bundle");
		question.put("How check your phone number", "How check your phone number");
		
		Map<String, String>map = new HashMap<String, String>();
		map.put("*131#", "*131#");
		answer.put("How to check airtime balance",map);
		
		map = new HashMap<String, String>();
		map.put("*182#", "*182#");
		answer.put("How to use mobile money service",map);
		
		map = new HashMap<String, String>();
		map.put("*140#", "*140#");
		answer.put("How to activate voice pack",map);
		
		map = new HashMap<String, String>();
		map.put("*345#", "*345#");
		answer.put("How to activate data bundle",map);
		
		map = new HashMap<String, String>();
		map.put("*135*8#", "*135*8#");
		answer.put("How check your phone number",map);
		
		
	}
	public List<String>questions(){
		return new ArrayList<>(question.values());
	}
	public void answerResult() {
		Map<String, String>map = new HashMap<String, String>();
		if(!key.equalsIgnoreCase("") && key != null) {
			map = this.answer.get(key);
		}else {
			map = new HashMap<String, String>();
		}
		this.answers =new ArrayList<String>(map.values());
		
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public List<String> getAnswers() {
		return answers;
	}
	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

}
