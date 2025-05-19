
package com.whty.smartpos.unionpay.pay.constant;

public enum Tag {

    PIN_KEY("FF8012"), // pin密钥
    MAC_KEY("FF8013"), // mac密钥
    TRACK_KEY("FF8014"), // 磁道密钥
    TRACK_ENCRYPT_FLAG("FF8015"), // 磁道信息加密标识
    ENCRYPT_ALGORITHM_FLAG("FF8017"), // 加密算法标识
    TRADE_TYPE_CODE("FF9000"), // 交易类型码
    CARD_NUM("FF9001"), // 卡号
    TRADE_AMOUNT("FF9002"), // 交易金额
    SERIAL_NUM("FF9003"), // 流水号
    TIME("FF9004"), // 时间
    DATE("FF9005"), // 日期
    VALIDITY("FF9006"), // 有效期
    CLEARING_DATE("FF9007"), // 清算日期
    INPUT_TYPE("FF9008"), // 输入方式
    PIN_FLAG("FF9009"), // PIN标识
    CARD_SERIAL("FF900A"), // 卡序列号
    PIN_OBTAIN_CODE("FF900B"), // PIN获取码
    ACQUIRER_ID("FF900C"), // 售卡方标识码
    TRACK_2("FF900D"), // 二磁道
    TRACK_3("FF900E"), // 三磁道
    RETRIEVAL_REFERENCE_NUM("FF900F"), // 检索参考号
    RESPONSE_CODE("FF9010"), // 应答码
    ISSUER_NUM("FF9013"), // 发卡机构信息
    ACQUIRER_INS("FF9014"), // 收单机构信息
    RECONCILIATION_RESULT("FF9015"), // 对账结果
    TRADE_CURRENCY_CODE("FF901B"), // 交易货币代码
    PIN("FF901C"), // 密码
    PIN_ENCRYPT_FUNCTION("FF901D"), // PIN加密方法
    ACCOUNT_TYPE("FF901E"), // 账户类型
    BALANCE_TYPE("FF901F"), // 余额类型
    BALANCE_SYMBOL("FF9020"), // 余额符号
    BALANCE("FF9021"), // 余额
    BATCH_NUM("FF9022"), // 批次号
    NETWORK_INFO_MANAGE_CODE("FF9023"), // 网络信息管理码
    TERMINAL_READING_ABILITY("FF9024"), // 终端读取能力
    IC_CONDITION_CODE("FF9025"), // IC卡条件代码
    PART_DEDUCTION_FLAG("FF9026"), // 部分扣款标识
    INTEGRAL_ACCOUNT_TYPE("FF9027"), // 积分账户类型
    OLD_BATCH_NUM("FF9028"), // 原批次号
    OLD_SERIAL_NUM("FF9029"), // 原POS流水号
    OLD_DATE("FF902A"), // 原交易日期
    AUTHORIZE_MODE("FF902B"), // 授权方式
    AUTHORIZED_INSTITUTION_CODE("FF902C"), // 授权机构代码
    INTERNATIONAL_CREDIT_CARD_COMPANY_CODE("FF9040"), // 国际信用卡公司代码
    OPERATOR_CODE("FF9041"), // 操作员代码
    ISSUING_KEEP("FF9042"), // 发卡保留域
    ACQUIRER_KEEP("FF9043"), // 收单保留域
    UNION_PAY_KEEP("FF9044"), // 银联保留域
    GET_MAC("FF9045"), // MAC
    AUTHORIZE_CODE("FF9046"), // 授权码
    RECHARGE_CARD_IC_CONDITION_CODE("FF9053"), // 转入卡IC卡条件代码
    DEVICE_SEQUENCE_NUM("FF9055"), // 设备序列号
    RECHARGE_CARD_INPUT_TYPE("FF905A"), // 转入卡服务点输入方式码
    RECHARGE_CARD_PIN_FLAG("FF905B"), // 转入卡包含PIN的标识
    REMAINDER_RECHARGE_BALANCE("FF9063"), // 可充余额
    OLD_RETRIEVAL_REFERENCE_NUM("FF9065"), // 原交易参考号
    OLD_AUTHORIZE_CODE("FF9066"), // 原授权码
    TIP_AMOUNT("FF9067"), // 小费金额
    DOMESTIC_CARD_DEBIT_TOTAL_COUNT("FF906C"), // 内卡借记总笔数
    DOMESTIC_CARD_DEBIT_TOTAL_AMOUNT("FF906D"), // 内卡借记总金额
    DOMESTIC_CARD_CREDIT_TOTAL_COUNT("FF906E"), // 内卡贷记总笔数
    DOMESTIC_CARD_CREDIT_TOTAL_AMOUNT("FF906F"), // 内卡贷记总金额
    ABROAD_CARD_DEBIT_TOTAL_COUNT("FF9070"), // 外卡借记总笔数
    ABROAD_CARD_DEBIT_TOTAL_AMOUNT("FF9071"), // 外卡借记总金额
    ABROAD_CARD_CREDIT_TOTAL_COUNT("FF9072"), // 外卡贷记总笔数
    ABROAD_CARD_CREDIT_TOTAL_AMOUNT("FF9073"), // 外卡贷记总金额
    ISSUE_COUNT("FFA000"), // 分期付款期数
    PROJECT_NUM("FFA001"), // 项目编码
    COMMISSION_PAYMENT("FFA002"), // 手续费支付方式
    FIRST_PHASE_REPAYMENT_AMOUNT("FFA003"), // 首期还款金额
    INSTALLMENT_COMMISSION("FFA004"), // 分期还款手续费
    REPAYMENT_CURRENCY_TYPE("FFA005"), // 还款币种
    EACH_PHASE_COMMISSION("FFA006"), // 每期还款手续费
    RECHARGE_CARD_NUM("FFA011"), // 转入卡卡号
    CERTIFICATE_NUM("FFA020"), // 证件号码
    PHONE_NUM("FFA021"), // 手机号码
    CERTIFICATE_TYPE("FFA025"), // 证件类型
    DEDUCTIBLE("FFA029"), // 自付金额
    POINTS("FFA030"), // 积分数
    COMMODITY_CODE("FFA033"), // 商品代码
    ISSUER_AUTHENTICATION_DATA("91"), // 发卡行认证数据
    ISSUER_SCRIPT1("71"), // 发卡行脚本1
    ISSUER_SCRIPT2("72"), // 发卡行脚本2
    APPLICATION_INTERACTION_FEATURE("82"), // 应用交互特征
    TERMINAL_VERIFY_RESULT("95"), // 终端验证结果
    TRADE_DATE("9A"), // 交易日期
    APPLICATION_FLAG("50"), // 应用标签
    APPLICATION_FIRST_NAME("9F12"), // 应用首选名称
    FIRST_ECASH_CURRENCY_CODE("9F51"), // 第一电子现金货币代码
    ECASH_ISSUER_AUTH_CODE("9F74"), // 电子现金发卡行授权码
    FIRST_ECASH_BALANCE("9F79"), // 第一电子现金余额
    ISSUER_SCRIPT_RESULT("DF31"), // 发卡行脚本结果
    SECOND_ECASH_CURRENCY_CODE("DF71"), // 第二电子现金货币代码
    SECOND_ECASH_BALANCE("DF79"), // 第二电子现金余额
    PUBLIC_KEY_RID("9F06"), // 公钥RID
    ISSUER_APPLICATION_DATA("9F10"), // 发卡行应用数据
    TERMINAL_COUNTRY_CODE("9F1A"), // 终端国家代码
    INTERFACE_DEVICE_SERIAL_NUM("9F1E"), // 接口设备序列号
    PUBLIC_KEY_INDEX("9F22"), // 公钥索引
    APPLICATION_CIPHERTEXT("9F26"), // 应用密文
    TERMINAL_PERFORMANCE("9F33"), // 终端性能
    CVMR("9F34"), // CVMR
    APPLICATION_TRADE_COUNTER("9F36"), // 应用交易计数器
    UNPREDICTABLE_NUM("9F37"), // 不可预知数
    AVAILABLE_ECASH_BALANCE("9F5D"), // 可用电子现金余额
    CARD_PRODUCT_FLAG("9F63"), // 卡产品标识
    PUBLIC_KEY_VALIDITY("DF05"), // 公钥有效期
    PUBLIC_KEY_HASH_ALGORITHM_IDENTIFIER("DF06"),//公钥哈希算法标识
    PUBLIC_KEY_SIGNATURE_ALGORITHM_IDENTIFIER("DF07"),//公钥签名算法标识
    PUBLIC_KEY("DF02"),//公钥模
    PUBLIC_KEY_EXPONENT("DF04"),//公钥指数
    PUBLIC_KEY_CHECK_VALUE("DF03"),//公钥校验值
    ASI("DF01"),//应用选择指示符
    APPLICATION_VERSION_NUMBER("9F09"),//应用版本号
    TAC_DEFAULT("DF11"),//TAC-缺省
    TAC_ONLINE("DF12"),//TAC-联机
    TAC_DENIAL("DF13"),//TAC-拒绝
    TERMINAL_FLOOR_LIMIT("9F1B"),//终端最低限额
    THRESHOLD_VALUE_FOR_BIASED_RANDOM_SELECTION("DF15"),//偏置随机选择阈值
    BIASED_RANDOM_SELECTION_MAXIMUM_TARGET_PERCENTAGE("DF16"),//偏置随机选择最大目标百分数
    BIASED_RANDOM_SELECTION_TARGET_PERCENTAGE("DF17"),//偏置随机选择目标百分数
    DEFAULT_DDOL("DF14"),//缺省DDOL
    E_CASH_BALANCE_TRANSACTION_LIMIT("9F7B"),//电子现金交易限额
    READER_CONTACTLESS_FLOOR_LIMIT("DF19"),//非接脱机最低限额
    READER_CONTACTLESS_TRANSACTION_LIMIT("DF20"),//非接交易限额
    READER_CVM_REQUIRED_LIMIT("DF21"),//非接CVM限额
    TERMINAL_ONLINE_PIN_SUPPORT_ABILITY("DF18"),//终端联机PIN支持能力

