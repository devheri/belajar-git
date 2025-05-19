package com.example.iso8583server;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iso8583server.api.Isoparsing;
import com.whty.smartpos.unionpay.pay.constant.PosConfig;
import com.whty.smartpos.unionpay.pay.msgmanager.handler.DataHandler;
import com.whty.smartpos.unionpay.pay.msgmanager.handler.DataHandler2;
import com.whty.smartpos.unionpay.pay.msgmanager.handler.MessageApi;
import com.whty.smartpos.unionpay.pay.msgmanager.model.FieldType;
import com.whty.smartpos.unionpay.pay.msgmanager.model.ParseElement;
import com.whty.smartpos.unionpay.pay.msgmanager.model.PatternTables;
import com.whty.smartpos.unionpay.pay.msgmanager.model.TYMsgParams;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IsoParser extends AppCompatActivity implements View.OnClickListener {
public TextView txtmti,txtbinary,txtde,txttpdu;
Button btnback,btncopyde,btnisorespon;
private static String ISOMSG="isomsg";
private static String ISOMSGRESPON;
private static String ISOMSGMENTAH="isomentah";
private String TPDU="TPDU";
private MessageApi parserApi;

private Context applicationContext = MainActivity.getContextOfApplication();
private SharedPreferences sharedPref = applicationContext.getSharedPreferences("DataParsing", Context.MODE_PRIVATE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iso_parser);
        txtmti=findViewById(R.id.txt_mti);
        txtbinary=findViewById(R.id.txt_binary);
        txtde=findViewById(R.id.txt_de);
        txttpdu=findViewById(R.id.txt_tpdu);
        btnback=findViewById(R.id.btn_close);
        btncopyde=findViewById(R.id.btn_copyde);
        btnisorespon=findViewById(R.id.btn_viewrespon);



        txtde.setMovementMethod(new ScrollingMovementMethod());
        btnback.setOnClickListener(this);
        btncopyde.setOnClickListener(this);
        btnisorespon.setOnClickListener(this);

        ISOMSG=getIntent().getStringExtra("PUREISOMESSAGE");
        ISOMSGRESPON=getIntent().getStringExtra("PUREISOMESSAGERESPON");
        ISOMSGMENTAH=getIntent().getStringExtra("ISOMSGMENTAH");


        TPDU=getIntent().getStringExtra("TPDU");


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title dialog
        alertDialogBuilder.setTitle("Pilih metode parsing !?");
        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Pilih Internal Parsing atau Eksternal")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Internal",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menjalankan internal parsing
                        DataHandler2 handler = getHandlerByKey();
                        handler.parseData(new StringBuffer(ISOMSG));
                        txtde.setText(sharedPref.getString("DataElement",""));
                        txtbinary.setText(sharedPref.getString("DataBitmap","")+" / "+sharedPref.getString("BinaryBitmap",""));
                        txtmti.setText(ISOMSG.substring(0, 4));
                        txttpdu.setText(ISOMSGMENTAH.substring(4, TYMsgParams.TPDU.length()));

                    }
                }) .setNegativeButton("Eksternal",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        eksternalparsing();
                        //dialog.cancel();
                    }
                });
        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();

    }


    void eksternalparsing(){
                    ApiParsingIso.serviceparsing.ParseResult("parse_action.php?data="+ISOMSG).enqueue(new Callback<ResponseParse>() {
                @Override
                public void onResponse(Call<ResponseParse> call, Response<ResponseParse> response) {
                    if(response.isSuccessful()){

                        ResponseParse responseParse= response.body();
                        txtmti.setText(responseParse.getMTI());
                        txtbinary.setText(responseParse.getBIN());
                        txtde.setText(responseParse.getDE());

                        txttpdu.setText(TPDU);
                    }
                }

                @Override
                public void onFailure(Call<ResponseParse> call, Throwable t) {
                    System.out.println("ADA KESALAHAN "+t.getLocalizedMessage());
                }
            });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_close){
            onBackPressed();
        }else if(view.getId()==R.id.btn_copyde){
            ClipboardManager clipboardManager = (ClipboardManager) view.getContext().getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("label",txtde.getText().toString());
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this, "Data Element Berhasil dicopy!", Toast.LENGTH_SHORT).show();
        }else if(view.getId()==R.id.btn_viewrespon){

            if(ISOMSGRESPON==null){
                Toast.makeText(applicationContext, "Harap Load Database Dahulu", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }else{
                DataHandler2 handler = getHandlerByKey();
                handler.parseData(new StringBuffer(ISOMSGRESPON));
                txtde.setText(sharedPref.getString("DataElement",""));
                txtbinary.setText(sharedPref.getString("DataBitmap","")+" / "+sharedPref.getString("BinaryBitmap",""));
                txtmti.setText(ISOMSG.substring(0, 4));
                txttpdu.setText(ISOMSGMENTAH.substring(4, TYMsgParams.TPDU.length()));

                btnisorespon.setVisibility(View.GONE);
            }


        }



    }

    public static class ApiParsingIso{
        private static Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://penuhcinta.com/iso8583/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        public static Isoparsing serviceparsing=retrofit.create(Isoparsing.class);


    }
    private DataHandler2 getHandlerByKey() {
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
            PatternTables.loadPatterns(this.getAssets(), keys, paths);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DataHandler2(key);
    }

}