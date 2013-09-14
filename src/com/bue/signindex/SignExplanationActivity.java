package com.bue.signindex;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class SignExplanationActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_explanation);
        Bundle bundle = this.getIntent().getExtras();
        int a=bundle.getInt("icon");
        ImageView image1=(ImageView)this.findViewById(R.id.imageView1);
        image1.setImageResource(a); 
        String param1 = bundle.getString("gr");
        TextView text1=(TextView)this.findViewById(R.id.textView1);
        text1.setText(param1);
       /* String param2 = bundle.getString("eng");
        TextView text2=(TextView)this.findViewById(R.id.textView2);
        text2.setText(param2);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_sign_explanation, menu);
        return true;
    }
}
