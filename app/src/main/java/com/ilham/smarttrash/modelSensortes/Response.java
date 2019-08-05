package com.ilham.smarttrash.modelSensortes;

import java.util.List;

public class Response{
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
			"Response{" + 
			"result = '" + result + '\'' + 
			",value = '" + value + '\'' + 
			"}";
		}
}