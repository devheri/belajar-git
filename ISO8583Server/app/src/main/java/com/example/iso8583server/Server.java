package com.example.iso8583server;

import static com.whty.smartpos.unionpay.pay.utils.GPMethods.str2HexStr;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.room.Room;

import com.example.iso8583server.database.EntityIsoMessages;
import com.example.iso8583server.database.ObjectDatabaseISO;
import com.whty.smartpos.unionpay.pay.constant.PosConfig;
import com.whty.smartpos.unionpay.pay.msgmanager.handler.DataHandler;
import com.whty.smartpos.unionpay.pay.msgmanager.handler.MessageApi;
import com.whty.smartpos.unionpay.pay.msgmanager.model.FieldType;
import com.whty.smartpos.unionpay.pay.msgmanager.model.PatternTables;
import com.whty.smartpos.unionpay.pay.msgmanager.model.TYMsgParams;
import com.whty.smartpos.unionpay.pay.utils.GPMethods;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
    MainActivity activity;
    ServerSocket serverSocket;
   public String message,message2 = "";
    static final int socketServerPORT = 7000;
    public int count2 = 0;
    private MessageApi parserApi,getParserApi_client;

    Context applicationContext = MainActivity.getContextOfApplication();


    public Server(MainActivity activity) {
        this.activity = activity;
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public int getPort() {
        return socketServerPORT;
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread extends Thread {

        int count = 0;

        @Override
        public void run() {


            try {
                // create ServerSocket using specified port

                serverSocket = new ServerSocket(socketServerPORT);

                while (true) {
                    // block the call until connection is created and return
                    // Socket object
                    Socket socket = serverSocket.accept();

                    if(message!=null){
                        message += "Koneksi Baru"  + " dari "
                                + socket.getInetAddress() + ":"
                                + socket.getPort() + "\n";

                    }else {
                        message = "Koneksi Baru"  + " dari "
                                + socket.getInetAddress() + ":"
                                + socket.getPort() + "\n";
                    }


                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.txtmonitorserver.setText(message);

                        }
                    });

//                    SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(socket);
//                    socketServerReplyThread.run();




                     String isi_delayrespon=MainActivity.edtdelayresponse.getText().toString();
                     if(TextUtils.isEmpty(isi_delayrespon)){
                         isi_delayrespon="1";//default adalah 1 detik
                     }

                     int delay_response=Integer.parseInt(isi_delayrespon);
                      Timer t = new Timer();
                      Task t1 = new Task(socket);

                    if(MainActivity.Kirim_Respon()==true){  //jika Check box kirim respon di check maka,
                        t.schedule(t1, delay_response*1000); //kirim respon dengan delay
                    }else{
                        SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(socket);
                        socketServerReplyThread.run();
                    }


                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    class Task extends TimerTask {
    Socket socket;

        public Task(Socket socket) {
            this.socket = socket;
        }
        public void run() {
           //execute
            SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(socket);
            socketServerReplyThread.run();
        }
    }


    private class SocketServerReplyThread extends Thread {
        private Socket hostThreadSocket;
        SocketServerReplyThread(Socket socket) {
            hostThreadSocket = socket;
        }

        ObjectDatabaseISO databaseISO= Room.databaseBuilder(applicationContext,
        ObjectDatabaseISO.class,"dbIsoMessages")
                .build();

        @Override
        public void run() {
            count2++;
            DataOutputStream output=null;
            DataInputStream dataInputStream=null;



            String requestData = "";
            String sampleMessage = "007860000005010210303801800e80000800000000000250000000000208452607110032003132333435363738393031323636363636363030333531303530333300241f031531303030303031303030303030313235303030303000215858585858585858583030303130303030303030300006aa0103564f4e0c950500800010009a031508189c01009f02060000000010005f2a02064382023c009f1a0206439f03060000000000009f3303e0f0c89f34034403029f3501229f1e0835313230323831358407a00000000310109f41030000565f340101";

            try {

                InputStream inFromServer = hostThreadSocket.getInputStream();
                dataInputStream = new DataInputStream(inFromServer);
                OutputStream outToServer = hostThreadSocket.getOutputStream();
                output = new DataOutputStream(outToServer);


                //Membaca Data yg dikirim EDC
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte buffer[] = new byte[4096];
                baos.write(buffer, 0 , dataInputStream.read(buffer));
                byte result[] = baos.toByteArray();
                //message2 +=(bytesToHexString(result))+ "\n" +"===========================\n";
                message2 =(bytesToHexString(result));
                System.out.println("Data dari Client : "+message2.toString());
                baos.flush();
                baos.reset();

                System.out.println("MULAI PARSING DATA DARI CLIENT EDC");
                getParserApi_client = new MessageApi(activity.getApplicationContext());
                String coba = String.valueOf(getParserApi_client.parseData(message2.toString()));
                System.out.println("STOP PARSING  DATA DARI CLIENT EDC");

                String dataRece=message2.toString();
                String msgBody = dataRece.substring(4 + TYMsgParams.TPDU.length()
                        + TYMsgParams.msgHead.length());

                System.out.println("COBA ADALAH " +msgBody);
                System.out.println("MTI REQUEST ADALAH " +msgBody.substring(0, 4));





                SharedPreferences sharedPref = applicationContext.getSharedPreferences("UntukRespon", Context.MODE_PRIVATE);
//
//                //RESPON BITMAP
//                int[] fields_Sale_respon=new int[]{3,4,11,12,13,24,25,37,38,39,41};
//                int[] fields_QRgenerate_respon=new int[]{3,11,12,13,24,37,38,39,41,63};

                //SET TANGGAL dan JAM
                SimpleDateFormat sdf2=new SimpleDateFormat("HHmmss");
                String jam=sdf2.format(new Date());
                SimpleDateFormat sdf=new SimpleDateFormat("MMdd");
                String tanggal=sdf.format(new Date());

                String processing_code=sharedPref.getString("3","");
                String msg_MTI=msgBody.substring(0, 4);
                Map<String, String> msgData = new HashMap<>();

                Random rand = new Random();
                int rand_int1 = rand.nextInt(1000000);

                String isi_de39=MainActivity.edtde_39.getText().toString();
                String de39 = GPMethods.str2HexStr(isi_de39);

                String de63="000201010212041553919900000002926710019ID.CO.CIMBNIAGA.WWW011893600022100000000102150000080000000180303UMI51440014ID.CO.QRIS.WWW0215ID10200196565260303UMI52045812530336054130000012113.005802ID5917SIGMA ONLINE SHOP6010TGR BANTEN61051522562220506"+rand_int1+"0708711111116304FB53";
               // String de63="000201010212041553919900000002926710019ID.CO.CIMBNIAGA.WWW011893600022100000000102150000080000000180303UMI51440014ID.CO.QRIS.WWW0215ID10200196565260303UMI52045812530336054130000012113.005802ID5917SIGMA ONLINE SHOP6010TGR BANTEN610515225622205069999990708711111116304FB53";
                String de63checkpayment="REF-ID3001104135APCODEXXXXXXXXXXXXXXXXXXXXCREDIT CARD         601900010002000398 12345678   ANDI ARIF SADELI              356336******1661   SHOPEE-PAY                              JCB ISSUING CREDIT CARD       BANK CIMB NIAGA               ";
                String de63refund="1234561307153430123456123456789012601900010002000398 BANK CIMB NIAGA               356336******1661   ANDI ARIF SADELI              SHOPEE-PAY                    SAVING ACCOUNT      ";
                String de63cashout="000201010212041553919900000002926710019ID.CO.CIMBNIAGA.WWW011893600022100000000102150000080000000180303UMI51440014ID.CO.QRIS.WWW0215ID10200196565260303UMI52045812530336054130000012113.005802ID5917SIGMA ONLINE SHOP6010TGR BANTEN610515225622205060000360708711111116304FB53";
                String de63cashin_confimr="000035000055692748VISA ISSUING CREDIT CARD                4012666666661661   01CREDIT CARD         TJEN EKA PURNAMA              1234567890123456789012345678901234567890123456789X000000250000";
                String de63cashin_next="000035000055692748VISA ISSUING CREDIT CARD                4012666666661661   01CREDIT CARD         TJEN EKA PURNAMA              PEMBAYARAN CICILAN RUMAH A/N ANDI ARIF SADELI     000000330000CIMB NIAGA                              4012888888881881   TOKO ABADI MAJU MUNDUR JAYA   BANDUNG        0230607150120";
                String de63qrreport="YYMMDDHHMMSS000023000000001250000000010000000001500000000005000000010000000000000000000000000000";
                String de63cardrefund="aa0103564f4e";
                String de63cardVer="aa0103564f4e";
                String de63offline="aa0103564f4e";
                String de63PaymentDebit2 = "REF-ID3001104135APCODEXXXXXXXXXXXXXXXXXXXXSAVING ACCOUNT     6019000100020 00398 ANDI ARIF SADELI             356336******1661  SHOPFE-PAY                                BANK CIMB NIAGA ";
                String de63PaymentDebit  = "REF-ID3001104135APCODEXXXXXXXXXXXXXXXXXXXXSAVING ACCOUNT      601900010002000398 ANDI ARIF SADELI              356336******1661   SHOPEE-PAY                              BANK CIMB NIAGA               ";

                if(processing_code.equals("955500") ){ //qr generate

                    msgData.put("MSGType", "0210");
                    msgData.put("id_3",sharedPref.getString("3",""));
                    msgData.put("id_11",sharedPref.getString("11",""));
                    msgData.put("id_12",jam.toString());
                    msgData.put("id_13",tanggal.toString());
                    msgData.put("id_24","0032");
                    msgData.put("id_37","313233343536373839303132");
                    msgData.put("id_38","313233343536");
                    msgData.put("id_39",de39.toString());
                    msgData.put("id_41",sharedPref.getString("41","")); //khusus buat QR Generation



                    String predata=str2HexStr((String)de63);

                    String len=String.valueOf(de63.length());
                    String data= "0" + len + predata; //PANJANG de63 dalam HEX (2digit = 8 bit)
                    msgData.put("id_63",data);


                 }else if(processing_code.equals("955555")){  //REFUND

                    msgData.put("MSGType", "0210");
                    msgData.put("id_3",sharedPref.getString("3",""));
                    msgData.put("id_4",sharedPref.getString("4",""));
                    msgData.put("id_11",sharedPref.getString("11",""));
                    msgData.put("id_12",jam.toString());
                    msgData.put("id_13",tanggal.toString());
                    msgData.put("id_24","0019");
                    msgData.put("id_25","05");// UNTUK REFUND
                    msgData.put("id_37","313233343536373839303132");
                    msgData.put("id_38","363534333231");
                    msgData.put("id_39",de39.toString());
                    msgData.put("id_41",sharedPref.getString("41",""));

                    String predata=str2HexStr((String)de63refund);
                    String len=String.valueOf(de63refund.length());
                    String data= "0" + len + predata; //PANJANG de63 dalam HEX (2digit = 8 bit)
                    msgData.put("id_63",data);

                } else if( processing_code.equals("956600")){ //check payment
                    msgData.put("MSGType", "0210");
                    msgData.put("id_3",sharedPref.getString("3",""));
                    msgData.put("id_4",sharedPref.getString("4",""));
                    msgData.put("id_11",sharedPref.getString("11",""));
                    msgData.put("id_12",jam.toString());
                    msgData.put("id_13",tanggal.toString());
                    msgData.put("id_24","0019");
                    msgData.put("id_37","313233343536373839303132");
                    msgData.put("id_38","363534333231");
                    msgData.put("id_39",de39.toString());
                    msgData.put("id_41",sharedPref.getString("41",""));


                    String predata=str2HexStr((String)de63checkpayment);

                    String len=String.valueOf(de63checkpayment.length());
                    String data= "0" + len + predata; //PANJANG de63 dalam HEX (2digit = 8 bit)
                    msgData.put("id_63",data);

                }else if(processing_code.equals("955600")){//CASH OUT

                    msgData.put("MSGType", "0210");
                    msgData.put("id_3",sharedPref.getString("3",""));
                    msgData.put("id_11",sharedPref.getString("11",""));
                    msgData.put("id_12",jam.toString());
                    msgData.put("id_13",tanggal.toString());
                    msgData.put("id_24","0019");
                    msgData.put("id_37","313233343536373839303132");
                    msgData.put("id_38","363534333231");
                    msgData.put("id_39",de39.toString());
                    msgData.put("id_41",sharedPref.getString("41",""));

                    String predata=str2HexStr((String)de63cashout);
                    String len=String.valueOf(de63cashout.length());
                    String data= "0" + len + predata; //PANJANG de63 dalam HEX (2digit = 8 bit)
                    msgData.put("id_63",data);

                }else if(processing_code.equals("795000")){//CASH IN CONFIRM

                    msgData.put("MSGType", "0210");
                    msgData.put("id_3",sharedPref.getString("3",""));
                    msgData.put("id_11",sharedPref.getString("11",""));
                    msgData.put("id_12",jam.toString());
                    msgData.put("id_13",tanggal.toString());
                    msgData.put("id_24","0019");
                    msgData.put("id_37","313233343536373839303132");
                    msgData.put("id_38","363534333231");
                    msgData.put("id_39",de39.toString());
                    msgData.put("id_41",sharedPref.getString("41",""));

                    String predata=str2HexStr((String)de63cashin_confimr);
                    String len=String.valueOf(de63cashin_confimr.length());

                    String panjang_string = GPMethods.int2String(Integer.parseInt(len), 4);
                    String data= panjang_string.toString() + predata; //PANJANG de63 dalam HEX (2digit = 8 bit)
                    msgData.put("id_63",data);
                }else if(processing_code.equals("790050")){//CASH IN NEXT

                    msgData.put("MSGType", "0210");
                    msgData.put("id_3",sharedPref.getString("3",""));
                    msgData.put("id_11",sharedPref.getString("11",""));
                    msgData.put("id_12",jam.toString());
                    msgData.put("id_13",tanggal.toString());
                    msgData.put("id_24","0019");
                    msgData.put("id_25","05");
                    msgData.put("id_37","313233343536373839303132");
                    msgData.put("id_38","363534333231");
                    msgData.put("id_39",de39.toString());
                    msgData.put("id_41",sharedPref.getString("41",""));

                    String predata=str2HexStr((String)de63cashin_next);
                    String len=String.valueOf(de63cashin_next.length());

                    String panjang_string = GPMethods.int2String(Integer.parseInt(len), 4);
                    String data= panjang_string.toString() + predata; //PANJANG de63 dalam HEX (2digit = 8 bit)
                    msgData.put("id_63",data);
                }else if(processing_code.equals("957700")) { //QR REPORT

                    msgData.put("MSGType", "0210");
                    msgData.put("id_3", sharedPref.getString("3", ""));
                    msgData.put("id_11", sharedPref.getString("11", ""));
                    msgData.put("id_12", jam.toString());
                    msgData.put("id_13", tanggal.toString());
                    msgData.put("id_24", "0019");
                    msgData.put("id_25", "05");
                    msgData.put("id_37", "313233343536373839303132");
                    msgData.put("id_38", "363534333231");
                    msgData.put("id_39", de39.toString());
                    msgData.put("id_41", sharedPref.getString("41",""));


                    String id54="000000500000";
                    String predata54=str2HexStr((String) id54);
                    String len54=String.valueOf(id54.length());
                    String panjang_string54 = GPMethods.int2String(Integer.parseInt(len54), 4);
                    String data54=panjang_string54+predata54;

                    msgData.put("id_54", data54);

                    System.out.println("ISI 54 ADALAH :" +data54);

                    String predata = str2HexStr((String) de63qrreport);
                    String len = String.valueOf(de63qrreport.length());
                    String panjang_string = GPMethods.int2String(Integer.parseInt(len), 4);
                    String data = panjang_string.toString() + predata; //PANJANG de63 dalam HEX (2digit = 8 bit)

                    msgData.put("id_63", data);
                }else if(processing_code.equals("790000")) {//QR Payment Debit

                    msgData.put("MSGType", "0210");
                    msgData.put("id_3", sharedPref.getString("3", ""));
                    msgData.put("id_4",sharedPref.getString("4",""));
                    msgData.put("id_11", sharedPref.getString("11", ""));
                    msgData.put("id_12", jam.toString());
                    msgData.put("id_13", tanggal.toString());
                    msgData.put("id_24",sharedPref.getString("24",""));
                    msgData.put("id_25", "05");
                    msgData.put("id_37", "313233343536373839303132");
                    msgData.put("id_38", "363534333231");
                    msgData.put("id_39", de39.toString());
                    msgData.put("id_41", sharedPref.getString("41", ""));

                    String predata = str2HexStr((String) de63PaymentDebit);
                    String len = String.valueOf(de63PaymentDebit.length());

                    String panjang_string = GPMethods.int2String(Integer.parseInt(len), 4);
                    String data = panjang_string.toString() + predata; //PANJANG de63 dalam HEX (2digit = 8 bit)
                    msgData.put("id_63", data);
                }

                else if(processing_code.equals("200000")) { // CARD REFUND
                    msgData.put("MSGType", "0210");
                    msgData.put("id_3", sharedPref.getString("3", ""));
                    msgData.put("id_4", sharedPref.getString("4", ""));
                    msgData.put("id_11", sharedPref.getString("11", ""));
                    msgData.put("id_12", jam.toString());
                    msgData.put("id_13", tanggal.toString());
                    msgData.put("id_24", "0019");
                    msgData.put("id_37", "313233343536373839303132");
                    msgData.put("id_38", "363534333231");
                    msgData.put("id_39", de39.toString());
                    msgData.put("id_41",sharedPref.getString("41",""));

                    //===================build DE63====================
                    String predata = str2HexStr((String) de63cardrefund);
                    String len = String.valueOf(de63cardrefund.length());
                    String panjang_string = GPMethods.int2String(Integer.parseInt(len), 4);
                    String data = panjang_string.toString() + predata; //PANJANG de63 dalam HEX (2digit = 8 bit)
                    //=======================================================

                    msgData.put("id_63", data);
                }else if(processing_code.equals("380000")) { // CARD VER
                    msgData.put("MSGType", "0110");
                    msgData.put("id_3", sharedPref.getString("3", ""));
                    msgData.put("id_4", sharedPref.getString("4", ""));
                    msgData.put("id_11", sharedPref.getString("11", ""));
                    msgData.put("id_12", jam.toString());
                    msgData.put("id_13", tanggal.toString());
                    msgData.put("id_24", "0019");
                    msgData.put("id_25", "00");
                    msgData.put("id_37", "313233343536373839303132");
                    msgData.put("id_38", "363534333231");
                    msgData.put("id_39", de39.toString());
                    msgData.put("id_41",sharedPref.getString("41",""));

                    //===================build DE63====================
                    String predata = str2HexStr((String) de63cardVer);
                    String len = String.valueOf(de63cardVer.length());
                    String panjang_string = GPMethods.int2String(Integer.parseInt(len), 4);
                    String data = panjang_string.toString() + predata; //PANJANG de63 dalam HEX (2digit = 8 bit)
                    //=======================================================
                    msgData.put("id_63", data);
                }else if(msg_MTI.equals("0400")){  //REVERSAL 400

                    msgData.put("MSGType", "0410");
                    msgData.put("id_3",sharedPref.getString("3",""));
                    msgData.put("id_4",sharedPref.getString("4",""));
                    msgData.put("id_11",sharedPref.getString("11",""));
                    msgData.put("id_12",jam.toString());
                    msgData.put("id_13",tanggal.toString());
                    msgData.put("id_24","0019");
                    msgData.put("id_25","05");
                    msgData.put("id_37","313233343536373839303132");
                    msgData.put("id_39",de39.toString());
                    msgData.put("id_41",sharedPref.getString("41",""));
                    msgData.put("id_61",sharedPref.getString("61",""));

                }else if(msg_MTI.equals("0100")){  //OFFLINE

                    msgData.put("MSGType", "0110");
                    msgData.put("id_3",sharedPref.getString("3",""));
                    msgData.put("id_4",sharedPref.getString("4",""));
                    msgData.put("id_11",sharedPref.getString("11",""));
                    msgData.put("id_12",jam.toString());
                    msgData.put("id_13",tanggal.toString());
                    msgData.put("id_24","0019");
                    msgData.put("id_25","00");
                    msgData.put("id_37","313233343536373839303132");
                    msgData.put("id_38", "363534333231");
                    msgData.put("id_39",de39.toString());
                    msgData.put("id_41",sharedPref.getString("41",""));

//
//                    //===================build DE63====================
////
//                    String predata = str2HexStr((String) de63offline);
//                    String len = String.valueOf(de63offline.length());
//                    String panjang_string = GPMethods.int2String(Integer.parseInt(len), 4);
//                    String data = panjang_string.toString() + predata; //PANJANG de63 dalam HEX (2digit = 8 bit)
//                    //=======================================================
//                    msgData.put("id_63", data);


                    String predata=str2HexStr((String)de63offline);
                    String len=String.valueOf(de63offline.length());
                    String panjang_string = GPMethods.int2String(Integer.parseInt(len), 4);
                    String data= panjang_string.toString() +predata; //PANJANG de63 dalam HEX (2digit = 8 bit)
                    msgData.put("id_63",data);


                }else if(processing_code.equals("791000")) {//NFC QR Payment Debit

                    msgData.put("MSGType", "0210");
                    msgData.put("id_3", sharedPref.getString("3", ""));
                    msgData.put("id_4",sharedPref.getString("4",""));
                    msgData.put("id_11", sharedPref.getString("11", ""));
                    msgData.put("id_12", jam.toString());
                    msgData.put("id_13", tanggal.toString());
                    msgData.put("id_24",sharedPref.getString("24",""));
                    msgData.put("id_25", "05");
                    msgData.put("id_37", "313233343536373839303132");
                    msgData.put("id_38", "363534333231");
                    msgData.put("id_39", de39.toString());
                    msgData.put("id_41", sharedPref.getString("41", ""));

                    String predata = str2HexStr((String) de63PaymentDebit);
                    String len = String.valueOf(de63PaymentDebit.length());

                    String panjang_string = GPMethods.int2String(Integer.parseInt(len), 4);
                    String data = panjang_string.toString() + predata; //PANJANG de63 dalam HEX (2digit = 8 bit)
                    msgData.put("id_63", data);
                }

                else  {  //Sale Biasa

                    msgData.put("MSGType", "0210");
                    msgData.put("id_3",sharedPref.getString("3",""));
                    msgData.put("id_4",sharedPref.getString("4",""));// tidak dibutuhkna saat qr generation
                    msgData.put("id_11",sharedPref.getString("11",""));
                    msgData.put("id_12",jam.toString());
                    msgData.put("id_13",tanggal.toString());
                    msgData.put("id_24","0019");
                    msgData.put("id_25","00");// tidak dibutuhkna saat qr generation
                    msgData.put("id_37","313233343536373839303132");
                    msgData.put("id_38","313233343536");
                    msgData.put("id_39",de39.toString());
                    msgData.put("id_41",sharedPref.getString("41",""));
                    msgData.put("id_61",sharedPref.getString("61",""));
                }

                parserApi = new MessageApi(activity.getApplicationContext());
                String rspServer=parserApi.buildData(msgData,null);

                byte [] respon=hexToASCII(rspServer).getBytes("ISO-8859-1");

                if(MainActivity.Kirim_Respon()==true){
                    output.write(respon);
                    System.out.println("Data Dikirim ke EDC : "+bytesToHexString(respon));
                    output.flush();

                }


                //memasukan kedalam DATABASE
                EntityIsoMessages entityIsoMessages=new EntityIsoMessages();
                entityIsoMessages.setIp_client(String.valueOf(hostThreadSocket.getInetAddress()));
                entityIsoMessages.setIso_request(dataRece);
                entityIsoMessages.setIso_respon(rspServer);
                databaseISO.isoMessagesDAO().insertISOMessages(entityIsoMessages);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       activity.arrayList_iso.add(new ISOmessage_gettersetter(count2+" - "+hostThreadSocket.getInetAddress().toString(),message2));
                        activity.rcclient.setAdapter(activity.adapter);
                        activity.rcclient.setLayoutManager(activity.layoutManager);
                        activity.rcclient.setHasFixedSize(true);
                       activity.adapter.notifyDataSetChanged();
                    }
                });


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                message += "Something wrong! " +hostThreadSocket.isConnected()+ e.toString() + "\n";
            }finally {
                try {
                    if (output != null) {
                        output.close();
                    }
                    if (dataInputStream!= null) {
                        dataInputStream.close();
                    }
                    if (hostThreadSocket != null) {
                        hostThreadSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.txtmonitorserver.setText(message);
                }
            });

            databaseISO.close();//menutup database sementara..untuk menunggu koneksi berikutnya masuk
        }

    }




    public String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress
                            .nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "  "
                                + inetAddress.getHostAddress().toString()+"";
                    }
                }
            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }

    private static String hexToASCII(String hexValue)
    {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexValue.length(); i += 2)
        {
            String str = hexValue.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }


    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuffer buff = new StringBuffer();
        int len = bytes.length;
        for (int j = 0; j < len; j++) {
            if ((bytes[j] & 0xff) < 16) {
                buff.append('0');
            }
            buff.append(Integer.toHexString(bytes[j] & 0xff));
        }
        return buff.toString();
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
            PatternTables.loadPatterns(activity.getApplicationContext().getAssets(), keys, paths);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DataHandler(key);
    }

    public static String binaryStrToHexString(String str) {
        StringBuffer sb = new StringBuffer();
        if (str.length() % 16 != 0) {
            System.out.println("输入参数的长度不是16的整数倍");
            return null;
        } else {
            for (int i = 0; i < str.length(); i += 8) {
                String part = "";
                part = Integer.toHexString(Integer.parseInt(str.substring(i, i + 8), 2));
                while (part.length() < 2) {
                    part = '0' + part;
                }
                sb.append(part);
            }
            return sb.toString();
        }
    }

    public static String stringToBinaryStr(String str) {
        if (str.length() % 2 != 0) {
            System.out.println("传入的字符串长度非偶数");
            return null;
        } else {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < str.length(); i += 2) {
                String string = str.substring(i, i + 2);
                String part = Integer.toBinaryString(Integer.parseInt(string, 16));
                while (part.length() < 8) {
                    part = '0' + part;
                }
                sb.append(part);
            }

            return sb.toString().toUpperCase();
        }
    }

}