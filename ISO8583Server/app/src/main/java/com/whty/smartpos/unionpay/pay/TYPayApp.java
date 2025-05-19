
/*
 * = COPYRIGHT
 *          Wuhan Tianyu
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date	                 Author	                Action
 * 20200616 	        liujian                  Create
 */

package com.whty.smartpos.unionpay.pay;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
import com.whty.smartpos.tysmartposapi.ITYSmartPosApi;
import com.whty.smartpos.tysmartposapi.modules.printer.PrinterInitListener;
//import com.whty.smartpos.unionpay.pay.bankindonesia.AddDefault_Bank;
//import com.whty.smartpos.unionpay.pay.core.InternetStatus;
//import com.whty.smartpos.unionpay.pay.setting.POSConfiguration;
//import com.whty.smartpos.unionpay.pay.utils.CrashHandler;
//import com.whty.smartpos.unionpay.pay.utils.MyLocationListener;
//import com.whty.smartpos.unionpay.pay.utils.SharedPrefUtils;
//import com.whty.smartpos.unionpay.pay.utils.TYPayUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

//import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
//import io.reactivex.rxjava3.core.Observable;
//import io.reactivex.rxjava3.schedulers.Schedulers;


public class TYPayApp extends Application {

    private final String TAG = "TYPayApp";
    private static Application mAppContext;

    private static ITYSmartPosApi mPOSApi;



    //网络状态（初始值为无网络）
    //private static InternetStatus internetStatus = InternetStatus.NO_INTERNET;

    public static Application getAppContext() {
        return mAppContext;
    }

    private void setAppContext(Application context) {
        mAppContext = context;
    }

    public static ITYSmartPosApi getPOSApi() {
        return mPOSApi;
    }

//    public static InternetStatus getInternetStatus() {
//        return internetStatus;
//    }

//    private static void setInternetStatus(InternetStatus internetStatus) {
//        TYPayApp.internetStatus = internetStatus;
//    }

    public static void setPOSApi(ITYSmartPosApi mPOSApi) {
        TYPayApp.mPOSApi = mPOSApi;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Aplikasi dimulai");
        String processName = getProcessName(this, android.os.Process.myPid());

        if (!TextUtils.isEmpty(processName) && processName.equals(this.getPackageName())) {
            //判断进程名，保证只有主进程运行
            //主进程初始化逻辑
            init();
            loadData();
        }

    }

    /**
     * 耗时操作考虑后台去处理。
     */
    private void init() {

        Log.d(TAG, "init: tyPay init");

        setAppContext(this);
        setPOSApi(ITYSmartPosApi.get(this));
        getPOSApi().initPrinter(new MyPrinterInitListener());

        //初始化POS参数(批次号，流水号，商户号，终端号)
       // POSConfiguration.initParams();

        initNetworkInspector();

        //异常捕捉
       // CrashHandler crashHandler = CrashHandler.getInstance();
      //  crashHandler.init(getApplicationContext());

    }

    class MyPrinterInitListener implements PrinterInitListener {

        @Override
        public void onPrinterInit(boolean isSuccess) {

        }
    }

    private String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "onTerminate: 应用停止");
    }

    //private LocationClient mLocationClient = null;

//    public LocationClient getLocationClient() {
//        return mLocationClient;
//    }

