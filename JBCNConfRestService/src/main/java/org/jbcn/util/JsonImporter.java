package org.jbcn.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import com.devoxx.model.Link;
import com.devoxx.model.Speaker;
import com.devoxx.model.Tag;
import com.devoxx.model.Talk;
import com.devoxx.model.TalkSpeaker;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class JsonImporter {

	private static List<Slot> talkSlots = new ArrayList<>();
	private static List<Slot> workshopSlots = new ArrayList<>();
	private static List<Slot> finalSlots = new ArrayList<>();

	private static Map<String, Speaker> speakersMap = new HashMap<>();
	private static Map<String, Talk> talksMap = new HashMap<>();

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JsonObject readJsonFromUrl(String url) throws IOException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JsonObject json = new JsonObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public static void importJbcnConf() throws Exception {
		importSpeakers();
		importTalks();
		initSlots();
		generateSessions();
	}

	private static void initSlots() throws Exception {
		talkSlots.add(new Slot("a_room_1", "Room 1", sdf.parse("27/05/2019 09:00")));
		talkSlots.add(new Slot("a_room_2", "Room 2", sdf.parse("27/05/2019 09:00")));
		talkSlots.add(new Slot("a_room_3", "Room 3", sdf.parse("27/05/2019 09:00")));
		talkSlots.add(new Slot("a_room_4", "Room 4", sdf.parse("27/05/2019 09:00")));
		talkSlots.add(new Slot("a_room_1", "Room 1", sdf.parse("27/05/2019 10:00")));
		talkSlots.add(new Slot("a_room_2", "Room 2", sdf.parse("27/05/2019 10:00")));
		talkSlots.add(new Slot("a_room_3", "Room 3", sdf.parse("27/05/2019 10:00")));
		talkSlots.add(new Slot("a_room_4", "Room 4", sdf.parse("27/05/2019 10:00")));
		talkSlots.add(new Slot("a_room_1", "Room 1", sdf.parse("27/05/2019 11:00")));
		talkSlots.add(new Slot("a_room_2", "Room 2", sdf.parse("27/05/2019 11:00")));
		talkSlots.add(new Slot("a_room_3", "Room 3", sdf.parse("27/05/2019 11:00")));
		talkSlots.add(new Slot("a_room_4", "Room 4", sdf.parse("27/05/2019 11:00")));
		talkSlots.add(new Slot("a_room_1", "Room 1", sdf.parse("27/05/2019 12:00")));
		talkSlots.add(new Slot("a_room_2", "Room 2", sdf.parse("27/05/2019 12:00")));
		talkSlots.add(new Slot("a_room_3", "Room 3", sdf.parse("27/05/2019 12:00")));
		talkSlots.add(new Slot("a_room_4", "Room 4", sdf.parse("27/05/2019 12:00")));
		talkSlots.add(new Slot("a_room_1", "Room 1", sdf.parse("28/05/2019 09:00")));
		talkSlots.add(new Slot("a_room_2", "Room 2", sdf.parse("28/05/2019 09:00")));
		talkSlots.add(new Slot("a_room_3", "Room 3", sdf.parse("28/05/2019 09:00")));
		talkSlots.add(new Slot("a_room_4", "Room 4", sdf.parse("28/05/2019 09:00")));
		talkSlots.add(new Slot("a_room_1", "Room 1", sdf.parse("28/05/2019 10:00")));
		talkSlots.add(new Slot("a_room_2", "Room 2", sdf.parse("28/05/2019 10:00")));
		talkSlots.add(new Slot("a_room_3", "Room 3", sdf.parse("28/05/2019 10:00")));
		talkSlots.add(new Slot("a_room_4", "Room 4", sdf.parse("28/05/2019 10:00")));
		talkSlots.add(new Slot("a_room_1", "Room 1", sdf.parse("28/05/2019 11:00")));
		talkSlots.add(new Slot("a_room_2", "Room 2", sdf.parse("28/05/2019 11:00")));
		talkSlots.add(new Slot("a_room_3", "Room 3", sdf.parse("28/05/2019 11:00")));
		talkSlots.add(new Slot("a_room_4", "Room 4", sdf.parse("28/05/2019 11:00")));
		workshopSlots.add(new Slot("a_room_1", "Room 1", sdf.parse("29/05/2019 09:00"), 3));
		workshopSlots.add(new Slot("a_room_2", "Room 2", sdf.parse("29/05/2019 09:00"), 3));
		workshopSlots.add(new Slot("a_room_3", "Room 3", sdf.parse("29/05/2019 09:00"), 3));
		workshopSlots.add(new Slot("a_room_4", "Room 4", sdf.parse("29/05/2019 09:00"), 3));
		workshopSlots.add(new Slot("a_room_1", "Room 1", sdf.parse("29/05/2019 14:00"), 2));
		workshopSlots.add(new Slot("a_room_2", "Room 2", sdf.parse("29/05/2019 14:00"), 2));
		workshopSlots.add(new Slot("a_room_3", "Room 3", sdf.parse("29/05/2019 14:00"), 2));
		workshopSlots.add(new Slot("a_room_4", "Room 4", sdf.parse("29/05/2019 14:00"), 2));
	}
	
	private static void generateSessions() throws Exception {
		Random rdm = new Random(1000);
		for (Talk talk : talksMap.values()) {
			if ("talk".equalsIgnoreCase(talk.getTalkType())) {
				int nextIndex = rdm.nextInt(talkSlots.size());
				Slot slot = talkSlots.remove(nextIndex);
				slot.assignTalk(talk);
				finalSlots.add(slot);
			} else 
			if ("workshop".equalsIgnoreCase(talk.getTalkType())) {
				int nextIndex = rdm.nextInt(workshopSlots.size());
				Slot slot = workshopSlots.remove(nextIndex);
				slot.assignTalk(talk);
				finalSlots.add(slot);				
			} else throw new RuntimeException("Talk type not managed " + talk.getTalkType());
		}
		
		JsonArray sessions = new JsonArray();
		for (Slot slot : finalSlots) {
			sessions.add(slot.toJson());
		}

		Files.write(Paths.get("src/main/resources/sessionsV2.json"), sessions.encodePrettily().getBytes());

	}
	

	public static void importSpeakers() throws IOException, URISyntaxException {
		JsonObject json = readJsonFromUrl(
				"https://raw.githubusercontent.com/barcelonajug/jbcnconf_web/master/2019/_data/speakers.json");
		JsonArray fromSpeakers = json.getJsonArray("items");
		JsonArray toSpeakers = new JsonArray();

		for (int i = 0; i < fromSpeakers.size(); i++) {
			JsonObject speakerJson = fromSpeakers.getJsonObject(i);
			Speaker speaker = new Speaker();
			speaker.setUuid(speakerJson.getString("ref"));
			speaker.setBio(speakerJson.getString("biography"));
			speaker.setBioAsHtml(speakerJson.getString("biography"));
			String name = speakerJson.getString("name");
			String[] firstLast = name.split(" ");
			speaker.setFirstName(firstLast[0]);
			String lastName = "";
			for (int j = 1; j < firstLast.length; j++) {
				lastName += firstLast[j] + " ";
			}
			speaker.setLastName(lastName.trim());
			speaker.setAvatarURL("http://www.jbcnconf.com/2019/" + speakerJson.getString("image"));
			speaker.setCompany("");
			speaker.setBlog(speakerJson.getString("homepage"));
			speaker.setTwitter(speakerJson.getString("twitter"));
			speaker.setLang("en");

			speakersMap.put(speaker.getUuid(), speaker);

			toSpeakers.add(JsonObject.mapFrom(speaker));
		}
		Files.write(Paths.get("src/main/resources/speakers.json"), toSpeakers.encodePrettily().getBytes());
	}

	public static void importTalks() throws IOException {
		JsonObject json = readJsonFromUrl(
				"https://raw.githubusercontent.com/barcelonajug/jbcnconf_web/master/2019/_data/talks.json");
		JsonArray fromTalks = json.getJsonArray("items");
		JsonArray toTalks = new JsonArray();
		for (int i = 0; i < fromTalks.size(); i++) {
			JsonObject talkJson = fromTalks.getJsonObject(i);

			String id = talkJson.getString("id");
			String title = talkJson.getString("title");
			String talkType = talkJson.getString("type");
			String lang = "en";
			String audienceLevel = talkJson.getString("level");
			String summary = talkJson.getString("abstract");
			List<Tag> tags = new ArrayList<>();
			JsonArray jsonTags = talkJson.getJsonArray("tags");
			for (int j = 0; j < jsonTags.size(); j++) {
				tags.add(new Tag(jsonTags.getString(j)));
			}
			List<TalkSpeaker> speakers = new ArrayList<>();
			JsonArray jsonSpeakers = talkJson.getJsonArray("speakers");
			for (int j = 0; j < jsonSpeakers.size(); j++) {
				TalkSpeaker ts = new TalkSpeaker();
				String speakerId = jsonSpeakers.getString(j);
				Speaker speaker = speakersMap.get(speakerId);
				if (speaker != null) {
					ts.setLink(new Link(speakerId, speakerId, speaker.getFullName()));
					ts.setName(speaker.getFullName());
					speakers.add(ts);
				} else {
					System.err.println("Speaker " + speakerId + " not found for talk " + title);
				}
			}
			Talk talk = new Talk(id, title, talkType, null, lang, audienceLevel, summary, summary, tags, speakers);
			toTalks.add(JsonObject.mapFrom(talk));

			talksMap.put(talk.getId(), talk);
		}
		Files.write(Paths.get("src/main/resources/talks.json"), toTalks.encodePrettily().getBytes());
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static SimpleDateFormat shortFormat = new SimpleDateFormat("HH:mm");
	
	private static class Slot {
		private String slotId;
		private String roomId;
		private String roomName;
		private String day;
		private String fromTime;
		private String toTime;
		private Talk talk;
		private long fromTimeMillis;
		private long toTimeMillis;

		public Slot(String roomId, String roomName, Date fromTime) {
			this(roomId, roomName, fromTime, 1);
		}

		public Slot(String roomId, String roomName, Date fromTime, int duration) {
			super();
			this.roomId = roomId;
			this.roomName = roomName;
			Calendar cal = Calendar.getInstance();
			cal.setTime(fromTime);
			this.day = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
			this.fromTime = shortFormat.format(fromTime);
			this.fromTimeMillis = fromTime.getTime();
			cal.add(Calendar.HOUR, duration);
			this.toTime = shortFormat.format(cal.getTime());
			this.toTimeMillis = cal.getTimeInMillis();
		}

		public void assignTalk(Talk talk) {
			this.talk = talk;
			this.slotId = talk.getId() + "_" + roomId + "_" + day + "_" + fromTime + "_" + toTime;
		}

		public JsonObject toJson() {
			JsonObject obj = new JsonObject();
			obj.put("slotId", slotId);
			obj.put("roomId", roomId);
			obj.put("roomName", roomName);
			obj.put("day", day);
			obj.put("fromTime", fromTime);
			obj.put("fromTimeMillis", fromTimeMillis);
			obj.put("toTime", toTime);
			obj.put("toTimeMillis", toTimeMillis);
			obj.put("talk", JsonObject.mapFrom(talk));			
			return obj;
		}
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		importJbcnConf();
	}

}

