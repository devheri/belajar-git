package com.example.iso8583server.database;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iso8583server.IsoParser;
import com.example.iso8583server.R;

import java.util.ArrayList;

public class ISOmessageHistory_Adapter extends RecyclerView.Adapter<ISOmessageHistory_Adapter.Viewholder> {
    private ArrayList<EntityIsoMessages> entityIsoMessagesArrayList;

    public ISOmessageHistory_Adapter(ArrayList<EntityIsoMessages> entityIsoMessagesArrayList) {
        this.entityIsoMessagesArrayList = entityIsoMessagesArrayList;
    }

    @Override
    public ISOmessageHistory_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View tampilan= LayoutInflater.from(parent.getContext()).inflate(R.layout.isomessage_row,parent,false);

        return new Viewholder(tampilan);
    }

    @Override
    public void onBindViewHolder(@NonNull ISOmessageHistory_Adapter.Viewholder holder, int position) {

        holder.ipclient.setText(entityIsoMessagesArrayList.get(position).getIp_client());
        holder.isomessage.setText(entityIsoMessagesArrayList.get(position).getIso_request());
        holder.isomessage.setTextColor(Color.parseColor("#034752"));
        holder.itemView.setOnClickListener(view -> {

            String PureIsoMsg=entityIsoMessagesArrayList.get(position).getIso_request().substring(14);
            String PureIsoMsgRespon=entityIsoMessagesArrayList.get(position).getIso_respon().substring(14);

            ClipboardManager clipboardManager = (ClipboardManager) view.getContext().getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("label",PureIsoMsg);
            clipboardManager.setPrimaryClip(clipData);

            Intent i=new Intent(view.getContext(), IsoParser.class);
            String TPDU=entityIsoMessagesArrayList.get(position).getIso_request().substring(4,10);
            i.putExtra("TPDU",TPDU.toString());
            i.putExtra("PUREISOMESSAGE",PureIsoMsg.toString());
            i.putExtra("PUREISOMESSAGERESPON",PureIsoMsgRespon.toString());
            i.putExtra("ISOMSGMENTAH",entityIsoMessagesArrayList.get(position).getIso_request());
            view.getContext().startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return entityIsoMessagesArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView ipclient,isomessage;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            ipclient=itemView.findViewById(R.id.txt_ipclient);
            isomessage=itemView.findViewById(R.id.txt_isomessage);

        }
    }
}
