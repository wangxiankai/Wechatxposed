package com.sanxin.wechatxposed

import com.sanxin.wechatxposed.hook.ReceivingHook
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Date: 2020/1/16
 *Description:  xp 初始化
 */
class XposedInit : IXposedHookLoadPackage {

    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (lpparam.packageName) {
            //微信相关
            "com.tencent.mm" -> {
                //收付款挂钩
                ReceivingHook.hook(lpparam)
            }
            //Hook掉模块验证方法返回true，验证模块是否生效
            "com.sanxin.wechatxposed" -> {
                XposedHelpers.findAndHookMethod("com.sanxin.wechatxposed.MainActivity",
                    lpparam.classLoader, "isModuleActive", XC_MethodReplacement.returnConstant(true))
            }
        }
    }


}