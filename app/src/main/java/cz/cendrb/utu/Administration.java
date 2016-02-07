package cz.cendrb.utu;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

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
