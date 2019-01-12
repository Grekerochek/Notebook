package com.alexander.notebook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Note> data;
    private Context context;

    public Adapter(Context context, List<Note> data){
        this.context = context;
        this.data = data;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        int visibleId = data.get(position).getId()+1;
        holder.id.setText(String.valueOf(visibleId));
        holder.title.setText(data.get(position).getTitle());
        holder.container.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                context.startActivity(EditNoteActivity.newIntent(context, data.get(position).getId()));
            }
        });
    }

    public void setData(List<Note> data){
        this.data = data;
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public View container;
        public TextView id;
        public TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            id = itemView.findViewById(R.id.id);
            title = itemView.findViewById(R.id.title);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
