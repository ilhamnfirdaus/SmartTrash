package com.ilham.smarttrash.modelKontrol;

import java.util.List;

public class ResponseKontrol{
	private List<ResultItem> result;
	private int value;

	public void setResult(List<ResultItem> result){
		this.result = result;
	}

	public List<ResultItem> getResult(){
		return result;
	}

	public void setValue(int value){
		this.value = value;
	}

	public int getValue(){
		return value;
	}

	@Override
 	public String toString(){
		return 
			"ResponseKontrol{" + 
			"result = '" + result + '\'' + 
			",value = '" + value + '\'' + 
			"}";
		}}