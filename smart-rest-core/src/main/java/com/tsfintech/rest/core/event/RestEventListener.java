package com.tsfintech.rest.core.event;

/**
 * Created by jack on 15-1-12.
 */
public interface RestEventListener {

    public void fireEvent(RestEvent restEvent);

    public String listening();

}
