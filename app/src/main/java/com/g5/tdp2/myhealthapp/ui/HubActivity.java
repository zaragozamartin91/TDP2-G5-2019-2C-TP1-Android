package com.g5.tdp2.myhealthapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class HubActivity extends AppCompatActivity {
    public static final String MEMBER_EXTRA = "member";
    public static final String PROF_SEARCH = "Listar profesionales";
    public static final String PROF_NEARBY = "Profesionales cercanos";
    private static final String NEW_CHECK = "Solicitar estudio";
    static final String[] VIEWS = new String[]{PROF_SEARCH, PROF_NEARBY, NEW_CHECK};

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
        int[] logos = new int[]{R.drawable.professional_logo, R.drawable.map_logo, R.drawable.newcheck_logo};
        ArrayAdapter<String> listAdapter = new HubArrayAdapter(this, VIEWS, logos);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedValue = Optional.ofNullable(listAdapter.getItem(position)).orElse("");

            // TODO : REEMPLAZAR ESTO POR UNA LISTA DE RUNNABLES INDEXADOS POR position
            switch (selectedValue) {
                case PROF_SEARCH:
                    Intent intent = new Intent(this, ProfessionalSearchActivity.class);
                    intent.putExtra(ProfessionalSearchActivity.MEMBER_EXTRA, member);
                    startActivity(intent);
                    return;
                case PROF_NEARBY:
                    Intent mintent = new Intent(this, ProfessionalMapActivity.class);
                    mintent.putExtra(ProfessionalSearchActivity.MEMBER_EXTRA, member);
                    startActivity(mintent);
                    return;
                case NEW_CHECK:
                    Intent ncintent = new Intent(this, NewCheckActivity.class);
                    ncintent.putExtra(ProfessionalSearchActivity.MEMBER_EXTRA, member);
                    startActivity(ncintent);
                default:
                    Toast.makeText(HubActivity.this, selectedValue, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        /* Apago el boton 'atras' */
        Log.d("CDA", "onBackPressed Called");
    }
}

class HubArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final int[] logos;

    public HubArrayAdapter(Context context, String[] values, int[] logos) {
        super(context, R.layout.hub_list_layout, values);
        this.context = context;
        this.values = values;
        this.logos = logos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.hub_list_layout, parent, false);
        TextView textView = rowView.findViewById(R.id.hub_list_label);
        ImageView imageView = rowView.findViewById(R.id.hub_list_logo);
        textView.setText(values[position]);

        // Change icon based on name
        String s = values[position];

        System.out.println(s);

        imageView.setImageResource(logos[position]);

        return rowView;
    }
}
