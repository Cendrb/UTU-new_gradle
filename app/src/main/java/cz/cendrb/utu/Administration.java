package cz.cendrb.utu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import cz.cendrb.utu.administrationactivities.AddEditTE;
import cz.cendrb.utu.enums.UTUType;

/**
 * Created by Cendrb on 27. 10. 2014.
 */
public class Administration extends Activity {
    Activity content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        content = this;

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.admin_layout);


    }
}
