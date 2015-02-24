package cz.cendrb.utu.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.R;
import cz.cendrb.utu.utucomponents.Event;

/**
 * Created by cendr_000 on 21. 2. 2015.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> events;

    public EventAdapter() {
        events = MainActivity.utuClient.events;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.event_item, viewGroup, false);

        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        Event event = events.get(i);
        eventViewHolder.vAdditionalInfoUrl.setText(event.getAdditionalInfoUrl());
        //eventViewHolder.vDatePay.setText(event.getPay().toString());
        eventViewHolder.vDateStart.setText(event.getStart().toString());
        eventViewHolder.vDateEnd.setText(event.getEnd().toString());
        eventViewHolder.vTitle.setText(event.getTitle());
        eventViewHolder.vDescription.setText(event.getDescription());
        eventViewHolder.vLocation.setText(event.getLocation());
        //eventViewHolder.vPrice.setText(String.valueOf(event.getPrice()));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        protected TextView vTitle;
        protected TextView vDescription;
        protected TextView vLocation;
        protected TextView vPrice;
        protected TextView vDateStart;
        protected TextView vDateEnd;
        protected TextView vDatePay;
        protected TextView vAdditionalInfoUrl;

        public EventViewHolder(View itemView) {
            super(itemView);
            vTitle = (TextView) itemView.findViewById(R.id.eventTitle);
            vDescription = (TextView) itemView.findViewById(R.id.eventDescription);
            vLocation = (TextView) itemView.findViewById(R.id.eventLocation);
            //vPrice = (TextView) itemView.findViewById();
            vDateStart = (TextView) itemView.findViewById(R.id.eventFrom);
            vDateEnd = (TextView) itemView.findViewById(R.id.eventTo);
            //vDatePay = (TextView) itemView.findViewById(R.id.event);
            vAdditionalInfoUrl = (TextView) itemView.findViewById(R.id.eventAdditionalInfo);
        }
    }
}
