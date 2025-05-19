package com.whty.smartpos.unionpay.pay.constant;

/**
 * Created by zengyahui on 2018/4/26.
 */

public interface TradeTypeConst {

    /**
     * 余额查询
     */
    int BALANCE_INQUIRE = 1;

    /**
     * 预授权
     */
    int AUTH = 2;

    /**
     * 预授权撤销
     */
    int CANCEL = 3;

    /**
     * 预授权完成
     */
    int AUTH_COMPLETE = 4;

    /**
     * 预授权完成撤销
     */
    int COMPLETE_VOID = 5;

    /**
     * 消费
     */
    int SALE = 6;

    /**
     * 消费撤销
     */
    int VOID = 7;

    /**
     * 退货
     */
    int REFUND = 8;

    /**
     * 电子现金脱机消费
     */
    int EC_SALE = 13;

    /**
     * 电子现金现金充值
     */
    int EC_LOAD = 29;

    /**
     * 现金
     */
    int CASH = 50;

    /**
     * 微信支付
     */
    int WECHAT_PAY = 51;

    /**
     * 微信撤销
     */
    int WECHAT_PAY_VOID = 52;

    /**
     * 微信退货
     */
    int WECHAT_PAY_REFUND = 53;

    /**
     * 支付宝支付
     */
    int ALIPAY = 54;

    /**
     * 支付宝撤销
     */
    int ALIPAY_VOID = 55;

    /**
     * 支付宝退货`
     */
    int ALIPAY_REFUND = 56;

    /**
     * 银联扫码
     */
    int UNION_PAY_SCAN_SALE = 57;

    /**
     * 银联扫码撤销
     */
    int UNION_PAY_SCAN_VOID = 58;

    /**
     * 银联扫码退货
     */
    int UNION_PAY_SCAN_REFUND = 59;

    //-----------------以下类型用于打印总计使用，切勿他用-----------------------
    int ALIPAYRESULT = 80;      //支付宝总计
    int WECHATRESULT = 81;      //微信总计
    int UNIONPAYRESULT = 82;    //银联总计
    int SCANRESULT = 83;        //扫码总计
    int BANKRESULT = 84;        //银行卡总计
    int PROAUTHRESULT = 85;     //预授权总计
    int PROAUTHCOMPLETEDRESULT = 86;    //预授权完成总计
    int CARDRESULT = 87;        //银行卡总计
}
