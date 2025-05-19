package com.whty.smartpos.unionpay.pay.msgmanager.handler;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;


import com.example.iso8583server.IsoParser;
import com.example.iso8583server.MainActivity;
import com.whty.smartpos.unionpay.pay.constant.PosConfig;
//import com.whty.smartpos.unionpay.pay.logtable.Sharedatasementara;
import com.whty.smartpos.unionpay.pay.msgmanager.model.FieldType;
import com.whty.smartpos.unionpay.pay.msgmanager.model.ParseElement;
import com.whty.smartpos.unionpay.pay.msgmanager.model.PatternTables;
import com.whty.smartpos.unionpay.pay.msgmanager.utils.BitMapUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataHandler {

    public static final String TAG = "DataHandler";

    Context applicationContext = MainActivity.getContextOfApplication();
    SharedPreferences sharedPref = applicationContext.getSharedPreferences("UntukRespon", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPref.edit();


    // 查询规则表的key
    private String lookupKey = null;

    public String getLookupKey() {
        return lookupKey;
    }

    public void setLookupKey(String lookupKey) {
        this.lookupKey = lookupKey;
    }

    public DataHandler(String lookupKey) {
        super();
        this.lookupKey = lookupKey;
    }

    public List<?> parseData(StringBuffer data) {
        List<?> p = PatternTables.getPattern(lookupKey);
        if (p == null)
            return null;

        List<ParseElement> list = processParse(lookupKey, data, p);

        return list;
    }

    @SuppressWarnings("unchecked")
    private List<ParseElement> processParse(String lookupKey, StringBuffer sb, List<?> p) {

        if (p == null || p.size() == 0)
            return null;

        if (lookupKey == null || lookupKey.equals(""))
            return null;

        if (sb == null || sb.equals(""))
            return null;

        List<ParseElement> parseList = new ArrayList<ParseElement>();

        ParseElement bitMap = null;
        if (lookupKey.startsWith("iso8583")) {
            for (int i = 0; i < p.size(); i++) {
                ParseElement e = (ParseElement) p.get(i);

                // 普通node结构
                if (e.getTargetClass() == null) {

                    ParseElement mirror = e.clone();
                    int len = 0;

                    if (e.getLllvar() != null && !e.getLllvar().equals("")) {
                        len = Integer.valueOf(e.getLllvar()) / 2 * 2;
                    } else {
                        String lenStr = e.getLen();
                        len = Integer.valueOf(lenStr);
                    }

                    String targetValue = sb.substring(0, Integer.valueOf(len));
                    mirror.setValue(targetValue);
                    sb = new StringBuffer(sb.substring(Integer.valueOf(len)));

                    if (e.getId() != null && Integer.valueOf(e.getId()) == 1) {
                        bitMap = mirror;
                        break;
                    }

                    parseList.add(mirror);
                } else {
                    // 复杂类型
                    try {
                        Class<?> targetClass = Class.forName(e.getTargetClass());
                        if (targetClass == null)
                            return null;
                        String parseMethodName = e.getParseMethod();
                        String parseParams = e.getParseMethodParam();

                        String[] params = parseParams.split(",");

                        Class<?>[] classArray = new Class<?>[params.length];
                        for (int c = 0; c < params.length; c++) {
                            classArray[c] = Class.forName(params[c]);
                        }

                        Method parseMethod = targetClass.getDeclaredMethod(parseMethodName,
                                classArray);
                        if (parseMethod != null) {
                            List<Object> peList = (List<Object>) parseMethod.invoke(
                                    targetClass.newInstance(), new Object[]{
                                            e, sb
                                    });
                            for (int j = 0; j < peList.size(); j++) {
                                if (j == 0) {
                                    sb = (StringBuffer) peList.get(j);
                                } else {
                                    parseList.add((ParseElement) peList.get(j));
                                }
                            }
                        }

                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }

                }
            }
        }
        // 非擎动才会执行此段代码
        if (bitMap != null) {
            int[] maps = BitMapUtils.getMessageUnit(bitMap.getValue());
            Log.d(TAG,"BITMAP YG DI DAPAT - "+bitMap.getValue().toString());


            for (int i = 0; i < maps.length; i++) {
                ParseElement e = getMessageUnit(maps[i], p);

                if (e == null) {
                    Log.d(TAG, "area" + maps[i] + " Not found ");
                    throw new NullPointerException("未知位元信息");
                } else {
//                    Log.d(TAG, "Area" + maps[i] + "turn Up");
                }

                ParseElement mirror = e.clone();

                int realLen = Integer.valueOf(e.getLen());
                int subLen = realLen / 2 * 2;
//                Log.e(TAG, "realLen: " + realLen + ", subLen: " + subLen);

                int subLenOfLength = 0;
                int valueLen = -1;
                // String llenValue = null;
                if (mirror.getLllvar() != null) {
                    subLenOfLength = Integer.valueOf(mirror.getLllvar());
                    valueLen = Integer.valueOf(sb.substring(0, subLenOfLength).toString());
//                    Log.e(TAG, "mirror.getLllvar not null subLenOfLength: " + subLenOfLength +
//                    ", valueLen: " + valueLen);
                }

                String unitValue = null;
                if (valueLen != -1) {
                    int rvalueLen = 0;
                    if (mirror.getCode().equalsIgnoreCase("binary")
                            || mirror.getCode().equalsIgnoreCase("ascii")) {
                        rvalueLen = valueLen * 2;
                    } else if (mirror.getCode().equalsIgnoreCase("bcd")) {
                        rvalueLen = (valueLen % 2 == 0 ? valueLen : valueLen + 1);
                    }
                    int l = subLenOfLength + rvalueLen;
                    unitValue = sb.substring(0, l);
                    sb = new StringBuffer(sb.substring(l));
                } else {
                    Log.e(TAG, "subLen: " + subLen);
                    unitValue = sb.substring(0, Integer.valueOf(subLen));
                    sb = new StringBuffer(sb.substring(subLen));
                }

                mirror.setValue(unitValue);
//                Log.d(TAG, mirror.getComments() != null ? mirror.getComments() + ":"
//                        + mirror.getValue() : mirror.getId() + "域" + ":" + mirror.getValue());

                Log.d(TAG,"Bit "+mirror.getId() +" isinya "+mirror.getValue()+"");
                parseList.add(mirror);

                editor.putString(mirror.getId(),mirror.getValue());
                editor.commit();

            }
        }


        return  parseList;
    }

    public static void bonkariso(){

    }


    /**
     * 获取位元信息
     *
     * @param id
     * @param p
     * @return
     */
    private ParseElement getMessageUnit(int id, List<?> p) {

        for (int i = 0; i < p.size(); i++) {
            ParseElement el = (ParseElement) p.get(i);
            if (el.getId() != null && el.getId().equals(String.valueOf(id))) {
                return el;
            }
        }

        return null;
    }

    public String buildData(Map<String, String> data) {

        List<?> p = PatternTables.getPattern(lookupKey);
        if (p == null)
            return null;

        String result = processBuild(lookupKey, p, data);

        return result;
    }

    /**
     * 构建数据格式
     *
     * @param lookupKey2
     * @param p
     * @param data
     * @return
     */
    private String processBuild(String lookupKey2, List<?> p, Map<String, String> data) {
        if (p == null || p.size() == 0)
            return null;

        if (lookupKey == null || lookupKey.equals(""))
            return null;

        if (data == null || data.size() == 0)
            return null;

        StringBuffer builder = new StringBuffer();

        if (lookupKey.startsWith("iso8583")) {
            String MSGType = "";
            String tempInfo = "";
            StringBuffer mapInfo = new StringBuffer();
            for (int i = 0; i < p.size(); i++) {
                ParseElement rule = (ParseElement) p.get(i);
                if (rule != null) {

                    // 普通规则的拼装
                    if (rule.getTargetClass() == null) {
                        String tag = rule.getTag();
                        if (tag == null || tag.equals("")) {
                            if (rule.getId() != null && !rule.getId().equals("")) {
                                tempInfo = rule.getId() + "|";
                                tag = "id_" + rule.getId();
                            } else
                                tag = null;
                        }

                        if (tag != null) {
                            String value = (String) data.get(tag);
                            if (value != null) {
                                if (tag.equals("MSGType"))
                                    MSGType = value;
                                else {
                                    mapInfo.append(tempInfo);
                                    builder.append(value);
                                }
                            }
                        }
                    } else {
                        // 复杂结构的拼装
                        try {
                            Class<?> targetClass = Class.forName(rule.getTargetClass());
                            if (targetClass == null)
                                return null;
                            String buildMethodName = rule.getBuildMethod();
                            String buildParams = rule.getBuildMethodParam();
                            String[] params = buildParams.split(",");
                            Class<?>[] classArray = new Class<?>[params.length];
                            for (int c = 0; c < params.length; c++) {
                                classArray[c] = Class.forName(params[c]);
                            }
                            Method buildMethod = targetClass.getDeclaredMethod(buildMethodName,
                                    classArray);
                            if (buildMethod != null) {
                                String ret = (String) buildMethod.invoke(targetClass.newInstance(),
                                        new Object[]{
                                                rule, data
                                        });
                                builder.append(ret);
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }

                    }
                }
            }
            if (MSGType != null && MSGType.length() != 0) {
                if (PosConfig.FIELD_TYPE == FieldType.FIELD_64) {
                    builder.insert(0, BitMapUtils.get64BitMap(mapInfo.toString()));
                }
                if (PosConfig.FIELD_TYPE == FieldType.FIELD_128) {
                    builder.insert(0, BitMapUtils.get128BitMap(mapInfo.toString()));
                }
                builder.insert(0, MSGType);
            }

        }
        // else if(lookupKey.startsWith("xml")){
        //
        // }

        return builder.toString();
    }

    /**
     * 根据id获取对应的规则
     *
     * @param p
     * @param id
     * @return
     */
    private ParseElement getPattermItem(List<?> p, String id) {
        if (p == null)
            return null;
        if (id == null)
            return null;
        for (int i = 0; i < p.size(); i++) {
            ParseElement pe = (ParseElement) p.get(i);
            if (id.equals(pe.getId()))
                return pe;
        }
        return null;
    }

}
