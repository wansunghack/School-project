package WangSeongHak.Barcode;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.examples.classification.R;

public class Foodinform extends AppCompatActivity {
    TextView textview;
    Button stopButton;
    Button stopButton2;
    Button stopButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_foodinform);

        textview = findViewById(R.id.textView3);
        stopButton = findViewById(R.id.stop);
        stopButton2 = findViewById(R.id.stop2);
        stopButton3 = findViewById(R.id.stop3);

        Intent intent = getIntent();


        String name = intent.getExtras().getString("name");
        textview.setText(name);

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=" + name + " 요리"));
                startActivity(intent);
            }
        });

        stopButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + name + " 요리"));
                startActivity(intent);
            }
        });

        stopButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=" + name + " 요리"));
                startActivity(intent);
            }
        });
    }
}