//    public void initLocationClient() {
//        //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
//        //原有BDLocationListener接口暂时同步保留。具体介绍请参考后文第四步的说明
//        mLocationClient = new LocationClient(getApplicationContext());
//        MyLocationListener myListener = new MyLocationListener();
//        //声明LocationClient类
//        mLocationClient.registerLocationListener(myListener);
//        //注册监听函数
//
//        LocationClientOption option = new LocationClientOption();
//
//        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
//        //可选，设置定位模式，默认高精度
//        //LocationMode.Hight_Accuracy：高精度；
//        //LocationMode.Battery_Saving：低功耗；
//        //LocationMode.Device_Sensors：仅使用设备；
//        option.setIsNeedAddress(true);
//        //可选，是否需要地址信息，默认为不需要，即参数为false
//        //如果开发者需要获得当前点的地址信息，此处必须为true
//        option.setIsNeedLocationDescribe(true);
//        //可选，是否需要位置描述信息，默认为不需要，即参数为false
//        //如果开发者需要获得当前点的位置信息，此处必须为true
//
//        option.setCoorType("BD09");
//        //可选，设置返回经纬度坐标类型，默认GCJ02
//        //GCJ02：国测局坐标；
//        //BD09ll：百度经纬度坐标；
//        //BD09：百度墨卡托坐标；
//        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
//
////        option.setScanSpan(60000 * 2);
//        //可选，设置发起定位请求的间隔，int类型，单位ms
//        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
//        //如果设置非0，需设置1000ms以上才有效
//
////        option.setOpenGps(true);
//        //可选，设置是否使用gps，默认false
//        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
//
////        option.setLocationNotify(true);
//        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
//
//        option.setIgnoreKillProcess(false);
//        //可选，定位SDK内部是一个service，并放到了独立进程。
//        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
//
//        option.SetIgnoreCacheException(false);
//        //可选，设置是否收集Crash信息，默认收集，即参数为false
//
//        option.setWifiCacheTimeOut(5 * 60 * 1000);
//        //可选，V7.2版本新增能力
//        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
//
//        option.setEnableSimulateGps(false);
//        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
//
//        option.setNeedNewVersionRgc(true);
//        //可选，设置是否需要最新版本的地址信息。默认需要，即参数为true
//
//        mLocationClient.setLocOption(option);
//        //mLocationClient为第二步初始化过的LocationClient对象
//        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
//
//        //开始定位
//        getLocationClient().start();
//    }


    /**
     * 如果涉及到动态申请的权限是必须的，数据加载或许要放到权限检查之后，或者将权限申请提前
     */
    private void loadData() {
//        Observable.create(emitter -> {
//            TYPayUtils.addDefaultOperators();
//            SharedPrefUtils.getInstance().operatorSignedOffPOS();
//            if (!emitter.isDisposed()) {
//                emitter.onNext(true);
//                emitter.onComplete();
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe();

        //heri - menambah table daftar bank
       // AddDefault_Bank addDefault_bank=new AddDefault_Bank(getAppContext());

    }

    private void initNetworkInspector() {

        ConnectivityManager mConnectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager == null) {
            Log.d(TAG, "networkStatus: mConnectivityManager null");
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            mConnectivityManager.requestNetwork(new NetworkRequest.Builder().build(),
                    new ConnectivityManager.NetworkCallback() {
                        @Override
                        public void onAvailable(Network network) {
                            super.onAvailable(network);
                            Log.d(TAG, "onAvailable: ");
                            //detectInternetConnectionValidation();
                        }

                        @Override
                        public void onLosing(Network network, int maxMsToLive) {
                            super.onLosing(network, maxMsToLive);
                            Log.d(TAG, "onLosing: ");
                           // detectInternetConnectionValidation();
                        }

                        @Override
                        public void onLost(Network network) {
                            super.onLost(network);
                            Log.d(TAG, "onLost: ");
                           // detectInternetConnectionValidation();
                        }

                        @Override
                        public void onUnavailable() {
                            super.onUnavailable();
                            Log.d(TAG, "onUnavailable: ");
                           // detectInternetConnectionValidation();
                        }

                        @Override
                        public void onCapabilitiesChanged(Network network,
                                                          NetworkCapabilities networkCapabilities) {
                            super.onCapabilitiesChanged(network, networkCapabilities);
                            Log.d(TAG, "onCapabilitiesChanged: ");
                           // detectInternetConnectionValidation();
                        }

                        @Override
                        public void onLinkPropertiesChanged(Network network,
                                                            LinkProperties linkProperties) {
                            super.onLinkPropertiesChanged(network, linkProperties);
                            Log.d(TAG, "onLinkPropertiesChanged: ");
                           // detectInternetConnectionValidation();
                        }
                    });

        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void detectInternetConnectionValidation() {
//        //耗时操作起线程处理
//        new Thread() {
//
//            @Override
//            public void run() {
//                super.run();
//                setInternetStatus(hasInternetConnectionM(getAppContext()));
//            }
//        }.start();
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
//    private synchronized InternetStatus hasInternetConnectionM(final Context context) {
//        final ConnectivityManager connectivityManager = (ConnectivityManager) context.
//                getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivityManager == null) {
//            return InternetStatus.NO_INTERNET;
//        }
//        final Network network = connectivityManager.getActiveNetwork();
//        if (network == null) {
//            return InternetStatus.NO_INTERNET;
//        }
//        final NetworkCapabilities capabilities = connectivityManager
//                .getNetworkCapabilities(network);
//
//        if (capabilities == null) {
//            return InternetStatus.NO_INTERNET;
//        }
//
//        boolean internetExistence =
//                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
//        Log.d(TAG, "hasInternetConnectionM: has internet:" + internetExistence);
//        if (!internetExistence) {
//            return InternetStatus.NO_INTERNET;
//        }
//        boolean internetValidation = false;
//        String[] urlList = getResources().getStringArray(R.array.connectionDetectionUrlList);
//        if (urlList.length == 0) {
//            Log.e(TAG, "hasInternetConnectionM: 未找到网络连通性服务器url配置，设置当前网络不可用");
//        } else {
//            for (String url : urlList) {
//                internetValidation = isConnectionValidated(url);
//                if (internetValidation) {
//                    break;
//                }
//            }
//        }
//        Log.d(TAG, "hasInternetConnectionM: has validated connection:" + internetValidation);
//        if (!internetValidation) {
//            return InternetStatus.INTERNET_NOT_VALIDATED;
//        }
//        return InternetStatus.INTERNET_VALIDATED;
//    }

    private boolean isConnectionValidated(String serverUrl) {

        //网络操作，最好在非UI线程中执行
        Log.d(TAG, "isConnectionValidated: serverUrl:" + serverUrl);

        HttpURLConnection urlConnection = null;
        int httpResponseCode;
        try {
            //准备连接的uri
            URL url = new URL(serverUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setConnectTimeout(6000);
            urlConnection.setReadTimeout(6000);
            urlConnection.setUseCaches(false);
            //发起连接
//            long requestTimestamp = SystemClock.elapsedRealtime();
            urlConnection.getInputStream();
//            long responseTimestamp = SystemClock.elapsedRealtime();
            //获取服务器回应
            httpResponseCode = urlConnection.getResponseCode();
            Log.d(TAG, "isConnectionValidated: httpResponseCode:" + httpResponseCode);
            //拿到回应
            if (httpResponseCode >= 200
                    && httpResponseCode < 300) {
                return true;
            }
        } catch (IOException e) {
            Log.d(TAG, "isConnectionValidated: 测试连接失败");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return false;
    }
}
