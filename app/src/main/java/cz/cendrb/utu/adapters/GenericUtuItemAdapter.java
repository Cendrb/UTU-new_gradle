package cz.cendrb.utu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cz.cendrb.utu.R;
import cz.cendrb.utu.utucomponents.Event;
import cz.cendrb.utu.utucomponents.GenericUtuItem;
import cz.cendrb.utu.utucomponents.TEItem;

/**
 * Created by cendr_000 on 31.01.2016.
 */
public class GenericUtuItemAdapter extends RecyclerView.Adapter<GenericUtuItemAdapter.GenericUtuItemViewHolder> {

    static String TAG = "GenericUtuItemAdapter";

    private Context context;
    private List<GenericUtuItem> data;
    private EventListener mEventListener;

    public GenericUtuItemAdapter(Context context, SparseArray<GenericUtuItem> sparseArray) {
        this.context = context;

        this.data = new ArrayList<>();
        for (int i = 0; i < sparseArray.size(); i++) {
            int key = sparseArray.keyAt(i);
            GenericUtuItem genericUtuItem = sparseArray.get(key);
            data.add(genericUtuItem);
        }

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
    public void onBindViewHolder(GenericUtuItemViewHolder genericUtuItemViewHolder, int i) {
        final GenericUtuItem item = data.get(i);

        genericUtuItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEventListener != null) {
                    mEventListener.onItemViewClicked(v, item);
                }
            }
        });

        genericUtuItemViewHolder.vTitle.setText(item.getTitle());
        if (item instanceof Event)
            genericUtuItemViewHolder.vSubjectCircle.setVisibility(View.GONE);
        else {
            genericUtuItemViewHolder.vSubjectCircle.setBackground(context.getResources().getDrawable(R.drawable.exam_circle));
            genericUtuItemViewHolder.vSubjectCircle.setText(((TEItem) item).getSubject().getName());
        }
        genericUtuItemViewHolder.vDescription.setText(item.getDescription());
        genericUtuItemViewHolder.vDate.setText(item.getDateString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface EventListener {
        void onItemViewClicked(View v, GenericUtuItem item);
    }

    public static class GenericUtuItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView vTitle;
        protected TextView vDescription;
        protected TextView vDate;
        protected TextView vSubjectCircle;

        public GenericUtuItemViewHolder(View itemView) {
            super(itemView);
            vTitle = (TextView) itemView.findViewById(R.id.gULITitle);
            vDescription = (TextView) itemView.findViewById(R.id.gULIDescription);
            vDate = (TextView) itemView.findViewById(R.id.gULIDate);
            vSubjectCircle = (TextView) itemView.findViewById(R.id.gULISubjectCircle);
        }
    }
}
