package com.fortinc.signalmute;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolderImage> {

    private ArrayList<String> lista;
    private Context context;

    public Adapter(ArrayList<String> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item, parent, false);
        return new ViewHolderImage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderImage holder, int position) {

        holder.tvSena.setText(lista.get(position));

        Glide.with(context)
                .load(lista.get(position))
                .into(holder.ivSena);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class ViewHolderImage extends RecyclerView.ViewHolder {
        private ImageView ivSena;
        private TextView tvSena;
        ViewHolderImage(@NonNull View itemView) {
            super(itemView);
            ivSena = itemView.findViewById(R.id.ivSena);
            tvSena = itemView.findViewById(R.id.tvSena);
        }
    }
}
