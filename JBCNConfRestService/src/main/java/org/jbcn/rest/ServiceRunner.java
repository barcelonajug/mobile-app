package org.jbcn.rest;

import io.vertx.core.Vertx;

public class ServiceRunner {

	public static void main(String[] args) {
		Vertx.vertx().deployVerticle(new RestVerticle());
	}
}