    RECORD_INFO("FFF014"), // 交易记录头
    ACCEPTANCE_AMOUNT("FFF015"), // 承兑金额
    PARAM_INFO_FLAG("FFF011"), // 参数信息标识
    PARAMETER("FFF012"), // 参数
    FREE_CLOSE_FREE_SIGN_FLAG("FFF018"), // 免密免签标识
    ARQC("FF0A"), // 授权请求密文，仅用于打印

    // 终端参数
    TEMINAL_NUM("9F1C"), // 终端编号
    MERCHANT_NUM("9F16"), // 商户编号

    CAMERA_TYPE("FFF101"), // 摄像头前后置 01:前置 02:后置
    INPUT_AMOUNT_TYPE("FFF102"), // 金额输入方式：0.分为单位 1.元为单位

    // 可设定参数
    TRANS_OUT_TIME("FF8024"), // 交易超时时间
    TRANS_REDIAL("FF8026"), // 交易重拨次数
    FIRST_PHONE_NUM("FF8000"), // 三个交易电话号码之一
    SECOND_PHONE_NUM("FF8001"), // 三个交易电话号码之二
    THIRD_PHONE_NUM("FF8002"), // 三个交易电话号码之三
    MANAGE_PHONE_NUM("FFF002"), // 一个管理号码
    TIP("FFF003"), // 是否支持小费
    STAND_IN_AUTHORIZATION("FFF01B"), // 小额代授权开关
    TIP_PERCENT("FF804D"), // 小费百分比
    INPUT_CARD_NUM("FFF004"), // 是否支持手工输入卡号
    AUTO_LOGOUT("FF8046"), // 是否自动签退
    MERCHANT_CHINESE_NAME("FF8048"), // 商户名称（中文简称）
    MERCHANT_ENGLISH_NAME("FF8064"), // 商户名称（英文）
    MSG_RESEND_NUM("FF8027"), // 消息重发次数
    MAIN_KEY_INDEX("FF8016"), // 主密钥INDEX
    TRANS_TYPE_BITMAP("FF8060"), // 交易类型位图
    AUTO_SEND_TOTAL_NUM("FF8047"), // 自动上送累计笔数
    CURRENT_YEAR("FFF005"), // 当前年份
    CURRENT_FLOW_NUM("FF23"), // 当前流水号
    CURRENT_BATCH_NUM("FF24"), // 当前批次号
    REFUND_MAX_AMOUNT("FF8061"), // 退货交易最大金额
    DEMOTION_TRADE("FF8062"), // 是否支持降级交易
    SETTLEMENT_PRINT_SETTING("FFF006"), // 结算打印明细设置
    ENGLISH_SETTING("FFF007"), // 英文设置
    SALE_SLIP_SETTING("FFF008"), // 签购单设置
    SLIP_HEADER_SELECT("FFF017"), // 签购单抬头选择
    SET_SALE_SLIP_CONTENT("FF806A"), // 设置签购单抬头内容
    DEFUALT_TRANS("FFF009"), // 终端默认交易设置

