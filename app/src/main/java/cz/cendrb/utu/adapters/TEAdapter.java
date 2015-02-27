package cz.cendrb.utu.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;

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
public class TEAdapter extends RecyclerView.Adapter<TEAdapter.TEViewHolder> implements SwipeableItemAdapter<TEAdapter.TEViewHolder> {

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
        void onItemRemoved(int position);

        void onItemPinned(int position);

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
/*
        // set background resource
        final int swipeState = teViewHolder.getSwipeStateFlags();

        if ((swipeState & RecyclerViewSwipeManager.STATE_FLAG_IS_UPDATED) != 0) {
            int bgResId;

            if ((swipeState & RecyclerViewSwipeManager.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.ivonka;
            } else if ((swipeState & RecyclerViewSwipeManager.STATE_FLAG_SWIPING) != 0) {
                bgResId = android.R.color.white;
            } else {
                bgResId = android.R.color.white;
            }

            teViewHolder.vContainer.setBackgroundResource(bgResId);
        }*/

        //teViewHolder.setSwipeItemSlideAmount(RecyclerViewSwipeManager.OUTSIDE_OF_THE_WINDOW_LEFT);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int onGetSwipeReactionType(TEViewHolder teViewHolder, int i, int i2) {
        if (MainActivity.utuClient.isLoggedIn())
            return RecyclerViewSwipeManager.REACTION_CAN_SWIPE_BOTH;
        else
            return RecyclerViewSwipeManager.REACTION_CAN_NOT_SWIPE_BOTH;
    }

    @Override
    public void onSetSwipeBackground(TEViewHolder teViewHolder, int type) {
        int bgRes = 0;
        switch (type) {
            case RecyclerViewSwipeManager.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_neutral;
                break;
            case RecyclerViewSwipeManager.DRAWABLE_SWIPE_LEFT_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_left;
                break;
            case RecyclerViewSwipeManager.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_right;
                break;
        }

        teViewHolder.vBottomContainer.setBackgroundResource(bgRes);
    }

    @Override
    public int onSwipeItem(TEViewHolder teViewHolder, int result) {
        Log.d(TAG, "onSwipeItem(result = " + result + ")");

        switch (result) {
            // swipe right --- remove
            case RecyclerViewSwipeManager.RESULT_SWIPED_RIGHT:
                return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM;
            // swipe left -- pin
            case RecyclerViewSwipeManager.RESULT_SWIPED_LEFT:
                return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_MOVE_TO_SWIPED_DIRECTION;
            // other --- do nothing
            case RecyclerViewSwipeManager.RESULT_CANCELED:
            default:
                return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_DEFAULT;
        }
    }

    @Override
    public void onPerformAfterSwipeReaction(final TEViewHolder teViewHolder, int result, int reaction) {
        Log.d(TAG, "onPerformAfterSwipeReaction(result = " + result + ", reaction = " + reaction + ")");

        final int position = teViewHolder.getPosition();
        final ITaskExam item = data.get(position);

        if (reaction == RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM) {
            Log.d(TAG, position + " " + item.getId());

            new Hider(context, item, true, new Runnable() {
                @Override
                public void run() {
                    data.remove(item);
                    notifyDataSetChanged();
                }
            }, new Runnable() {
                @Override
                public void run() {
                    data.add(item);
                    Collections.sort(data);
                    notifyDataSetChanged();
                }
            }).execute();

            if (mEventListener != null) {
                mEventListener.onItemRemoved(position);
            }
        } else if (reaction == RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_MOVE_TO_SWIPED_DIRECTION) {
            data.remove(position);

            if (mEventListener != null) {
                mEventListener.onItemPinned(position);
            }
        } else {
        }
    }

    public static class TEViewHolder extends AbstractSwipeableItemViewHolder {
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

        @Override
        public View getSwipeableContainerView() {
            return vContainer;
        }
    }
}
