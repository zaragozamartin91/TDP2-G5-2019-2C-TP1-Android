package com.g5.tdp2.myhealthapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

public class GetChecksActivity extends ActivityWnavigation {
    private RecyclerView recyclerView;
    private CheckListAdapter mAdapter;

    @Override
    protected String actionBarTitle() { return "Mis estudios"; }

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
            mAdapter = new CheckListAdapter(checks);
            recyclerView.setAdapter(mAdapter);
        }, err -> {
            progressBar.setVisibility(View.INVISIBLE);
            // TODO : analizar el tipo de error con un switch
            DialogHelper.INSTANCE.showNonCancelableDialogWaction(
                    this,
                    "Error al obtener historial de estudios",
                    "Ocurrio un error al obtener los estudios del usuario",
                    this::finish
            );
        });
    }
}

class CheckListAdapter extends RecyclerView.Adapter<CheckListViewHolder> {
    private List<Check> mDataset;

    CheckListAdapter(List<Check> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public CheckListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        CardView v = (CardView) LayoutInflater.from(context)
                .inflate(R.layout.check_card, parent, false);

        return new CheckListViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(CheckListViewHolder holder, int position) {
        holder.setCheck(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}


class CheckListViewHolder extends RecyclerView.ViewHolder {
    private CardView cardView;
    private Context ctx;

    CheckListViewHolder(CardView v, Context c) {
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
    }
}
