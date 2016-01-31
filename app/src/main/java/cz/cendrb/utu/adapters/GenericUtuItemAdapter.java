package cz.cendrb.utu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import cz.cendrb.utu.R;
import cz.cendrb.utu.utucomponents.GenericUtuItem;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class GenericUtuItemAdapter extends RecyclerView.Adapter<GenericUtuItemAdapter.GenericUtuItemViewHolder> {

    static String TAG = "GenericUtuItemAdapter";

    private Context context;
    private List<GenericUtuItem> data;
    private EventListener mEventListener;

    public GenericUtuItemAdapter(Context context, List<GenericUtuItem> data) {
        this.context = context;
        this.data = data;

        Log.d(TAG, String.valueOf(data.size()));

        setHasStableIds(true);
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public void setEventListener(EventListener mEventListener) {
        this.mEventListener = mEventListener;
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public GenericUtuItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.generic_utu_line_item, viewGroup, false);

        return new GenericUtuItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GenericUtuItemViewHolder eventViewHolder, int i) {
        PrettyTime prettyTime = new PrettyTime();
        DateFormat dateFormat = new SimpleDateFormat(" (E dd. MM.)");

        final ITaskExam item = data.get(i);

        teViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEventListener != null) {
                    mEventListener.onItemViewClicked(v, item);
                }
            }
        });

        teViewHolder.vTitle.setText(item.getTitle());
        teViewHolder.vSubjectCircle.setText(item.getSubjectString());
        teViewHolder.vDescription.setText(item.getDescription());
        if (item instanceof Exam)
            teViewHolder.vSubjectCircle.setBackground(context.getResources().getDrawable(R.drawable.exam_circle));
        else
            teViewHolder.vSubjectCircle.setBackground(context.getResources().getDrawable(R.drawable.task_circle));
        teViewHolder.vDate.setText(prettyTime.format(item.getDate()) + dateFormat.format(item.getDate()));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public interface EventListener {
        void onItemViewClicked(View v, ITaskExam item);
    }

    public static class GenericUtuItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView vTitle;
        protected TextView vDescription;
        protected TextView vLocation;
        protected TextView vPrice;
        protected TextView vDateStart;
        protected TextView vDateEnd;
        protected TextView vDatePay;
        protected TextView vAdditionalInfoUrl;

        public GenericUtuItemViewHolder(View itemView) {
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
