package io.github.reionchan.users.event;

import lombok.Getter;

import java.util.EventObject;

/**
 * @author Reion
 * @date 2024-11-29
 **/
@Getter
public class UserEvent extends EventObject {

    private UserEventType eventType;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public UserEvent(UserEventType eventType, UserEventContext source) {
        super(source);
        this.eventType = eventType;
    }
}
