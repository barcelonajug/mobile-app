package org.jbcn.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class RestVerticle extends AbstractVerticle {

	private JsonArray allConferences;
	private Map<String, JsonObject> conferenceMap = new HashMap<>();
	
	@Override
	public void start() throws Exception {		
		readConferences(done -> {
			Router router = Router.router(vertx);
			
			router.get("/conferences").handler(this::getConferences);
			router.get("/conferences/:conferenceId").handler(this::getConferenceById);
			
			vertx.createHttpServer().requestHandler(router::handle).listen(7199);		
		});
		
	}

	
	private void readConferences(Handler<Void> handler) throws IOException {
		allConferences = new JsonArray(new String(Files.readAllBytes(Paths.get("src/main/resources/allConferences.json")), "UTF-8"));
		allConferences.forEach(conf -> {
			JsonObject conference = (JsonObject) conf;
			conferenceMap.put(conference.getString("id"), conference);
		});
		
		handler.handle(null);
	}


	private void getConferences(RoutingContext context) {
		context.response().end(allConferences.encode());
	}
	

	private void getConferenceById(RoutingContext context) {
		String conferenceId = context.pathParam("conferenceId");
		context.response().end(conferenceMap.get(conferenceId).encode());		
	}
}
