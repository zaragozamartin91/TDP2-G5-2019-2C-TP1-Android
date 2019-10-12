package com.g5.tdp2.myhealthapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.Office;
import com.g5.tdp2.myhealthapp.entity.Professional;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class ProfessionalListActivity extends ActivityWnavigation {
    public static final String PROFESSIONALS_KEY = "PROFESSIONALS";

    private List<Professional> professionals;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ProfessionalListAdapter mAdapter;

    @Override
    protected String actionBarTitle() { return "Profesionales"; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_list);

        professionals = (List<Professional>) getIntent().getSerializableExtra(PROFESSIONALS_KEY);

        recyclerView = findViewById(R.id.prof_list_rview);

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
}

class ProfessionalListAdapter extends RecyclerView.Adapter<ProfessionalListViewHolder> {
    private List<Professional> mDataset;

    ProfessionalListAdapter(List<Professional> mDataset) {
        this.mDataset = mDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProfessionalListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        CardView v = (CardView) LayoutInflater.from(context)
                .inflate(R.layout.professional_card, parent, false);

        return new ProfessionalListViewHolder(v, context);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ProfessionalListViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.setProfessional(mDataset.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
class ProfessionalListViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    private CardView cardView;
    private Context ctx;

    ProfessionalListViewHolder(CardView v, Context c) {
        super(v);
        cardView = v;
        ctx = c;
    }

    void setProfessional(Professional p) {
        ((TextView) cardView.findViewById(R.id.prof_card_name)).setText(p.getName());

        ((TextView) cardView.findViewById(R.id.prof_card_specialty))
                .setText(ctx.getString(R.string.prof_card_specialty, buildString(p.getSpecialties())));

        ((TextView) cardView.findViewById(R.id.prof_card_langs))
                .setText(ctx.getString(R.string.prof_card_langs, buildString(p.getLanguages())));

        ((TextView) cardView.findViewById(R.id.prof_card_plans))
                .setText(ctx.getString(R.string.prof_card_plans, p.getPlan()));

        ((TextView) cardView.findViewById(R.id.prof_card_emails))
                .setText(ctx.getString(R.string.prof_card_emails, buildString(p.getEmails())));

        ((TextView) cardView.findViewById(R.id.prof_card_offices))
                .setText(ctx.getString(R.string.prof_card_offices, buildStringFoffices(p.getOffices())));
    }

    private String buildString(List<String> ls) {
        StringJoiner sj = new StringJoiner(", ");
        ls.forEach(sj::add);
        return sj.toString();
    }

    private String buildStringFoffices(List<Office> offices) {
        StringJoiner sj = new StringJoiner(", ");
        offices.forEach(o -> sj.add(o.addressWphone()));
        return sj.toString();
    }
}
