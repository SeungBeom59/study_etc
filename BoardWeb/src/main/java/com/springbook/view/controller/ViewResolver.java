package com.springbook.view.controller;

import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Map;

public class ViewResolver {
	
	public String prefix;
	public String suffix;
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	public String getView(String viewName) {
		return prefix + viewName + suffix;
	}
}

