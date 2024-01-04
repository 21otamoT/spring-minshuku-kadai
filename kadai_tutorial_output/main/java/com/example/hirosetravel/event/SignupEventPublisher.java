package com.example.hirosetravel.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.hirosetravel.entity.User;

@Component
public class SignupEventPublisher {
    private final ApplicationEventPublisher APPLICATION_EVENT_PUBLISHER;
    
    public SignupEventPublisher(ApplicationEventPublisher APPLICATION_EVENT_PUBLISHER) {
    	this.APPLICATION_EVENT_PUBLISHER = APPLICATION_EVENT_PUBLISHER;
    }
    
    public void publishSignupEvent(User user, String requestUrl) {
    	APPLICATION_EVENT_PUBLISHER.publishEvent(new SignupEvent(this, user, requestUrl));
    }
}
