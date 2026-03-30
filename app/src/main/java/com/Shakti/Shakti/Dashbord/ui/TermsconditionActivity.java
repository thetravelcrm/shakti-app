package com.Shakti.Shakti.Dashbord.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;

import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.Shakti.Shakti.R;

public class TermsconditionActivity extends AppCompatActivity {

    TextView name,description;
    String descriptionval;
    String nameval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termscondition);

        descriptionval=getIntent().getStringExtra("description");
        nameval=getIntent().getStringExtra("name");

        Getid();



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(""+nameval);
        if(nameval.equalsIgnoreCase("Contact Us"))
        {

        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void Getid() {


        description=findViewById(R.id.description);
        name=findViewById(R.id.name);


        Spanned spanned = HtmlCompat.fromHtml(descriptionval, HtmlCompat.FROM_HTML_MODE_COMPACT);
        description.setText(spanned);




//description.setText(""+ descriptionval);
        name.setText(""+ nameval);


    }
}
