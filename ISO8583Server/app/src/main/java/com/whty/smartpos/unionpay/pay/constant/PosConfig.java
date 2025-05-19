package com.whty.smartpos.unionpay.pay.constant;

import com.whty.smartpos.unionpay.pay.msgmanager.model.FieldType;

/**
 * Created by zengyahui on 2018/4/25.
 */

public class PosConfig {

    /**
     * 控制扫码库为Zxing还是天喻：0：天喻 ； 2：zxing
     */
    public static int SCAN_TYPE = 0;

    public static int SCAN_TYPE_TY = 0;

    public static int SCAN_TYPE_ZXING = 2;

    /**
     * 报文格式：64域、128域
     */
    public static final int FIELD_TYPE = FieldType.FIELD_64;

    public static final String SWITCH_OPEN = "01";
    public static final String SWITCH_CLOSED = "00";

    public static final String MSG_HEADER = "";
}
