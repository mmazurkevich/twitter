package com.example.infinity.twitter.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.infinity.twitter.R;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

/**
 * Created by infinity on 26.09.15.
 */
public class GitHubActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(new IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_chevron_left)
                .color(Color.WHITE)
                .sizeDp(24));
        ImageButton imageButton = (ImageButton) findViewById(R.id.github);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/InfinityQX70s/weather"));
                startActivity(a);
            }
        });
        TextView textView = (TextView) findViewById(R.id.github_link);
        textView.setText(Html.fromHtml("https://github.com/InfinityQX70s/weather"));
    }



}
