package cz.cendrb.utu.administrationactivities;

import android.app.Activity;
import android.view.View;

import cz.cendrb.utu.generics.Action;
import cz.cendrb.utu.generics.KanaViewHolder;
import cz.cendrb.utu.utucomponents.GenericUtuItem;

/**
 * Created by cendr_000 on 02.02.2016.
 */
public abstract class EditViewHolder<ItemType extends GenericUtuItem> extends KanaViewHolder<ItemType> {

    protected Action<Void> onSaveClicked;

    public EditViewHolder(View view, Activity activity) {
        super(view, activity);
    }

    public void setOnSaveClicked(Action<Void> onSaveClicked) {
        this.onSaveClicked = onSaveClicked;
    }
}
