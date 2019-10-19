package com.g5.tdp2.myhealthapp.ui;

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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class HubActivity extends ActivityWnavigation {
    public static final String MEMBER_EXTRA = "member";
    public static final String PROF_SEARCH = "Listar profesionales";
    public static final String PROF_NEARBY = "Profesionales cercanos";
    private static final String NEW_CHECK = "Solicitar estudio";
    private static final String GET_CHECKS = "Mis estudios";
    static final String[] VIEWS = new String[]{PROF_SEARCH, PROF_NEARBY, NEW_CHECK, GET_CHECKS};

    private Member member;

    @Override
    protected boolean wMenu() { return true; }

    @Override
    protected boolean wBack() { return false; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        member = (Member) getIntent().getSerializableExtra(MEMBER_EXTRA);

        TextView title = findViewById(R.id.hub_title);
        title.setText(getString(R.string.main_hub_title, member.getFirstname()));

        ListView listView = findViewById(R.id.main_hub_list_view);
        int[] logos = new int[]{
                R.drawable.professional_logo,
                R.drawable.map_logo,
                R.drawable.newcheck_logo,
                R.drawable.md_check};
        ArrayAdapter<String> listAdapter = new HubArrayAdapter(this, VIEWS, logos);
        listView.setAdapter(listAdapter);
        List<Runnable> intents = Arrays.asList(
                () -> startActivity(
                        new Intent(this, ProfessionalSearchActivity.class)
                                .putExtra(ProfessionalSearchActivity.MEMBER_EXTRA, member)),
                () -> startActivity(
                        new Intent(this, ProfessionalMapActivity.class)
                                .putExtra(ProfessionalSearchActivity.MEMBER_EXTRA, member)),
                () -> startActivity(
                        new Intent(this, NewCheckActivity.class)
                                .putExtra(ProfessionalSearchActivity.MEMBER_EXTRA, member)),
                () -> startActivity(
                        new Intent(this, GetChecksActivity.class)
                                .putExtra(ProfessionalSearchActivity.MEMBER_EXTRA, member))
        );
        listView.setOnItemClickListener(
                (parent, view, position, id) -> intents.get(position).run());
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
