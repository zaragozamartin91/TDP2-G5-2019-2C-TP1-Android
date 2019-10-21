package com.g5.tdp2.myhealthapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.g5.tdp2.myhealthapp.AppState;
import com.g5.tdp2.myhealthapp.CrmBeanFactory;
import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.Check;
import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.usecase.GetChecks;
import com.g5.tdp2.myhealthapp.util.DateFormatter;
import com.g5.tdp2.myhealthapp.util.DialogHelper;

import java.util.List;

public class GetChecksActivity extends ActivityWnavigation {
    private RecyclerView recyclerView;
    private ChecksAdapter mAdapter;

    @Override
    protected String actionBarTitle() {
        return "Mis estudios";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_checks);

        recyclerView = findViewById(R.id.check_list_rview);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ProgressBar progressBar = findViewById(R.id.get_checks_progress);
        progressBar.setVisibility(View.VISIBLE);

        Member member = (Member) getIntent().getSerializableExtra(MEMBER_EXTRA);
        GetChecks usecase = CrmBeanFactory.INSTANCE.getBean(GetChecks.class);
        usecase.getChecks((int) member.getId(), checks -> {
            progressBar.setVisibility(View.INVISIBLE);
            mAdapter = new ChecksAdapter(checks);
            recyclerView.setAdapter(mAdapter);
        }, err -> {
            progressBar.setVisibility(View.INVISIBLE);
            DialogHelper.INSTANCE.showNonCancelableDialogWaction(
                    this,
                    "Error al obtener historial de estudios",
                    "Ocurrio un error al obtener los estudios del usuario",
                    this::finish
            );
        });
    }
}

class ChecksAdapter extends RecyclerView.Adapter<ChecksViewHolder> {
    private List<Check> mDataset;

    ChecksAdapter(List<Check> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public ChecksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        CardView v = (CardView) LayoutInflater.from(context)
                .inflate(R.layout.check_card, parent, false);

        return new ChecksViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(ChecksViewHolder holder, int position) {
        holder.setCheck(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}


class ChecksViewHolder extends RecyclerView.ViewHolder {
    private CardView cardView;
    private Context ctx;

    ChecksViewHolder(CardView v, Context c) {
        super(v);
        cardView = v;
        ctx = c;
    }

    void setCheck(Check p) {
        String specialty = p.translateSpecialty(AppState.INSTANCE::getSpecialty);

        ((TextView) cardView.findViewById(R.id.check_card_specialty))
                .setText(ctx.getString(R.string.check_card_specialty, specialty));

        ((TextView) cardView.findViewById(R.id.check_card_date))
                .setText(ctx.getString(R.string.check_card_date,
                        p.translateCreatedAt(DateFormatter.YYYY_MM_DD::serialize)));

        ((TextView) cardView.findViewById(R.id.check_card_update))
                .setText(ctx.getString(R.string.check_card_update,
                        p.translateUpdatedAt(DateFormatter.YYYY_MM_DD::serialize)));

        ((TextView) cardView.findViewById(R.id.check_card_status))
                .setText(ctx.getString(R.string.check_card_status, p.translateStatus()));


        cardView.setOnClickListener(v -> viewDetail(p));
    }

    private void viewDetail(Check c) {
        Toast.makeText(ctx, "Detalle de " + c.getPath(), Toast.LENGTH_SHORT).show();
        ctx.startActivity(new Intent(
                ctx, CheckDetailActivity.class).putExtra(ActivityWnavigation.CHECK_EXTRA, c));
    }
}
