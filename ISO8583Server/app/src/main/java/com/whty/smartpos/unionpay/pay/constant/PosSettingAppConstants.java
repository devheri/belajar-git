package com.whty.smartpos.unionpay.pay.constant;

public class PosSettingAppConstants {

    public static String saleCancelCard;
    public static String authDoneCancelCard;
    public static String saleCancelPin;
    public static String authDoneCancelPin;
    public static String authCancelPin;
    public static String authDoneRequestPin;

    public final static int SERIAL_NUM = 1;// 流水号

    public final static int BATCH_NUM = 2;// 批次号

    public final static int PRINT_NUM = 3;// 批次号

    public final static int INPUT_MASTER_KEY_TEXT = 4;// 主密钥密钥值

    // 表A bin 默认开关
    public final static String SWITCH_BINA = "01";

    // 小额免密默认开关
    public final static String SWITCH_FREE_PWD = "01";

    // 小额免签默认开关
    public final static String SWITCH_FREE_SIGN = "01";

    // 主扫二维码后是否打印签单
    public final static String QRCODE_ISPRINT = "01";

    // 闪卡当笔处理默认时间
    public final static String FLASH_CARD_TIME = "10";

    // 闪卡全处理默认时间
    public final static String FLASH_CARD_ALL_TIME = "60";

    // 小额免签默认限额
    public final static String FREE_PWD_AMOUNT = "0300";

    // 小额免密默认限额
    public final static String FREE_SIGN_AMOUNT = "0300";

    // 获取天喻演示样机的系统属性
    public static String TY_PROP_VALUE;
}
