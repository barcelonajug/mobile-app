/**
 * Copyright (c) 2016, 2018 Gluon Software
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation and/or other materials provided
 *    with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse
 *    or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.devoxx.model;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class Conference {

    private static final Logger LOG = Logger.getLogger(Conference.class.getName());

    private static final ZoneId DEFAULT_CONFERENCE_ZONE_ID = ZoneId.of("Europe/Madrid");

    private String id; 
    private String name; 
    private String website; 
    private String imageURL; 
    private String fromDate; 
    private String endDate; 
    private ZonedDateTime fromDateTime;
    private ZonedDateTime endDateTime;
    private ZonedDateTime[] days;
    private Type eventType; 
    private String cfpURL; 
    private String cfpVersion;
    private long locationId;
    private boolean myBadgeActive;
    private List<Track> tracks;
    private List<SessionType> sessionTypes;
    private List<Floor> floorPlans;
    
    private ZoneId timezoneId = DEFAULT_CONFERENCE_ZONE_ID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;

        if (this.fromDate != null && this.endDate != null && this.timezoneId != null) {
            calculateConferenceDays();
        }
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;

        if (this.fromDate != null && this.endDate != null && this.timezoneId != null) {
            calculateConferenceDays();
        }
    }

    public Type getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = Type.valueOf(eventType);
    }

    public String getCfpURL() {
        return cfpURL;
    }

    public void setCfpURL(String cfpURL) {
        this.cfpURL = cfpURL;
    }

    public String getCfpVersion() {
        return cfpVersion;
    }

    public void setCfpVersion(String cfpVersion) {
        this.cfpVersion = cfpVersion;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public boolean isMyBadgeActive() {
        return myBadgeActive;
    }

    public void setMyBadgeActive(boolean myBadgeActive) {
        this.myBadgeActive = myBadgeActive;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public List<SessionType> getSessionTypes() {
        return sessionTypes;
    }

    public void setSessionTypes(List<SessionType> sessionTypes) {
        this.sessionTypes = sessionTypes;
    }

    public List<Floor> getFloorPlans() {
        return floorPlans;
    }

    public void setFloorPlans(List<Floor> floorPlans) {
        this.floorPlans = floorPlans;
    }

    public ZonedDateTime getFromDateTime() {
        return fromDateTime;
    }

    public ZonedDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setConferenceZoneId(String zoneId) {
    	this.timezoneId = ZoneId.of(zoneId);
    }
    
    public ZoneId getConferenceZoneId() {
        return timezoneId;
    }

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private void calculateConferenceDays() {
        this.fromDateTime = LocalDate.parse(fromDate, DATE_FORMATTER).atStartOfDay(timezoneId);
        this.endDateTime = LocalDate.parse(endDate, DATE_FORMATTER).atStartOfDay(timezoneId);
        long numberOfDays = DAYS.between(fromDateTime, endDateTime) + 1;
        days = new ZonedDateTime[(int) numberOfDays];
        days[0] = dayOnly(fromDateTime, timezoneId);
        for (int day = 1; day < numberOfDays; day++) {
            days[day] = days[0].plusDays(day);
        }
    }

    public ZonedDateTime[] getDays() {
        return days;
    }

    private static ZonedDateTime dayOnly(ZonedDateTime dateTime, ZoneId zoneId) {
        return ZonedDateTime.of(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), 0, 0, 0, 0, zoneId);
    }

    public int getConferenceDayIndex(ZonedDateTime date) {
        return Arrays.binarySearch(days, dayOnly(date, getConferenceZoneId())) + 1;
    }

    public long getDaysUntilStart() {
        LocalDate today = LocalDate.now(getConferenceZoneId());
        return today.until(getFromDateTime(), ChronoUnit.DAYS);
    }

    public long getDaysUntilEnd() {
        LocalDate today = LocalDate.now(getConferenceZoneId());
        return today.until(getEndDateTime(), ChronoUnit.DAYS);
    }

    public String getCountry() {
        String[] split = name.split(" ");
        return split.length >= 1 ? split[1] : "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conference that = (Conference) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Conference{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
    
    public enum Type {
    	MEETUP("Meetup", "Meetup"),
        CONFERENCE("Conference", "Conference");    	

        private String name;
        private String displayName;

        Type(String name, String displayName) {
            this.name = name;
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
