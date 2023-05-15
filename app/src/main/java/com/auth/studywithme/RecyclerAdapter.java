package com.auth.studywithme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    User user;
    ArrayList<StudyRequest> studyRequests;

    ISStudyRequestRecycler srListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subject;
        TextView reason;
        TextView date;
        ISStudyRequestRecycler srListener;

        public ViewHolder(View itemView, ArrayList<StudyRequest> studyRequests, ISStudyRequestRecycler srListener) {
            super(itemView);
            this.srListener = srListener;
            this.subject = itemView.findViewById(R.id.subject);
            this.reason = itemView.findViewById(R.id.reason);
            this.date = itemView.findViewById(R.id.date);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                StudyRequest studyRequest = studyRequests.get(position);
                srListener.showStudyRequestDetails(studyRequest);
            });
        }
    }

    public RecyclerAdapter(Context context, User user, ISStudyRequestRecycler srListener) {
        this.srListener = srListener;
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
        return new ViewHolder(v, studyRequests, srListener);
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

    interface ISStudyRequestRecycler {
        void showStudyRequestDetails(StudyRequest sr);
    }

}