    SALE_CANCEL_CARD("FFF00A"), // 消费撤销是否刷卡
    AUTH_DONE_CANCEL_CARD("FFF00B"), // 预授权完成撤销是否刷卡
    AUTH_DONE_CARD("FFF01A"), // 预授权完成是否刷卡

    SALE_CANCEL_PIN("FFF00C"), // 消费撤销是否输密
    AUTH_DONE_CANCEL_PIN("FFF00D"), // 预授权完成撤销是否输密
    AUTH_CANCEL_PIN("FFF00E"), // 预授权撤销是否输密
    AUTH_DONE_REQUEST_PIN("FFF00F"), // 预授权完成(请求)是否输密
    AUTO_BALANCE_TIME("FFF010"),//自动结算时间

    PRE_DAIL("FF8008"), // 是否预拨号
    OUTLINE_NUM("FF8003"), // 外线号码
    GPRS_PARAMS("FF8006"), // GPRS参数
    CDMA_PARAMS("FF8007"), // 交易中心CDMA参数
    LAN_PARAMS("FF8009"), // 交易中心以太网参数
    TPDU("FF903D"), // TPDU
    PRINT_NUM("FF8035"), // 打印张数
    QR_PRINT_NUM("FFF8099"), // 二维码打印张数
    MAX_TRANS_NUM("FF806B"), // 最大交易笔数

