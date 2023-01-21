package com.example.dictionary.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.R;

import java.util.ArrayList;
import java.util.List;

public class WordsAdapter  extends RecyclerView.Adapter<WordsAdapter.ViewHolder> {

    private ArrayList<String> words = new ArrayList<>();
    private ItemSelectedListener itemSelectedListener;

    public WordsAdapter(ItemSelectedListener myItemSelectedListener){
        this.itemSelectedListener = myItemSelectedListener;
    }

    public void addAll(List<String> wordsFromAdapter){
        words.clear();
        words.addAll(wordsFromAdapter);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public WordsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_dic_recycler, parent, false);

        return new WordsAdapter.ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull WordsAdapter.ViewHolder holder, int position) {
        holder.item1.setText(words.get(position));
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public interface ItemSelectedListener{
        void onItemSelected(String selected);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView item1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item1 = itemView.findViewById(R.id.word_recycler_item1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemSelectedListener != null) {
                        itemSelectedListener.onItemSelected(words.get(getAdapterPosition()));
                    }
                }
            });

        }

    }
}
