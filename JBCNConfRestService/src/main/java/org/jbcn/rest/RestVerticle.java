package org.jbcn.rest;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
	
	private JsonArray allSessions;
	
	private JsonArray allSpeakers;
	private Map<String, JsonObject> speakerMap = new HashMap<>();
	
	@Override
	public void start() throws Exception {		
		readConferences(done -> {
			Router router = Router.router(vertx);
			
			router.get("/conferences").handler(this::getConferences);
			router.get("/conferences/:conferenceId").handler(this::getConferenceById);
			router.get("/sessions/:conferenceId").handler(this::getSessions);
			
			router.get("/speakers/:conferenceId").handler(this::getSpeakers);
			router.get("/speaker/:conferenceId/:speakerId").handler(this::getSpeakerById);
			
			vertx.createHttpServer().requestHandler(router::handle).listen(7199);		
		});
		
	}

	
	private void readConferences(Handler<Void> handler) throws IOException {
		allConferences = new JsonArray(new String(Files.readAllBytes(Paths.get("src/main/resources/allConferences.json")), UTF_8));
		allConferences.forEach(conf -> {
			JsonObject conference = (JsonObject) conf;
			conferenceMap.put(conference.getString("id"), conference);
		});
		
		allSessions = new JsonArray(new String(Files.readAllBytes(Paths.get("src/main/resources/sessions.json")), UTF_8));

		allSpeakers = new JsonArray(new String(Files.readAllBytes(Paths.get("src/main/resources/speakers.json")), UTF_8));
		allSpeakers.forEach(conf -> {
			JsonObject speaker = (JsonObject) conf;
			speakerMap.put(speaker.getString("uuid"), speaker);
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
	
	private void getSessions(RoutingContext context) {
		context.response().end(allSessions.encode());
	}
	
	private void getSpeakers(RoutingContext context) {
		context.response().end(allSpeakers.encode());
	}
	
	private void getSpeakerById(RoutingContext context) {
		String speakerId = context.pathParam("speakerId");
		context.response().end(speakerMap.get(speakerId).encode());
	}

}
