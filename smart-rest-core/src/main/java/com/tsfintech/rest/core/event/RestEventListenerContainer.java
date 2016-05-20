package com.tsfintech.rest.core.event;

import org.apache.commons.lang3.ArrayUtils;

import com.tsfintech.rest.core.model.EntityUri;

/**
 * Created by jack on 15-1-12.
 */
public class RestEventListenerContainer {

    private RestEventListener[] eventListeners;

    public void fireEvent(EntityUri entityUri, RestEventType eventType, Object target) {
        if (eventListeners != null) {
            StringBuilder listeningBuilder = new StringBuilder()
                    .append(entityUri.getEntityScope()).append(":")
                    .append(entityUri.getEntityName()).append(":");

            if (eventType.equals(RestEventType.INVOKE)) {
                listeningBuilder.append(entityUri.getOperation());
            } else {
                listeningBuilder.append(eventType.name()).toString();
            }

            String listening = listeningBuilder.toString();
            for (RestEventListener listener : eventListeners) {
                if (listening.toString().matches(listener.listening())) {
                    listener.fireEvent(new RestEvent(entityUri, eventType, target));
                }
            }
        }
    }

    public void addEventListener(RestEventListener eventListener) {
        if (eventListener == null) {
            eventListeners = new RestEventListener[]{eventListener};
        } else {
            eventListeners = ArrayUtils.add(eventListeners, eventListener);
        }
    }

    public void setEventListeners(RestEventListener[] eventListeners) {
        this.eventListeners = eventListeners;
    }
}
