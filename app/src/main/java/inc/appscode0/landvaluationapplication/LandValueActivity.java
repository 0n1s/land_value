package inc.appscode0.landvaluationapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class LandValueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_value);
        final EditText ValueField =(EditText) findViewById(R.id.ValueField);

        Intent intent=getIntent();
        String land_vsalues=intent.getStringExtra("value");
        ValueField.setText(land_vsalues + "");
    }
}
/*<color name="Az">#379b29</color>*/