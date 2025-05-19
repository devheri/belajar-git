package com.example.iso8583server;

import static android.content.Context.CLIPBOARD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

public class ISOmessage_Adapter extends RecyclerView.Adapter<ISOmessage_Adapter.Viewholder> {
    private ArrayList<ISOmessage_gettersetter> arrayList;

    public ISOmessage_Adapter(ArrayList<ISOmessage_gettersetter> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public ISOmessage_Adapter.Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View tampilan= LayoutInflater.from(parent.getContext()).inflate(R.layout.isomessage_row,parent,false);

        return new Viewholder(tampilan);
    }

    @Override
    public void onBindViewHolder( ISOmessage_Adapter.Viewholder holder, int position) {

        holder.ipclient.setText(arrayList.get(position).getIpclient());
        holder.isomessage.setText(arrayList.get(position).getIsomessage());
        holder.isomessage.setTextColor(Color.parseColor("#f50727")); //setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));

        holder.itemView.setOnClickListener(view -> {

            String PureIsoMsg=arrayList.get(position).getIsomessage().substring(14);
//            String PureISOMsgRespom=arrayList.get(position).get

            ClipboardManager clipboardManager = (ClipboardManager) view.getContext().getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("label",PureIsoMsg);
            clipboardManager.setPrimaryClip(clipData);

            Intent i=new Intent(view.getContext(),IsoParser.class);
            String TPDU=arrayList.get(position).getIsomessage().substring(4,10);
            i.putExtra("TPDU",TPDU.toString());
            i.putExtra("PUREISOMESSAGE",PureIsoMsg.toString());
            i.putExtra("ISOMSGMENTAH",arrayList.get(position).getIsomessage());
            view.getContext().startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView ipclient,isomessage;
        public Viewholder(View itemView) {
        super(itemView);

         ipclient=itemView.findViewById(R.id.txt_ipclient);
         isomessage=itemView.findViewById(R.id.txt_isomessage);

        }
    }
}
