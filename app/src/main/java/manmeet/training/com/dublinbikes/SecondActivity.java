package manmeet.training.com.dublinbikes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: leads us back to the main activity............

        setContentView(R.layout.activity_second);
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        Button mapButton = (Button) findViewById(R.id.mapButton);


        //TODO: setting OnClick listener on backButton............

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent1 = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(myIntent1);
            }
        });



        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent2 = new Intent(SecondActivity.this, MapsActivity.class);
                startActivity(myIntent2);

            }
        });
    }
}