    //非接参数
    NON_TOUCHABLE_CARD("FF805D"), // 非接交易通道开关
    FLASH_CARD_CURRENT_PROCESSING_TIME("FF803A"), // 闪卡当笔处理时间
    FLASH_CARD_ALL_PROCESSING_TIME("FF803C"), // 闪卡全处理时间
    NON_TOUCHABLE_HALF_PENNY_FREE_PWD("FFF023"), // 非接小额免密开关
    NON_TOUCHABLE_HALF_PENNY_FREE_PWD_QUOTA("FF8058"), // 非接小额免密限额
    BIN_TABLE_A("FF8055"), // bin表A
    BIN_TABLE_B("FF8056"), // bin表B
    HALF_PENNY_FREE_SIGN("FF805A"), // 非接小额免签开关
    FREE_SIGN_QUOTA("FF8059"), // 非接免签限额
    CDCVM_FLAG("FF8057"), // CDCVM标识
    SUPPORTED_SM("FFF01C"), // 国密开关
    NON_TOUCHABLE_FLAG("FF8054"), // 非接快速业务标识
    NFC_CARD_FIRST("FF810C"), // 优先挥卡，打开就不能插卡，关掉可以进行一次插卡

    INPUT_SYSTEM_PWD("FFF02F"), // 退货类交易是否需要输入主管密码
    PRINT_CHINESE_ACQUIRING_BANK("FFF02A"), // 是否打印中文收单行
    PRINT_CHINESE_ISSUING_BANK("FFF02B"), // 是否打印中文发卡行
    PRINT_ENGLISH("FFF02C"), // 签购单是否打印英文
    TIMEOUT_IMMEDIATELY("FFF02D"), // 接收超时立即冲正
    VOID_PRINT_MINUS("FFF03A"), // 撤销类打印负号
    TTS_PLAY("FFF03B"), // 语音提示
    PAY_AND_SCAN("FFF03C"), // 支付扫码通道
    SUPPORT_SIGNATURE("FFF03D"), // 是否支持电子签名
    SIGNATURE_TIMEOUT("FFF03E"), // 电子签名超时时间

