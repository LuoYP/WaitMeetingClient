package org.example.service;

import cn.hutool.core.text.CharSequenceUtil;
import org.example.common.context.Factory;
import org.example.communication.server.api.MeetingRoomService;
import org.example.container.MeetingCookies;
import org.example.room.MeetingRoom;
import org.example.shell.annotation.Shell;
import org.example.shell.annotation.ShellMethod;
import org.example.shell.annotation.ShellOption;

import java.time.LocalDateTime;

@Shell
public class Service {

    private final MeetingRoomService meetingRoomService = (MeetingRoomService) Factory.getBeanNotNull(MeetingRoomService.class);

    @ShellMethod(description = "登录")
    public String login(@ShellOption(description = "用户名") String username) {
        MeetingCookies.login(username);
        return "success";
    }

    @ShellMethod(command = "create", description = "创建会议室")
    public String applyingMeetingRoom() {
        String username = check();
        MeetingRoom meetingRoom = meetingRoomService.createMeetingRoom(username, LocalDateTime.now());
        MeetingCookies.addMeetRoom(meetingRoom);
        return meetingRoom.roomNumber();
    }

    @ShellMethod(description = "加入会议")
    public String join(@ShellOption(description = "会议号") String roomNumber) {
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
