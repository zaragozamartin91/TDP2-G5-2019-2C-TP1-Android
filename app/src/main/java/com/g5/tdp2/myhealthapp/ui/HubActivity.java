package com.g5.tdp2.myhealthapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.Member;

import java.io.Serializable;

public class HubActivity extends ListActivity {
    public static final String MEMBER_EXTRA = "member";
    static final String[] MOBILE_OS =
            new String[]{"Listar profesionales", "Profesionales cercanos"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new MobileArrayAdapter(this, MOBILE_OS));

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        //get selected items
        String selectedValue = (String) getListAdapter().getItem(position);
        Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();

    }


//
//    private Member member;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_hub);
//
//        member = (Member) getIntent().getSerializableExtra(MEMBER_EXTRA);
//
//        TextView title = findViewById(R.id.hub_title);
//        title.setText(getString(R.string.main_hub_title, member.getFirstname()));
//
//
//    }
}
