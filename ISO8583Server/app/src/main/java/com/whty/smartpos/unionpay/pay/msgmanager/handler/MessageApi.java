
package com.whty.smartpos.unionpay.pay.msgmanager.handler;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.iso8583server.IsoParser;
import com.example.iso8583server.R;
import com.whty.smartpos.tysmartposapi.OperationResult;
import com.whty.smartpos.tysmartposapi.ResultCode;
import com.whty.smartpos.unionpay.pay.TYPayApp;
import com.whty.smartpos.unionpay.pay.constant.PosConfig;
import com.whty.smartpos.unionpay.pay.msgmanager.model.FieldType;
import com.whty.smartpos.unionpay.pay.msgmanager.model.ParseElement;
import com.whty.smartpos.unionpay.pay.msgmanager.model.PatternTables;
import com.whty.smartpos.unionpay.pay.msgmanager.model.ReceiveType;
import com.whty.smartpos.unionpay.pay.msgmanager.model.TYMsgParams;
import com.whty.smartpos.unionpay.pay.utils.GPMethods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageApi {
    private static final String TAG = MessageApi.class.getSimpleName();
    IsoParser isoParser;

    TextView isi;


    private Context context;

    public MessageApi(@NonNull Context context) {
        if (context != null) {
            this.context = context.getApplicationContext();
        }
    }

    /**
     * 组装报文类
     *
     * @param data
     * @return
     */
    public String buildData(Map<String, String> data, String macField) {
        try {
            DataHandler handler = getHandlerByKey();
            String msgBody = handler.buildData(data);
            Log.e(TAG, "buildData macField: " + macField);
            Log.e(TAG, "buildData msgBody: " + msgBody);
            if (macField != null) {
                byte[] msgBodyBytes = GPMethods.str2bytes(msgBody);
                OperationResult or = TYPayApp.getPOSApi().calculateMac(msgBodyBytes,
                        msgBodyBytes.length <= 0xff ? (byte) 0x00 : (byte) 0x80);
                Log.e(TAG, "buildData msgBodyBytes: " + GPMethods.bytesToHexString(msgBodyBytes));
                Log.e(TAG, "or.getStatusCode(): " + or.getStatusCode());
                if (or.getStatusCode() == ResultCode.SUCCESS) {
                    String mac = or.getData();
                    Log.e(TAG, "buildData mac: " + mac);
                    msgBody += mac;
                    Log.e(TAG, "buildData msgBody: " + msgBody);
                } else {
                    Log.e(TAG, "buildData 64域 PosContext.posInstance.calculateMac error");
                    return null;
                }
            }
            handler.parseData(new StringBuffer(msgBody));
            String msg = TYMsgParams.TPDU + TYMsgParams.msgHead + msgBody;
            Log.e(TAG, "buildData msg: " + msg);

            //Heri
            Log.e(TAG, "isi TPDU: " + TYMsgParams.TPDU);
            Log.e(TAG, "isi msgHead: " + TYMsgParams.msgHead);
            Log.e(TAG, "isi msgBody: " + msgBody);


            String len = Integer.toHexString(Integer.valueOf(msg.length() / 2));
            len = GPMethods.paddingLeft0(len, 2);
            // 拼装最后可以发送的报文
            String wholeMSG = len + msg;
            Log.e(TAG, "buildData wholeMSG: " + wholeMSG);
            return wholeMSG;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public HashMap<String, String> parseData(String dataRece) {

        try {

            DataHandler handler = getHandlerByKey();
            HashMap<String, String> resData = new HashMap<String, String>();
            if (TYMsgParams.receiveType == ReceiveType.TYPE_8583) {
                // 8583格式的报文
                resData.put("TPDU", dataRece.substring(4, TYMsgParams.TPDU.length()));
                resData.put(
                        "msgHead",
                        dataRece.substring(4 + TYMsgParams.TPDU.length(),
                                4 + TYMsgParams.TPDU.length() + TYMsgParams.msgHead.length()));
                String msgBody = dataRece.substring(4 + TYMsgParams.TPDU.length()
                        + TYMsgParams.msgHead.length());
                resData.put("msgBody", msgBody.substring(0, msgBody.length() - 16));
                resData.put("msgType", msgBody.substring(0, 4));

                //Heri
                Log.d("TAG","TPDU adalah : "+dataRece.substring(4, TYMsgParams.TPDU.length()));
                Log.d("TAG","MTI adalah : "+msgBody.substring(0, 4));
                Log.d("TAG","msgBody adalah : "+msgBody.substring(0, msgBody.length() - 16));



                List<?> list = handler.parseData(new StringBuffer(msgBody));
                for (int i = 0; i < list.size(); i++) {
                    String nodeId = ((ParseElement) list.get(i)).getId();
                    String nodeValue = ((ParseElement) list.get(i)).getValue();
                    resData.put("" + nodeId, nodeValue);

                }
                return resData;
            } else if (TYMsgParams.receiveType == ReceiveType.TYPE_JASON) {
                // jason格式的报文
            } else if (TYMsgParams.receiveType == ReceiveType.TYPE_XML) {
                // xml格式的报文
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         return null;
    }

    // 报文组装
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
            PatternTables.loadPatterns(context.getAssets(), keys, paths);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DataHandler(key);
    }

}
