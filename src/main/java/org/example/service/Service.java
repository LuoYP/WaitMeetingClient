package org.example.service;

import org.example.common.annotation.Autowired;
import org.example.common.annotation.Component;
import org.example.communication.server.api.MeetingRoomService;
import org.example.container.MeetingCookies;
import org.example.room.MeetingRoom;

import java.time.LocalDateTime;

@Component
public class Service {

    @Autowired
    private MeetingRoomService meetingRoomService;

    public String applyingMeetingRoom(String username, LocalDateTime scheduleTime) {
        MeetingRoom meetingRoom = meetingRoomService.createMeetingRoom(username, scheduleTime);
        MeetingCookies.addMeetRoom(meetingRoom);
        return meetingRoom.roomNumber();
    }

    public void join(String roomNumber, String username) {
        boolean joined = meetingRoomService.joinRoom(roomNumber, username);
    }
}
