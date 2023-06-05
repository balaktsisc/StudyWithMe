package com.auth.studywithme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * The RecyclerAdapter class is responsible for binding StudyRequest data to RecyclerView items.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    User user;
    ArrayList<StudyRequest> studyRequests;
    StorageHandler sh;
    StudyRequest sr;
    ISStudyRequestRecycler srListener;

    /**
     * ViewHolder class holds the views for a single RecyclerView item.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subject;
        TextView reason;
        TextView date;
        CardView card;
        ISStudyRequestRecycler srListener;

        public ViewHolder(View itemView, ArrayList<StudyRequest> studyRequests, ISStudyRequestRecycler srListener) {
            super(itemView);
            this.srListener = srListener;
            this.subject = itemView.findViewById(R.id.subject);
            this.reason = itemView.findViewById(R.id.reason);
            this.date = itemView.findViewById(R.id.date);
            this.card = itemView.findViewById(R.id.card_view);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                StudyRequest studyRequest = studyRequests.get(position);
                srListener.showStudyRequestDetails(studyRequest.getId());
            });
        }
    }

    /**
     * Constructs a RecyclerAdapter for displaying study requests of a user.
     *
     * @param context the context of the adapter
     * @param user the user for whom the study requests are displayed
     * @param srListener the listener for study request item clicks
     * @param sh the storage handler for data retrieval
     */
    public RecyclerAdapter(Context context, User user, ISStudyRequestRecycler srListener, StorageHandler sh) {
        this.sh = sh;
        this.srListener = srListener;
        this.context = context;
        this.user = user;
        this.sr = null;
        this.studyRequests = new ArrayList<>();

        for (Integer srId : sh.fetchStudyRequestsOfUser(user.getId()))
            this.studyRequests.add(sh.fetchStudyRequestById(srId));
    }

    /**
     * Constructs a RecyclerAdapter for displaying study request matches.
     *
     * @param context the context of the adapter
     * @param sr the study request for which matches are displayed
     * @param srListener the listener for study request item clicks
     * @param sh the storage handler for data retrieval
     */
    public RecyclerAdapter(Context context, StudyRequest sr, ISStudyRequestRecycler srListener, StorageHandler sh) {
        this.sh = sh;
        this.sr = sr;
        this.srListener = srListener;
        this.context = context;
        this.user = sh.fetchUserById(sr.getRequestedUserId());
        this.studyRequests = new ArrayList<>();
        for (Integer srId : sh.fetchMatchesOfStudyRequest(sr.getId()))
            this.studyRequests.add(sh.fetchStudyRequestById(srId));
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
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        StudyRequest sr = studyRequests.get(position);

        if (sh.isStudyRequestMatched(sr.getId()))
            holder.card.setCardBackgroundColor(Color.parseColor("#d4ffb2"));

        if (this.sr != null && this.sr.getRequestedUserId() == sr.getRequestedUserId())
            holder.card.setCardBackgroundColor(Color.parseColor("#ececec"));

        holder.subject.setText(sr.getSubject());
        holder.reason.setText(ReasonOfStudy.getReasonName(context, sr.getReason()));
        holder.date.setText((new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(sr.getDatetime()));
    }

    @Override
    public int getItemCount() {
        return this.studyRequests.size();
    }

    /**
     * The ISStudyRequestRecycler interface provides a callback for study request item clicks.
     */
    interface ISStudyRequestRecycler {
        /**
         * Called when a study request item is clicked.
         *
         * @param srId the ID of the clicked study request
         */
        void showStudyRequestDetails(long srId);
    }

}
