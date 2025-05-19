
package com.whty.smartpos.unionpay.pay.constant;

public interface TranControlFlagConst {

    int INPUT_AMOUNT = 0x00;//输金额界面

    int DIALOG_SWIPE_CARD = 0X01;//请刷卡对话框

    int INPUT_PASSWORD = 0X02;//输密界面

    int DIALOG_CONNECT = 0X03;

    int DIALOG_RESULT = 0X04;

    int DISMISS_DIALOG = 0X05;//取消对话框

    int DIALOG_TRANS_COMPLETE = 0X06;

    int BACKGROUND = 0x07;//显示背景

    int DIALOG_STATUS = 0x08;//显示交易状态的对话框，如 交易成功

    int FINISH = 0x09;//交易结束

    int PRE_AUTH = 0x10;//预授权界面，可以手输卡号

    int DIALOG_PER_AUTH_SWIPE_CARD = 0x11;//预授权请刷卡对话框

    int PRE_AUTH_COMM = 0x12;//预授权撤销输入信息界面

    int DIALOG_PART_ACCEPTANCE = 0x13;//部分承兑对话框

    int INSTALMENT = 0x14;

    int POINT_CONSUMPTION = 0x15;//积分消费

    int ORDER = 0x16;//订购

    int CARD_HOLDER = 0x17;//持卡人验证

    int DIALOG_SHOW_RESULT = 0x18;//显示结果对话框

    int PRE_ORDER = 0x19;//订购预授权

    int INPUT_TRANS_VOUCHER_NUM = 0x20;//输入流水号界面

    int REFUND = 0x21;//输入退货金额界面

    int CONFIRM_AMOUNT = 0x22;//退货确认金额界面

    int OFFLINE_REFUND = 0x23;//离线退货

    int FIND_RECORD = 0x24;

    int RECORD_SUMMARY = 0x25;//交易汇总界面

    int TRANS_RECORD = 0x26;//交易明细界面

    int DIALOG_PRINT_TRADE_DETAIL = 0x27;//是否打印交易明细对话框

    int CHANGE_OPERATOR_PWD = 0x28;//修改操作员密码

    int TVR_TSI = 0x29;//TVR&&TSI

    int CARD_HOLDER_INFO = 0x30;//持卡人信息界面

    int OFFLINE_SETTLEMENT_1 = 0x31;//离线结算1

    int OFFLINE_SETTLEMENT_2 = 0x32;//离线结算2

    int CHOICE_CERTIFICATE = 0x33;//选择证件

    int CHOOSE_OPTION = 0x34;//应用选择

    int CONFIRM_RESULT = 0x35;//持卡人认证确认

    int DISPLAY_MESSAGE = 0x36;//显示K21提供的信息

    int INPUT_IC_CARD_PASSWORD = 0x37;//IC卡输入密码

    int EC_DETAIL = 0x38;//电子现金明细列表

    int DISPLAY_REMAINDER_RECHARGE_BALANCE = 0x39;//显示充值余额

    int INPUT_IC_CARD_AMOUNT = 0x40;//IC卡输入金额

    int LOCK_DEVICE = 0x41;//锁机

    int SENDING_OFFLINE_TRADE = 0x42;//上送离线交易

    int OPERATOR_LOGIN = 0x43;//操作员登录

    int CONFIRM_CARD_NUM = 0x44;//确认卡号流程

    int VERSION = 0x45;//版本

    int SWIPE_CARD_FRAGMENT = 0x46;//刷卡界面

    int SHOW_CARD_NUM = 0x47;//显示卡号

    int TRADE_SUCCESS = 0x48;//交易成功

    int TRADE_FAIL = 0x49;//交易失败

    int SHOW_TOAST = 0x50;//弹提示

    int SHOW_TRANS_INFO = 0x51;//显示交易签购单

    int VERIFY_SUPERVISOR_PASSWORD = 0x52; //验证主管密码

    int ONLINE_SEND_MESSAGE = 0x53;//上送交易报文

    int QR_SCAN = 0x54;//扫码

    int ACTRUAL_AMOUNT = 0x55;//真实交易金额

    int CONFIRM_QRPAY = 0x56;//确认扫码

    int PLAY_MEDIA = 0x57;//播放声音

    int EC_SIGNATURE = 0x58;//签名界面

    int LOADING = 0x59;//加载中过度动画

    int OUT_OF_PAPER = 0x60;//缺纸对话框

    int TO_BATCH_SETTLEMENT = 0x61;//批结算

    int ON_UPGRADE_HARDWARE = 0x62;

    int UPGRADE_HARDWARE_SUCCESS = 0x63;

    int UPGRADE_HARDWARE_FAIL = 0x64;

    int STOP_TIMER = 0x65;// 停止刷卡页面的计时器

    int LOGIN_RESULT = 0x66;//签到结果

    int REFRESH_SUCCESS_FRAGMENT = 0x67;//刷新成功的页面

    int SHOW_SUCCESS_FRAGMENT = 0x68;//显示成功的页面

    int SHOW_PRINTING_DIALOG = 0x69;//显示正在打印的弹窗

    int SHOW_TIMER_TASK = 0x70;//显示打印完成，等待几秒回首页的消息

    int SHOW_QRCODE = 0x71;//显示收款码页面

    int TO_LOGIN = 0x72;//去签到

    int SELECT_APPLICATION = 0x73;//应用选择

    int RESULT_ERR_CODE = 0x74;//卡锁应用锁

    int SWIPECARD_DIALOG = 0x75;//刷复合卡

    int REMOVE_CARD = 0x76;//请移卡

    int AIDL_RESPONSE = 0x77;//AIDL返回结果

    int SHOW_QR_SUCCESS = 0x78;//显示扫码交易成功界面

    int CONTINUE_PRINT = 0x79;//显示继续打印

    int SHOW_QR_ACTIVATE_CODE = 0x80;//显示激活二维码页面

    int ACTIVATE_SUCCESS = 0x81;//显示激活二维码页面

    int RF_TIPS = 0x82;//默认非接交易开关关闭，此时挥卡是提示非接交易关闭

    int RF_FAILED = 0x83;//手机PAY绑定公交卡，公交卡失败后，POS继续轮询非接不退出.手机切换成银行卡NFC后读卡能正确识别

    int OPERATOR_LIST = 0x84;//操作员列表

    int ADD_OPERATOR = 0x85;//增加操作员
	
	int PRINT_ERR = 0x86;//打印错误

    int TRANSTATISTICS = 0x87;//交易统计

    int SHOW_UNKNOWN_SCAN_TEADE = 0x88;//查询未知状态扫码

    int IN_CHARGING = 0x89;//正在充电的状态

    int TRADE_SUCCESS_SPEEK = 0x8a;//交易成功语音播报
}
