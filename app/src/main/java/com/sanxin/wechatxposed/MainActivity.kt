package com.sanxin.wechatxposed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sanxin.wechatxposed.util.shortToast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!isModuleActive()) shortToast("Xposed模块未激活!")
    }

    //判断模块是否生效的方法，XposedInit里把这个方法Hook掉返回true
    private fun isModuleActive() = false
}
