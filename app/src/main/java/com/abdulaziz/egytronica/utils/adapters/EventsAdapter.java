package com.abdulaziz.egytronica.utils.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdulaziz.egytronica.R;
import com.abdulaziz.egytronica.data.model.Event;
import com.abdulaziz.egytronica.utils.Utils;

import java.util.ArrayList;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by abdulaziz on 6/7/17.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Event> eventsList;
    private final PublishSubject<Event> onclickSubject = PublishSubject.create();

    public EventsAdapter(Context context, ArrayList<Event> eventsList) {
        this.context = context;
        this.eventsList = eventsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.events_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Event event = eventsList.get(position);

        holder.titleTV.setText(event.getTitle());

        holder.dateTV.setText(Utils.timeStampToFormattedDate(String.valueOf(event.getDate())));
        holder.timeTV.setText(Utils.timeStampToTime(String.valueOf(event.getDate())));


        holder.locationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickSubject.onNext(event);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public Observable<Event> getPositionClicks(){return onclickSubject;}

    public class ViewHolder extends RecyclerView.ViewHolder{

        public View mainView;
        public TextView titleTV, dateTV, timeTV;
        public ImageView locationIV;

        public ViewHolder(View itemView) {
            super(itemView);

            mainView = itemView.findViewById(R.id.events_item_card_view);

            titleTV = (TextView) itemView.findViewById(R.id.events_item_title_tv);
            dateTV = (TextView) itemView.findViewById(R.id.events_item_date_tv);
            timeTV = (TextView) itemView.findViewById(R.id.events_item_time_tv);

            locationIV = (ImageView) itemView.findViewById(R.id.events_item_location_iv);
        }
    }
}