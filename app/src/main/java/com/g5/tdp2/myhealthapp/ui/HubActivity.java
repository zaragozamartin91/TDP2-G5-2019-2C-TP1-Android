package com.g5.tdp2.myhealthapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.Member;

import java.io.Serializable;

public class HubActivity extends AppCompatActivity {
    public static final String MEMBER_EXTRA = "member";

    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        member = (Member) getIntent().getSerializableExtra(MEMBER_EXTRA);

        TextView title = findViewById(R.id.hub_title);
        title.setText(getString(R.string.hub_title, member.getFirstname()));


    }
}
