package com.example.mustafakaratas.a181220wifiapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context con;
    private ArrayList<String> arrayL;

    public MyAdapter(Context context, ArrayList<String> arrayL) {
        inflater = LayoutInflater.from(context);
        this.con = context;
        this.arrayL = arrayL;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.row,viewGroup,false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.row.setText(arrayL.get(i));
    }

    @Override
    public int getItemCount() {
        return arrayL.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView row;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            row = itemView.findViewById(R.id.tvRow);
        }
    }
}
