package org.example.container;

import org.example.room.MeetingRoom;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MeetingCookies {

    private static final String MEET_ROOM = "meet-room";

    private static final Map<String, Object> COOKIES = new ConcurrentHashMap<>();

    public static MeetingRoom getMeetRoom() {
        return (MeetingRoom) COOKIES.get(MEET_ROOM);
    }

    public static void addMeetRoom(MeetingRoom meetingRoom) {
        if (Objects.nonNull(COOKIES.get(MEET_ROOM))) {
            throw new RuntimeException("current meeting is not finish!");
        }
        COOKIES.put(MEET_ROOM, meetingRoom);
    }

    public static void removeMeetRoom() {
        COOKIES.remove(MEET_ROOM);
    }
}
