package org.example.service;

import cn.hutool.core.text.CharSequenceUtil;
import org.example.common.context.Factory;
import org.example.common.io.sound.RpcAudioFormat;
import org.example.common.io.sound.RpcSourceDataLine;
import org.example.communication.server.api.MeetingRoomService;
import org.example.container.MeetingCookies;
import org.example.room.MeetingRoom;
import org.example.shell.annotation.Shell;
import org.example.shell.annotation.ShellMethod;
import org.example.shell.annotation.ShellOption;

import javax.sound.sampled.*;
import java.io.IOException;
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
        MeetingRoom meetingRoom = meetingRoomService.joinRoom(roomNumber, username);
        MeetingCookies.addMeetRoom(meetingRoom);
        new Thread(() -> {
            try {
                AudioFormat audioFormat = new AudioFormat(44100.0F, 16, 2, true, false);
                DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
                TargetDataLine line = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                line.open(audioFormat);
                line.start();
                AudioInputStream audioInputStream = new AudioInputStream(line);

                RpcAudioFormat rpcAudioFormat = new RpcAudioFormat(44100.0F, 16, 2, true, false);
                RpcSourceDataLine sourceDataLine = RpcSourceDataLine.build(rpcAudioFormat, roomNumber);
                sourceDataLine.start();
                byte[] bytes = new byte[1024];
                while (audioInputStream.read(bytes, 0, 1024) != -1) {
                    sourceDataLine.write(bytes);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        return "success";
    }

    private String check() {
        String username = MeetingCookies.getUsername();
        if (CharSequenceUtil.isBlank(username)) {
            throw new RuntimeException("you must login");
        }
        return username;
    }
}
