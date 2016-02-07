package cz.cendrb.utu.generics;

import android.app.Activity;
import android.view.View;

import cz.cendrb.utu.utucomponents.GenericUtuItem;

/**
 * Created by cendr_000 on 06.02.2016.
 */
public abstract class KanaViewHolder<ItemType extends GenericUtuItem> {

    protected View vView;
    protected Activity activity;

    protected ItemType data;

    public KanaViewHolder(View view, Activity activity) {
        vView = view;
        this.activity = activity;
    }

    public void setupUsing(ItemType item) {
        data = item;
        setupUsing();
    }

    protected abstract void setupUsing();
}
