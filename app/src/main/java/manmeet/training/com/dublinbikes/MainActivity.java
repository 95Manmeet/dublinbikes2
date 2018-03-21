package manmeet.training.com.dublinbikes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: From adding this we can jump from one activity to another...

        setContentView(R.layout.activity_main);
        Button enterButton = (Button) findViewById(R.id.enterButton);


        // TODO: Setting OnClick listener for enterButton............

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent myIntent = new Intent(MainActivity.this, SecondActivity.class);
                    startActivity(myIntent);
            }
        });


    }
}
