package com.tistory.tobyspring.controller;

public abstract class AbstactController<T, P, S> implements GenericController<T, P> {

	private S service;

	public S getService() {
		return service;
	}
}
