package com.example.medamigo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.transition.Hold;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Models.Historico;

public class CustomAdapter extends BaseAdapter {

    private final Context context;
    private List<Historico> historicos;

    public CustomAdapter(Context context, List<Historico> historicos) {
        this.context = context;
        this.historicos = historicos;
    }

    @Override
    public int getCount() {
        return historicos.size();
    }

    @Override
    public Object getItem(int position) {
        return historicos.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HolderView holderView;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_layout_historico_items, parent, false);

            holderView = new HolderView(convertView);
            convertView.setTag(holderView);
        }
        else{
            holderView = (HolderView) convertView.getTag();
        }

        Historico hist = historicos.get(position);
        holderView.NomeRemedio.setText(hist.NomeDoRemedio);
        holderView.DataDaDose.setText(hist.DataDaDose);
        holderView.Atrasou.setText(hist.Atrasou);
        holderView.ImagemPilula.setImageResource(R.drawable.pilula);

        return convertView;
    }

    private  static class HolderView{
        private final TextView NomeRemedio;
        private final TextView DataDaDose;
        private final TextView Atrasou;
        private final ImageView ImagemPilula;

        public HolderView(View view){
            NomeRemedio = view.findViewById(R.id.textViewRemedio);
            DataDaDose = view.findViewById(R.id.textViewData);
            Atrasou = view.findViewById(R.id.textViewAtrasou);
            ImagemPilula = view.findViewById(R.id.imgPill);
        }

    }
}
