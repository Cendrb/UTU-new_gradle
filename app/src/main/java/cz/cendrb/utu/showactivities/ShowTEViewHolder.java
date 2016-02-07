package cz.cendrb.utu.showactivities;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import cz.cendrb.utu.R;
import cz.cendrb.utu.utucomponents.TEItem;

/**
 * Created by cendr_000 on 06.02.2016.
 */
public class ShowTEViewHolder extends ShowViewHolder<TEItem> {

    TextView vTitle;
    TextView vDescription;
    TextView vSubject;
    TextView vDate;

    public ShowTEViewHolder(View view, Activity activity) {
        super(view, activity);
        vTitle = (TextView) view.findViewById(R.id.showTETitle);
        vDescription = (TextView) view.findViewById(R.id.showTEDescription);
        vSubject = (TextView) view.findViewById(R.id.showTESubjectCircle);
        vDate = (TextView) view.findViewById(R.id.showTEDate);
    }

    @Override
    public void setupUsing() {
        vTitle.setText(data.getTitle());
        vDescription.setText(data.getDescription());
        vSubject.setText(data.getSubject().getName());
        vDate.setText(data.getDateString());
    }
}
