package org.example.configuration;

import cn.hutool.core.text.CharSequenceUtil;
import org.example.common.annotation.Bean;
import org.example.common.annotation.Configuration;
import org.example.common.constant.Constants;
import org.example.common.handler.Filter;
import org.example.container.MeetingCookies;
import org.example.schedule.TimeWheel;

import java.util.List;
import java.util.Objects;

@Configuration
public class Bootstrap {

    @Bean
    public TimeWheel timeWheel() {
        return new TimeWheel();
    }

    @Bean
    public Filter filter() {
        return (authentication) -> {
            if (Objects.isNull(MeetingCookies.getMeetRoom())) {
                return false;
            }
            if (Constants.MULTICAST.equals(authentication)) {
                return true;
            }
            if (CharSequenceUtil.isBlank(authentication)) {
                return false;
            }
            List<String> split = CharSequenceUtil.split(authentication, "#");
            String roomNumber = split.get(0);
            String username = split.get(1);
            return CharSequenceUtil.equals(roomNumber, MeetingCookies.getMeetRoom().roomNumber()) && !CharSequenceUtil.equals(username, MeetingCookies.getUsername());
        };
    }
}
