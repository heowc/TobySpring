package com.tistory.tobyspring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface GenericController<T, P> {

	@RequestMapping(method = {RequestMethod.POST})
	public void add(T entity);
	@RequestMapping(method = {RequestMethod.GET})
	public T find(P id);
	@RequestMapping(method = {RequestMethod.PUT})
	public void modify(T entity);
	@RequestMapping(method = {RequestMethod.DELETE})
	public void remove(P id);
}
