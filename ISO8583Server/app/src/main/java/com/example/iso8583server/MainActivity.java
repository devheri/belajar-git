package com.example.iso8583server;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iso8583server.database.EntityIsoMessages;
import com.example.iso8583server.database.ISOmessageHistory_Adapter;
import com.example.iso8583server.database.ObjectDatabaseISO;
import com.whty.smartpos.unionpay.pay.constant.PosConfig;
import com.whty.smartpos.unionpay.pay.msgmanager.handler.DataHandler;
import com.whty.smartpos.unionpay.pay.msgmanager.handler.DataHandler2;
import com.whty.smartpos.unionpay.pay.msgmanager.model.FieldType;
import com.whty.smartpos.unionpay.pay.msgmanager.model.PatternTables;
import com.whty.smartpos.unionpay.pay.msgmanager.model.TYMsgParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    public TextView txtmonitorserver,txtdelayresponse_delay,txtdelayresponse_second;
    public TextView txtip;

    public static CheckBox check_kirim;
    public static EditText edtdelayresponse,edtde_39;

    public Button btncontrol, btnclear,btnloadhistory,btnreset;
    public static TextView txtmonitorclient;
    Server server;
    public RecyclerView rcclient;
    public ISOmessage_Adapter adapter;
    public ISOmessageHistory_Adapter adapterhistory;
    public RecyclerView.LayoutManager layoutManager;
    public ArrayList<ISOmessage_gettersetter> arrayList_iso=new ArrayList<>();
    public ArrayList<EntityIsoMessages> arrayList_iso_history=new ArrayList<>();
    public static Context contextOfApplication;






    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Inputronik Host - Base On TYPAY30");
        txtmonitorserver=findViewById(R.id.txt_monitor_server);
        check_kirim=findViewById(R.id.check_kirim_respon);
        edtdelayresponse=findViewById(R.id.edt_delayresponse);
        txtdelayresponse_delay=findViewById(R.id.txt_delayresponse_delay);
        txtdelayresponse_second=findViewById(R.id.txt_delayresponse_second);
        edtde_39=findViewById(R.id.edt_de39);
        int maxLength = 2;
        edtde_39.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});


       // txtmonitorclient=findViewById(R.id.txt_monitor_client);
        txtip=findViewById(R.id.txt_ip);
        btncontrol=findViewById(R.id.btn_control);
        btnclear=findViewById(R.id.btn_clear);
        btnloadhistory=findViewById(R.id.btn_load_data);
        btnreset=findViewById(R.id.btn_resetdata);
        contextOfApplication = getApplicationContext();

        rcclient=findViewById(R.id.rc_client);
        adapter=new ISOmessage_Adapter(arrayList_iso);
        adapterhistory=new ISOmessageHistory_Adapter(arrayList_iso_history);
        layoutManager=new LinearLayoutManager(this);

        rcclient.setAdapter(adapter);
        rcclient.setLayoutManager(layoutManager);
        rcclient.setHasFixedSize(true);

//        Toast.makeText(contextOfApplication, ""+check_kirim.isChecked(), Toast.LENGTH_SHORT).show();

        //Initialisasi DATABASE untuk LOAD History
        ObjectDatabaseISO databaseISO= Room.databaseBuilder(getApplicationContext(),
                        ObjectDatabaseISO.class,"dbIsoMessages").allowMainThreadQueries()
                .build();


        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             arrayList_iso.clear();
             arrayList_iso_history.clear();
             adapter.notifyDataSetChanged();
             adapterhistory.notifyDataSetChanged();
             Server server=new Server(null);
             server.message2=null;
             server.count2=0;
             txtmonitorserver.setText(null);
             server.message=null;
            }
        });

        btncontrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.btn_control){
                    System.exit(0);
                }
            }
        });

        btnloadhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.btn_load_data){
                    arrayList_iso_history.clear();
                    Toast.makeText(MainActivity.this, "Loading History Data", Toast.LENGTH_SHORT).show();
                    arrayList_iso_history.addAll(Arrays.asList(databaseISO.isoMessagesDAO().readDataIsoHistory()));

                    //SORT DESCENDING
                    Collections.sort(arrayList_iso_history, new Comparator<EntityIsoMessages>() {
                        @Override
                        public int compare(EntityIsoMessages entityIsoMessages, EntityIsoMessages t1) {
                            return entityIsoMessages.getId().compareTo(t1.getId());
                        }
                    });

                    rcclient.setAdapter(adapterhistory);
                    rcclient.setLayoutManager(layoutManager);
                    rcclient.setHasFixedSize(true);

                   // adapterhistory.notifyDataSetChanged();
                }
            }
        });

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //=========ALERT DIALOG===================================

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                // set title dialog
                alertDialogBuilder.setTitle("KONFIRMASI RESET");
                // set pesan dari dialog
                alertDialogBuilder
                        .setMessage("Anda yakin ingin mereset database ?")
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setPositiveButton("Yakin",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                databaseISO.isoMessagesDAO().hapusdata();
                                arrayList_iso_history.clear();
                                arrayList_iso.clear();
                                adapterhistory.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "DATA SUDAH DIRESET !", Toast.LENGTH_SHORT).show();

                            }
                        }) .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();                            }
                        });
                // membuat alert dialog dari builder
                AlertDialog alertDialog = alertDialogBuilder.create();

                // menampilkan alert dialog
                alertDialog.show();
                //==================================================================

            }
        });

        txtmonitorserver.setMovementMethod(new ScrollingMovementMethod());
//        txtmonitorclient.setMovementMethod(new ScrollingMovementMethod());

        //nyoba
//        DataHandler handler = getHandlerByKey();
//        String msgBody ="0210303801800e800000000000000002360000000002090914082900320031323334353637383930313231323334353630303335313035303333";
       // handler.parseData(new StringBuffer(msgBody));
       // String msg = TYMsgParams.TPDU + TYMsgParams.msgHead + msgBody;

        server = new Server(this);
        txtip.setText(server.getIpAddress());

        check_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check_kirim.isChecked()){
                    txtdelayresponse_delay.setVisibility(View.VISIBLE);
                    txtdelayresponse_second.setVisibility(View.VISIBLE);
                    edtdelayresponse.setVisibility(View.VISIBLE);
                }else{
                    txtdelayresponse_delay.setVisibility(View.GONE);
                    txtdelayresponse_second.setVisibility(View.GONE);
                    edtdelayresponse.setVisibility(View.GONE);
                }
            }
        });

    }

    private DataHandler getHandlerByKey() {
        // 组8583报文准备部分
        // KEY 以iso8583开头表示为类8583格式
        String key = "iso8583_MSG";
        String[] keys = new String[]{
                key
        };
        String[] paths;
        if (PosConfig.FIELD_TYPE == FieldType.FIELD_64) {
            paths = new String[]{
                    "whty/iso8583-common-64.xml"
            };
        }
        if (PosConfig.FIELD_TYPE == FieldType.FIELD_128) {
            paths = new String[]{
                    "whty/iso8583-common-128.xml"
            };
        }
        try {
            PatternTables.loadPatterns(getApplicationContext().getAssets(), keys, paths);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DataHandler(key);
    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }



    public static Boolean Kirim_Respon(){

        Boolean jawaban=check_kirim.isChecked();
        return jawaban;
    }
}