package com.g5.tdp2.myhealthapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.Member;

import java.util.Optional;

public class HubActivity extends AppCompatActivity {
    public static final String MEMBER_EXTRA = "member";
    public static final String PROF_SEARCH = "Listar profesionales";
    public static final String PROF_NEARBY = "Profesionales cercanos";
    static final String[] VIEWS = new String[]{PROF_SEARCH, PROF_NEARBY};

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setListAdapter(new MobileArrayAdapter(this, VIEWS));
//
//    }
//
//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//
//        //get selected items
//        String selectedValue = (String) getListAdapter().getItem(position);
//        Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
//
//    }


    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        member = (Member) getIntent().getSerializableExtra(MEMBER_EXTRA);

        TextView title = findViewById(R.id.hub_title);
        title.setText(getString(R.string.main_hub_title, member.getFirstname()));

        ListView listView = findViewById(R.id.main_hub_list_view);
        ArrayAdapter<String> listAdapter = new HubArrayAdapter(this, VIEWS);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedValue = Optional.ofNullable(listAdapter.getItem(position)).orElse("");

            switch (selectedValue) {
                case PROF_SEARCH:
                    Intent intent = new Intent(this, ProfessionalSearchActivity.class);
                    intent.putExtra(ProfessionalSearchActivity.MEMBER_EXTRA, member);
                    startActivity(intent);
                    return;
                case PROF_NEARBY:
                default:
                    Toast.makeText(HubActivity.this, selectedValue, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

class HubArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public HubArrayAdapter(Context context, String[] values) {
        super(context, R.layout.hub_list_layout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.hub_list_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.hub_list_label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.hub_list_logo);
        textView.setText(values[position]);

        // Change icon based on name
        String s = values[position];

        System.out.println(s);

//        if (s.equals("WindowsMobile")) {
//            imageView.setImageResource(R.drawable.windowsmobile_logo);
//        } else if (s.equals("iOS")) {
//            imageView.setImageResource(R.drawable.ios_logo);
//        } else if (s.equals("Blackberry")) {
//            imageView.setImageResource(R.drawable.blackberry_logo);
//        } else {
//            imageView.setImageResource(R.drawable.android_logo);
//        }

        return rowView;
    }
}
