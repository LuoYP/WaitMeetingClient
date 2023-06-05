package org.example.configuration;

import cn.hutool.core.text.CharSequenceUtil;
import org.example.common.annotation.Bean;
import org.example.common.annotation.Configuration;
import org.example.common.constant.Constants;
import org.example.common.handler.Filter;
import org.example.container.MeetingCookies;
import org.example.schedule.TimeWheel;

@Configuration
public class Bootstrap {

    @Bean
    public TimeWheel timeWheel() {
        return new TimeWheel();
    }

    @Bean
    public Filter filter() {
        return (roomNumber) -> {
            if (Constants.MULTICAST.equals(roomNumber)) {
                return true;
            }
            if (CharSequenceUtil.isBlank(roomNumber)) {
                return false;
            }
            return CharSequenceUtil.equals(roomNumber, MeetingCookies.getMeetRoom().roomNumber());
        };
    }
}
