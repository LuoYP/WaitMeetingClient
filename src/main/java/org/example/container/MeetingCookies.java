package org.example.container;

import org.example.room.MeetingRoom;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MeetingCookies {

    private static final String MEET_ROOM = "meet-room";

    private static final String USERNAME = "username";

    private static final Map<String, Object> COOKIES = new ConcurrentHashMap<>();

    public static MeetingRoom getMeetRoom() {
        return (MeetingRoom) COOKIES.get(MEET_ROOM);
    }

    public static void addMeetRoom(MeetingRoom meetingRoom) {
        COOKIES.put(MEET_ROOM, meetingRoom);
    }

    public static String getUsername() {
        return (String) COOKIES.get(USERNAME);
    }

    public static void login(String username) {
        if (Objects.nonNull(COOKIES.get(USERNAME))) {
            throw new RuntimeException("you must logout!");
        }
        COOKIES.put(USERNAME, username);
    }

    public static void logout() {
        COOKIES.remove(USERNAME);
    }

    public static void removeMeetRoom() {
        COOKIES.remove(MEET_ROOM);
    }
}
