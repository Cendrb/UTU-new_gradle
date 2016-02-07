package cz.cendrb.utu.showactivities;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import cz.cendrb.utu.R;
import cz.cendrb.utu.utucomponents.Event;

/**
 * Created by cendr_000 on 06.02.2016.
 */
public class ShowEventViewHolder extends ShowViewHolder<Event> {
    TextView vTitle;
    TextView vDescription;
    TextView vLocation;
    TextView vPrice;
    TextView vDate;

    public ShowEventViewHolder(View view, Activity activity) {
        super(view, activity);
        vTitle = (TextView) view.findViewById(R.id.showEventTitle);
        vDescription = (TextView) view.findViewById(R.id.showEventDescription);
        vLocation = (TextView) view.findViewById(R.id.showEventLocation);
        vPrice = (TextView) view.findViewById(R.id.showEventPrice);
        vDate = (TextView) view.findViewById(R.id.showEventDate);
    }

    @Override
    public void setupUsing() {
        vTitle.setText(data.getTitle());
        vDescription.setText(data.getDescription());
        vLocation.setText(data.getLocation());
        vPrice.setText(String.valueOf(data.getPrice()));
        vDate.setText(data.getDateString());
    }
}
