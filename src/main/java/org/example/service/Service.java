package org.example.service;

import cn.hutool.core.text.CharSequenceUtil;
import org.example.common.context.Factory;
import org.example.communication.server.api.MeetingRoomService;
import org.example.container.MeetingCookies;
import org.example.room.MeetingRoom;
import org.example.shell.annotation.Shell;
import org.example.shell.annotation.ShellMethod;

import java.time.LocalDateTime;

@Shell
public class Service {

    private final MeetingRoomService meetingRoomService = (MeetingRoomService) Factory.getBeanNotNull(MeetingRoomService.class);

    @ShellMethod
    public String login(String username) {
        MeetingCookies.login(username);
        return "success";
    }

    @ShellMethod(command = "create")
    public String applyingMeetingRoom() {
        String username = check();
        MeetingRoom meetingRoom = meetingRoomService.createMeetingRoom(username, LocalDateTime.now());
        MeetingCookies.addMeetRoom(meetingRoom);
        return meetingRoom.roomNumber();
    }

    @ShellMethod
    public String join(String roomNumber) {
        String username = check();
        boolean joined = meetingRoomService.joinRoom(roomNumber, username);
        if (joined) {
            return "success";
        } else {
            return "failed";
        }
    }

    private String check() {
        String username = MeetingCookies.getUsername();
        if (CharSequenceUtil.isBlank(username)) {
            throw new RuntimeException("you must login");
        }
        return username;
    }
}
