
package com.whty.smartpos.unionpay.pay.constant;

public interface Constants extends LedConst, TradeTypeConst, ProcessingConst {
    /**
     * 预授权菜单
     **/
    public static final int MENU_PREAUTH = 0;
    /**
     * 退款菜单
     **/
    public static final int MENU_REVOKE = 1;
    /**
     * 打印菜单
     **/
    public static final int MENU_PRINT = 2;
    /**
     * 电子现金
     **/
    public static final int MENU_EC = 3;

    public static final int MENU_TYPE = MENU_PREAUTH;

    public static final String MENU_TYPE_KEY = "menuType";

    public static String SUCCESS = "1";

    /**数据库只有一个，但是表有多张，操作员表，银行卡交易记录表，二维码交易记录表**/
    public static final String DB_NAME = "typay.db";

    //厂商武汉天喻编号
    public static final String FACTORY_WHTY_NUMBER = "000024";

    public static final String HASH256HEAD = "0001FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF003031300D060960864801650304020105000420";

}
