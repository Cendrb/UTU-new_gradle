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
import java.util.Collections;
import java.util.List;

import cz.cendrb.utu.MainActivity;
import cz.cendrb.utu.R;
import cz.cendrb.utu.backgroundtasks.Hider;
import cz.cendrb.utu.utucomponents.Exam;
import cz.cendrb.utu.utucomponents.ITaskExam;

/**
 * Created by cendr_000 on 21. 2. 2015.
 */
public class TEAdapter extends RecyclerView.Adapter<TEAdapter.TEViewHolder> {

    static String TAG = "TEAdapter";

    private Context context;

    private List<ITaskExam> data;

    public EventListener getEventListener() {
        return mEventListener;
    }

    public void setEventListener(EventListener mEventListener) {
        this.mEventListener = mEventListener;
    }

    private EventListener mEventListener;

    public TEAdapter(Context context, List<ITaskExam> data) {
        this.context = context;
        this.data = data;

        Log.d(TAG, String.valueOf(data.size()));

        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    public interface EventListener {
        void onItemViewClicked(View v, ITaskExam item);
    }

    @Override
    public TEViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.te_line_item, viewGroup, false);

        return new TEViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TEViewHolder teViewHolder, int i) {
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
        return data.size();
    }


    public static class TEViewHolder extends RecyclerView.ViewHolder {
        protected ViewGroup vContainer;
        protected ViewGroup vBottomContainer;
        protected TextView vTitle;
        protected TextView vSubjectCircle;
        protected TextView vDescription;
        protected TextView vDate;

        public TEViewHolder(View itemView) {
            super(itemView);
            vTitle = (TextView) itemView.findViewById(R.id.teTitle);
            vSubjectCircle = (TextView) itemView.findViewById(R.id.teSubjectCircle);
            vDate = (TextView) itemView.findViewById(R.id.teDate);
            vDescription = (TextView) itemView.findViewById(R.id.teDescription);
            vBottomContainer = (ViewGroup) itemView.findViewById(R.id.bottom_container);
            vContainer = (ViewGroup) itemView.findViewById(R.id.te_line_item_container);
        }
    }
}
