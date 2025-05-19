package com.whty.smartpos.unionpay.pay.constant;

/**
 * ProjectName  : TYPayProject
 * PackageName  : com.whty.smartpos.unionpay.pay.constant
 * ClassName    : ResponseCode
 * Description  : 响应码常量（主要用于冲正记录设置响应码）
 * Author       : WangWei
 * Date         : 2020/8/21 14:29
 */
public interface ResponseCode {

    //POS 终端在时限内未能收到 POS 中心的应答
    String SERVER_NO_RESPONSE = "98";

    //POS 终端收到 POS 中心的批准应答消息，但由于 POS 机故障无法完成交易
    String POS_EXCEPTION = "96";

    //POS 终端对收到 POS 中心的批准应答消息，验证 MAC 出错
    String MAC_INCONSISTENT = "A0";

    //其他情况引发的冲正
    String UNHANDLED_SITUATION = "06";

    //成功
    String SUCCESS = "00";
}
