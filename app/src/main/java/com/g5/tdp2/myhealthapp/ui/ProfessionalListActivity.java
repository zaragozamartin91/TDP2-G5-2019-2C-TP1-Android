package com.g5.tdp2.myhealthapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.Professional;

import java.util.List;
import java.util.Optional;

public class ProfessionalListActivity extends AppCompatActivity {
    public static final String PROFESSIONALS_KEY = "PROFESSIONALS";

    private List<Professional> professionals;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ProfessionalListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_list);

        Optional.ofNullable(getSupportActionBar()).ifPresent(actionBar -> {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("Profesionales");
        });

        professionals = (List<Professional>) getIntent().getSerializableExtra(PROFESSIONALS_KEY);

        recyclerView = (RecyclerView) findViewById(R.id.prof_list_rview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ProfessionalListAdapter(professionals);
        recyclerView.setAdapter(mAdapter);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Optional.ofNullable(item)
                .filter(i -> i.getItemId() == android.R.id.home)
                .ifPresent(i -> finish());

        return super.onOptionsItemSelected(item);
    }
}

class ProfessionalListAdapter extends RecyclerView.Adapter<ProfessionalListViewHolder> {
    private List<Professional> mDataset;

    ProfessionalListAdapter(List<Professional> mDataset) {
        this.mDataset = mDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProfessionalListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.professional_card, parent, false);

        // create a new view
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.my_text_view, parent, false);

        return new ProfessionalListViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ProfessionalListViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
class ProfessionalListViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public CardView cardView;

    public ProfessionalListViewHolder(CardView v) {
        super(v);
        cardView = v;
        // TODO : obtener los textViews hijos mediante v.findViewById(...)
    }
}
