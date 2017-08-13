package com.abdulaziz.egytronica.ui.other_section.events_fragment;

import com.abdulaziz.egytronica.data.model.Event;
import com.abdulaziz.egytronica.ui.base.BaseView;

import java.util.ArrayList;

/**
 * Created by abdulaziz on 6/7/17.
 */

public interface EventsBaseView  extends BaseView {

    void eventsListReqComplete();
    void eventsListReqSuccess(ArrayList<Event> eventsList);
    void eventsListReqError(String e);

}

