package com.whty.smartpos.unionpay.pay.constant;

/**
 * Created by zengyahui on 2018/4/26.
 */

public interface ProcessingConst {

    /**
     * 无处理要求
     */
    String NONE_PROCESSING_DEMAND= "0";

    /**
     * 下载终端磁条卡参数
     */
    String DOWNLOAD_TERMINAL_MAG_CARD_PARAMS = "1";

    /**
     * 上传终端磁条卡参数
     */
    String UPLOAD_TERMINAL_MAG_CARD_PARAMS = "2";

    /**
     * 重新签到
     */
    String RELOGON = "3";

    /**
     * 更新公钥
     */
    String UPDATE_PUBKEY = "4";

    /**
     * 更新IC卡参数
     */
    String UPDATE_IC_CARD_PARAMS = "5";

    /**
     * 下载TMC参数
     */
    String DOWNLOAD_TMS_PARAMS = "6";

    /**
     * 卡bin黑名单下载
     */
    String DOWNLOAD_CARD_BIN_BLACK_LIST = "7";

    /**
     * 非接业务参数下载
     */
    String RF_DOWNLOAD_PARAMS = "9";
}
