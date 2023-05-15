package com.auth.studywithme;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    User user;
    ArrayList<StudyRequest> studyRequests;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subject;
        TextView reason;
        TextView date;

        public ViewHolder(View itemView, ArrayList<StudyRequest> studyRequests) {
            super(itemView);
            subject = itemView.findViewById(R.id.subject);
            reason = itemView.findViewById(R.id.reason);
            date = itemView.findViewById(R.id.date);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                StudyRequest studyRequest = studyRequests.get(position);

                // Start the UpdateStudyRequestActivity and pass the selected study request
                Intent intent = new Intent(itemView.getContext(), UpdateStudyRequestActivity.class);
                intent.putExtra("studyRequest", studyRequest);
                itemView.getContext().startActivity(intent);

//                Snackbar.make(v, "Click detected on item " + position,
//                        Snackbar.LENGTH_LONG).show();
            });
        }
    }

    public RecyclerAdapter(Context context, User user) {
        this.context = context;
        this.user = user;
        try (StorageHandler sh = new StorageHandler(this.context,null,1)) {
            this.studyRequests = sh.fetchStudyRequestsOfUser(user);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_study_request, parent, false);
        return new ViewHolder(v, studyRequests);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        StudyRequest sr = studyRequests.get(position);
        holder.subject.setText(sr.getSubject());
        holder.reason.setText(sr.getReason());
        holder.date.setText((new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(sr.getDatetime()));
    }

    @Override
    public int getItemCount() { return this.studyRequests.size(); }
}