    INPUT_MASTER_KEY_INDEX("FFF030"), // 手工设置主秘钥(索引值)
    INPUT_MASTER_KEY("FFF031"), // 手工设置主秘钥(秘钥值)
    SERVICE_NUM("FFF032"), // 服务热线
    BATCH_SETTLEMENT_TYPE("FFF033"), // 商户结算方式
    MERCHANT_PHONE_NUM("FFF034"), // 商户电话号码
    TRANS_DETAIL_PRINT_FLAG("FFF035"), // 终端明细单打印
    IC_PARAMS_VERSION("FFF036"), // IC卡公钥和IC卡参数版本号
    ADVERTISEMENT("FFF037"), // 广告
    SKIN_FLAG("FFF038"), // 换肤标识

    QR_CONTENT("FFF039"), // 二维码内容
    TRADE_WAY("FFF040"), // 交易方式
    PRINT_MODEL("FFF041"), // 打印模板
    THREE_FLAG("FFF042"), // 是否打印第三联（钱宝）
    REMARK("FFF043"), // 备注（钱宝）
    CARD_HOLDER_NAME("FFF044"), // 持卡人姓名
    RETRIEVAL_REFERENCE_NUM_59("FFF045"), // 59域返回的参考号
    TIME_59("FFF046"), // 59域时间
    DATE_59("FFF047"), // 59域日期
    
    YEAR("FFF048"), // 年份
    PAYMENT_VOUCHER_NUM("FFF049"), // 付款凭证码，二维码用
    QR_PAYMENT_DATA("FFF050"), // 付款凭证码，二维码用
    OLD_PAYMENT_VOUCHER_NUM("FFF051"), // 付款凭证码，二维码用
    IS_FREE_PWD("FFF052"), // 存交易记录是否免密，用于重打印
    IS_FREE_SIGN("FFF053"), // 存交易记录是否免签，用于重打印
    FREE_PWD_AMOUNT("FFF054"), // 存交易记录免密金额
    FREE_SIGN_AMOUNT("FFF055"), // 存交易记录免签金额
    SOCKET_PROTOCOL("FFF080"), // 网络协议
    COMMON_BACKSTAGE("FFF081"), // 常用后台
    QRCODE_ISPRINT("FFF082"), // 主扫动态二维码是否打印签到单

    TSI("9B"),
    SALE47_TAG33("FFF083"),//47域 tag33
    SALE47_TAG35("FFF084"),//7域 tag35
    SALE47_TAG36("FFF088"),//7域 tag36
    ISS("FFF085"),//63域 iss
    CPU("FFF086"),//63域 cpu
    AQU("FFF087"),//63域 aqu

    TRANSACTION_ID("FFF089"),//平台订单号
    OUT_TRANSACTION_ID("FFF090"),//第三方交易号
    OUT_TRADE_NO("FFF091"),//商户订单号
    QR_MERCHANT_NUM("FFF092"),//二维码商户号
    QR_URL("FFF093"),
    QR_SIGN_KEY("FFF094"),
    QR_TERMINAL_NUM("FFF095"),//二维码终端号
    QR_MERCHANT_NAME("FFF096"),//二维码商户名

    GYL_QR_TERMINAL_NUM("FFF097"),//广州银联二维码终端号
    GYL_QR_MERCHANT_NAME("FFF098"),//广州银联二维码商户名
    GYL_QR_MERCHANT_NUM("FFF099"),//广州银联二维码商户号

    // 电子现金明细查询
    EC_TRADE_TIME("9F21"), // 交易时间
    EC_AUTH_AMOUNT("9F02"), // 授权金额
    EC_OTHER_AMOUNT("9F03"), // 其他金额
    EC_CURRENCY_CODE("5F2A"), // 交易货币代码
    EC_MERCHANT_NAME("9F4E"), // 商户名称
    EC_TRADE_TYPE("9C"); // 交易类型

    //非接参数下载


    private String value;

    private Tag(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static Tag getTag(String value) {
        for (Tag tag : Tag.values()) {
            if (value.equals(tag.value)) {
                return tag;
            }
        }
        return null;
    }
}
