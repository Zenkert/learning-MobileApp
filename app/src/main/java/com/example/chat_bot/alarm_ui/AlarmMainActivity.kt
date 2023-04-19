package com.example.chat_bot.Activities.acivity


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.chat_bot.Activities.Welcomepage.WelcomePage
import com.example.chat_bot.alarm_adapter.ItemAdapter
import com.example.chat_bot.alarm_data.CustomBottomBar
import com.example.chat_bot.alarm_data.CustomBottomItem
import com.example.chat_bot.alarm_fragments.AlarmFragment
import com.example.chat_bot.alarm_fragments.SettingsFragment
import com.example.chat_bot.R

@SuppressLint("ResourceType")
class AlarmMainActivity : AppCompatActivity(), ItemAdapter.ItemSelectorInterface{

    private var customBottomBar: CustomBottomBar? = null
    private val ALARM_HOME = 0
    private val SETTINGS = 1
    private var fm: androidx.fragment.app.FragmentManager?=null
    private var active: Fragment? = null
    private var fragment1: Fragment? = null
    private var fragment2: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedprefs=getSharedPreferences("pref", Context.MODE_PRIVATE)
        val darkModeIsActivated=sharedprefs.getBoolean("DARK MODE",false)
        if(darkModeIsActivated){
            setTheme(R.style.DarkMode)
        }else{
            setTheme(R.style.WhiteMode)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_activity_main)

        createNotificationChannel()
        val action=intent!!.action


        fragment1 = AlarmFragment()
        fragment2 = SettingsFragment()
        active = fragment1
        fm = supportFragmentManager
        fm!!.beginTransaction().add(R.id.fragmentcontainer, fragment2!!, "2").hide(fragment2!!).commit()
        fm!!.beginTransaction().add(R.id.fragmentcontainer, fragment1!!, "1").commit()
        customBottomBar = CustomBottomBar(
            this,
            findViewById(R.id.customBottomBar),
            this@AlarmMainActivity
        )
        initItems()
        //convert our custom attributes to typedArray
        val typedArray=this.obtainStyledAttributes(R.styleable.ds)
        val defaultBackgroundString=typedArray.getString(R.styleable.ds_bottomBarBackground)
        typedArray.recycle()
        customBottomBar!!.changeBackground(defaultBackgroundString!!)
        customBottomBar!!.defaultBackground = defaultBackgroundString
        customBottomBar!!.defaultTint = getString(R.color.colorItemDefaultTint)
        customBottomBar!!.changeDividerColor(getString(R.color.colorDivider))
        customBottomBar!!.hideDivider()
        customBottomBar!!.apply(ALARM_HOME)
    }

    @SuppressLint("ResourceType")
    private fun initItems() {
        val typeArray=this.obtainStyledAttributes(R.styleable.ds)
        val textColor=typeArray.getString(R.styleable.ds_bottomBarBackgroundTextColor)
        typeArray.recycle()
        val alarmHome = CustomBottomItem(
            ALARM_HOME,
            R.drawable.ic_alarm, getString(R.string.Alarms),
            getString(R.color.colorItem1Background), textColor
        )

        val settings = CustomBottomItem(
            SETTINGS, R.drawable.ic_settings,
            getString(R.string.settings), getString(R.color.colorItem2Background),
            textColor
        )


        customBottomBar!!.addItem(alarmHome)
        customBottomBar!!.addItem(settings)
    }

    override fun itemSelect(selectedID: Int) {

        if(selectedID==(ALARM_HOME)){
            fm!!.beginTransaction().hide(active!!).show(fragment1!!).commit()
            active = fragment1
            try {
                fragment1!!.onStart()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        else if(selectedID==(SETTINGS)){
            fm!!.beginTransaction().hide(active!!).show(fragment2!!).commit()
            active = fragment2
            try {
                fragment2!!.onStart()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }



    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "SEEDS CHAT & LEARN"
            val description = "Channel For Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("SeedsAndroid", name, importance)
            channel.description = description

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent = Intent(this@AlarmMainActivity, WelcomePage::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
        }
        return super.onKeyDown(keyCode, event)
    }
}
