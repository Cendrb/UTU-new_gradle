package cz.cendrb.utu.showactivities;

import android.app.Activity;
import android.view.View;

import cz.cendrb.utu.generics.KanaViewHolder;
import cz.cendrb.utu.utucomponents.GenericUtuItem;

/**
 * Created by cendr_000 on 06.02.2016.
 */
public abstract class ShowViewHolder<ItemType extends GenericUtuItem> extends KanaViewHolder<ItemType> {
    public ShowViewHolder(View view, Activity activity) {
        super(view, activity);
    }
}
