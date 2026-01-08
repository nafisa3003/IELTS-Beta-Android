package com.ieltsbeta.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.button.MaterialButton;
import android.widget.TextView;

import java.util.List;

public class PracticeAdapter extends RecyclerView.Adapter<PracticeAdapter.ViewHolder> {

    private final List<PracticeItem> items;
    private final Context context;

    public PracticeAdapter(Context context, List<PracticeItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public PracticeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_practice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PracticeAdapter.ViewHolder holder, int position) {
        PracticeItem item = items.get(position);

        holder.txtTitle.setText(item.title);
        holder.txtDesc.setText(item.description);

        holder.btnStart.setOnClickListener(v -> {
            if (item.videoUrl != null && !item.videoUrl.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.videoUrl));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardLayout;
        TextView txtTitle, txtDesc;
        MaterialButton btnStart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardLayout = itemView.findViewById(R.id.cardLayout);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            btnStart = itemView.findViewById(R.id.btnStart);
        }
    }
}