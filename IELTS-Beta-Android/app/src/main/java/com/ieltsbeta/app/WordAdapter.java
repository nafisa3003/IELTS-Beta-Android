package com.ieltsbeta.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ieltsbeta.app.R;
import com.ieltsbeta.app.Word;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private List<Word> wordList;

    public WordAdapter(List<Word> wordList) {
        this.wordList = wordList;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word_card, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Word word = wordList.get(position);

        holder.txtWord.setText(word.getWord());
        holder.txtMeaning.setText(word.getMeaning());
        holder.txtExample.setText(word.getExample());

        holder.itemView.setOnClickListener(v -> {
            if (holder.txtMeaning.getVisibility() == View.GONE) {
                holder.txtMeaning.setVisibility(View.VISIBLE);
                holder.txtExample.setVisibility(View.VISIBLE);
            } else {
                holder.txtMeaning.setVisibility(View.GONE);
                holder.txtExample.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {

        TextView txtWord, txtMeaning, txtExample;

        WordViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWord = itemView.findViewById(R.id.txtWord);
            txtMeaning = itemView.findViewById(R.id.txtMeaning);
            txtExample = itemView.findViewById(R.id.txtExample);
        }
    }
}