package com.sanxin.wechatxposed.hook

import android.content.ContentValues
import android.util.Log
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import fr.arnaudguyon.xmltojsonlib.XmlToJson

/**
 * Date: 2020/1/14
 *Description: 微信交易账单读取  此功能基于微信7.0.10版本
 */
object ReceivingHook {

    private val TAG = "LogUtils"

    fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            val clazz = XposedHelpers.findClass("com.tencent.wcdb.database.SQLiteDatabase", lpparam.classLoader)
            XposedHelpers.findAndHookMethod(clazz, "insert",
                    String::class.java, String::class.java, ContentValues::class.java,
                    object : XC_MethodHook() {

                        override fun afterHookedMethod(param: MethodHookParam) {
                            super.afterHookedMethod(param)
                            val contentValues = param.args[2] as ContentValues
                            val tableName = param.args[0].toString()
                            //过滤消息内容
                            if (tableName === "message" || tableName.isEmpty()){
                                return
                            }

                            //返回APP系统标识通知 ： AppMessage
                            if (tableName === "AppMessage"){
                                Log.e(TAG, "返回APP系统标识通知 ：$tableName")
                                val type = contentValues.getAsInteger("type")
                                Log.e(TAG, "当前消息类型 type ：$type")
                                if (null === type){
                                    Log.e(TAG,"不是微信收付款信息")
                                    return
                                }
                                Log.e(TAG,"收付款转ContentValues：" + contentValues.getAsString("description"))
                                when(type){
                                    //二维码收付款
                                    5 -> { handleQRCodeReceiving(contentValues) }
                                    //转账收付款
                                    2000 -> { handleBilling(contentValues) }
                                    //红包收付款
                                    2001 -> { handleBilling(contentValues) }
                                    else -> Log.e(TAG, "非收付款")
                                }
                            }
                        }
                    })
        } catch (e: Throwable) {
            Log.e(TAG, "获取收付款信息出错 : " + e.message)
            e.printStackTrace()
        } catch (e: Exception) {
            Log.e(TAG, "获取收付款信息出错: " + e.message)
            e.printStackTrace()
        }
    }

    /**
     * 二维码收付款处理
     */
    private fun handleQRCodeReceiving(contentValues: ContentValues) {
        val description = contentValues.getAsString("description")
        var money: Double = 0.0
        if(description.isNotEmpty()){
            when {
                description.contains("收款金额") -> {
                    money = description.substring(description.indexOf("收款金额￥") + 5,description.indexOf("\n")).toDouble()
                    Log.e(TAG, "收款金额 : $money")
                }
                description.contains("付款金额") -> {
                    money = description.substring(description.indexOf("付款金额￥") + 5,description.indexOf("\n")).toDouble()
                    Log.e(TAG, "付款金额 : $money")
                }
                else -> Log.e(TAG, "未知付款")
            }
            //可以作数据统计  用户信息获取不到
        }
    }

    /**
     * 处理收付款 (废弃)
     */
    private fun handleBilling(contentValues: ContentValues) {
        val content = contentValues.getAsString("content")
        Log.e(TAG, "微信收付款数据：$content")
        val xmlToJson = XmlToJson.Builder(content).build()
        val jsonObject = xmlToJson.toJson()
        Log.e(TAG, "微信收付款json数据 : " + jsonObject.toString())
        jsonObject?.let {
            val mmreader = jsonObject.getJSONObject("msg").getJSONObject("appmsg").getJSONObject("mmreader")
            val des = jsonObject.getJSONObject("msg").getJSONObject("appmsg").getString("des")
            Log.e(TAG, "进入判断字段阶段des$des")
            val title = mmreader.getJSONObject("template_header").getString("title")
            Log.e(TAG, "选择的收付款==$title")
            if (!title.isNullOrEmpty()){
                val mmreader1 = jsonObject.getJSONObject("msg").getJSONObject("appmsg").getJSONObject("mmreader")
                when(title){
                    "收款到账通知" -> ""
                    "微信支付凭证" -> ""
                    "转账到银行卡到账成功" -> ""
                    "银行卡发起成功" -> ""
                    else -> {}
                }
                Log.e(TAG, "收款 ： $mmreader1")
            }
        }?: Log.e(TAG,"微信收付款获取失败")
    }
}