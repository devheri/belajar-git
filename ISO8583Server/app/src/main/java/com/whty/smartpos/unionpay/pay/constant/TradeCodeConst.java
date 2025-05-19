
package com.whty.smartpos.unionpay.pay.constant;

public class TradeCodeConst {

    /**
     * 现金
     **/
    public static final String CASH = "00001";

    /**
     * 消费
     **/
    public static final String SALE = "00002";

    /**
     * 消费撤销
     **/
    public static final String VOID = "00003";

    /**
     * 退货
     **/
    public static final String REFUND = "00004";

    /**
     * 签到
     **/
    public static final String LOGIN = "00012";

    /**
     * 签退
     **/
    public static final String LOGOUT = "00015";

    /**
     * 结算
     **/
    public static final String SETTLEMENT = "00019";

    /**
     * 余额查询
     **/
    public static final String BALANCE_INQUIRE = "00037";

    /**
     * 预授权
     **/
    public static final String AUTH = "00151";

    /**
     * 预授权撤销
     **/
    public static final String CANCEL = "00251";

    /**
     * 预授权完成
     **/
    public static final String AUTH_COMPLETE = "00351";

    /**
     * 预授权完成撤销
     **/
    public static final String COMPLETE_VOID = "00451";

    /**
     * 银联扫码付消费撤销
     **/
    public static final String UNION_PAY_SCAN_VOID = "00086";

    /**
     * 银联扫码付消费退货
     **/
    public static final String UNION_PAY_SCAN_REFUND = "00087";

    /**
     * 银联扫码付消费
     **/
    public static final String UNION_PAY_SCAN_SALE = "00089";

    /**
     * 银联扫码付消费（收款码）
     **/
    public static final String UNION_PAY_CODE_SALE = "00090";

    /**
     * 微信主扫
     **/
    public static final String WECHAT_PAY_SCAN = "00091";

    /**
     * 微信被扫
     **/
    public static final String WECHAT_PAY_QR = "00092";

    /**
     * 支付宝主扫
     **/
    public static final String ALIPAY_SCAN = "00093";

    /**
     * 支付宝被扫
     **/
    public static final String ALIPAY_QR = "00094";

    /**
     * IC卡密钥下载
     **/
    public static final String IC_CARD_DOWNLOAD_KEY = "00095";

    /**
     * 扫码交易查询
     */
    public static final String QR_TRADE_QUERY = "00096";

    /**
     * 二维码订单查询
     */
    public static final String QR_ORDER_QUERY = "00097";

    /**
     * 退款查询
     */
    public static final String UNION_PAY_SCAN_REFUND_QUERY = "00098";

    /**
     * 磁条卡参数下载
     **/
    public static final String MAG_CARD_DOWNLOAD_PARAMS = "01761";

    /**
     * IC卡公钥下载
     **/
    public static final String IC_CARD_DOWNLOAD_PUBKEY = "02761";

    /**
     * IC卡参数下载
     **/
    public static final String IC_CARD_DOWNLOAD_PARAMS = "03761";

    /**
     * 非接业务参数下载
     **/
    public static final String RF_DOWNLOAD_PARAMS = "04761";

    /**
     * 卡bin下载
     **/
    public static final String CARD_BIN_DOWNLOAD = "05761";

    /**
     * 参数下载
     **/
    public static final String DOWNLOAD_PARAMS = "00761";

    /**
     * 回响测试
     **/
    public static final String ECHO_TEST = "00762";

    /**
     * 黑名单下载
     **/
    public static final String DOWNLOAD_BLACKLIST = "00763";

    /**
     * POS参数传递
     */
    public static final String POS_PARAMS_TRANSMIT = "11761";

    /**
     * POS状态上送
     */
    public static final String POS_STATUS_SENDING = "12761";

    /**
     * 下载结束
     */
    public static final String DOWNLOAD_FINISH = "13761";

    /**
     * 打印最后一笔
     */
    public static final String PRINT_LAST_RECORD = "00008";

    /**
     * 打印任意一笔
     */
    public static final String PRINT_ANY_RECORD = "00009";

    /**
     * 打印交易明细
     */
    public static final String PRINT_TRANSACTION_DETAILS = "00010";

    /**
     * 打印交易汇总
     */
    public static final String PRINT_TRANSACTION_SUMMARY = "00011";

    /**
     * 交易汇总
     */
    public static final String TRANSACTION_SUMMARY = "00017";

    /**
     * 流水号查询
     */
    public static final String SERIAL_NUM_INQUIRE = "00018";

    /**
     * 打印上批总计
     */
    public static final String PRINT_LAST_BATCH_SUMMARY = "00100";

    /**
     * 版本
     */
    public static final String VERSION = "00021";

    /**
     * 末笔查询
     */
    public static final String QUERY_LAST_QRTRADE = "00022";

    /**
     * 电子现金快速消费
     */
    public static final String EC_SALE_QUICK = "00023";

    /**
     * 电子现金普通消费
     */
    public static final String EC_SALE_NORMAL = "00024";

    /**
     * 电子现金现金充值
     */
    public static final String EC_LOAD = "00025";

    /**
     * 电子现金余额查询
     */
    public static final String EC_QUERY_BALANCE = "00026";


    /**
     * 指定账户圈存
     */
    public static final String EC_LOAD_ASSIGN = "00028";

    /**
     * TMS参数下载
     */
    public static final String TMS = "00200";

    /**
     * 请求激活码
     */
    public static final String ACTIVATE_BANDING = "04001";

    /**
     * 请求激活码
     */
    public static final String QUERY_ACTIVATE_BANDING = "04002";

    /**
     * 操作员列表
     */
    public static final String OPERATOR = "00101";

    /**
     * 参数打印
     */
    public static final String PRINTPARAMS = "00102";

    /**
     * 交易统计
     */
    public static final String TRANSTATISTICS = "00103";

}
