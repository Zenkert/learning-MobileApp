package com.example.chat_bot.Activities.Welcomepage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.Secure
import android.provider.Settings.Secure.ANDROID_ID
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.chat_bot.Activities.HomePage.MainActivity
import com.example.chat_bot.R
import com.example.chat_bot.Room.Dao.SeedsDao
import com.example.chat_bot.Room.Entities.OnlineUserData
import com.example.chat_bot.Room.SeedsDatabase
import com.example.chat_bot.data.User
import com.example.chat_bot.databinding.ActivityLoginBinding
import com.example.chat_bot.networking.Retrofit.Seeds_api.api.SEEDSApi
import com.example.chat_bot.networking.Retrofit.Seeds_api.api.SEEDSRepository
import com.example.chat_bot.networking.Retrofit.Seeds_api.api.SEEDSViewModel
import com.example.chat_bot.networking.Retrofit.Seeds_api.api.SEEDSViewModelFact
import com.example.chat_bot.utils.SessionManager
import com.yariksoffice.lingver.Lingver
import kotlinx.coroutines.launch


class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: SEEDSViewModel
    private val retrofitService = SEEDSApi.getInstance()
    lateinit var session: SessionManager
    var user_name:String = ""
    var m_androidId: String ?= null
    private lateinit var language: String
    private lateinit var materialLanguage: String



    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen)
        val sharedprefs: SharedPreferences = this.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val switchIsTurnedOn = sharedprefs.getBoolean("DARK MODE", false)
        if (switchIsTurnedOn) {
            //if true then change app theme to dark mode
            layoutInflater.context.setTheme(R.style.DarkMode)
        } else {
            layoutInflater.context.setTheme(R.style.WhiteMode)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        materialLanguage = ""
        session = SessionManager(applicationContext)
        checklang()
        viewModel = ViewModelProvider(this, SEEDSViewModelFact(SEEDSRepository(retrofitService)))
            .get(SEEDSViewModel::class.java)


        binding.backgroundImage?.setOnClickListener{
            binding.usernameEt.clearFocus()
            val foc = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            foc.hideSoftInputFromWindow(binding.usernameEt.windowToken, 0)
        }

        binding.btnLogin.setOnClickListener{
            if (isOnline(this))
            {
                dologin()
            }
            else
            {
                if (language== "en")
                {
                    materialLanguage = "English"
                    Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show()

                }
                if (language== "de")
                {
                    materialLanguage = "Deutsch"
                    Toast.makeText(this, "Bitte überprüfen Sie Ihre Internetverbindung", Toast.LENGTH_SHORT).show()

                }
                if (language== "es")
                {
                    materialLanguage = "español"
                    Toast.makeText(this, "Por favor, compruebe su conexión a Internet", Toast.LENGTH_SHORT).show()

                }
                if (language== "el")
                {
                    materialLanguage = "ελληνικά"
                    Toast.makeText(this, "Ελέγξτε τη σύνδεσή σας στο διαδίκτυο", Toast.LENGTH_SHORT).show()

                }
            }

        }

        binding.registerTv.setOnClickListener { register() }

        getDevID()

    }

    private fun checklang() {
        language = Lingver.getInstance().getLanguage()
        Log.d("ChatFragment", "language = $language")
    }

    private fun register() {
        if (isOnline(this))
        {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
        }
        else
        {
            Toast.makeText(this, "please check your internet connection", Toast.LENGTH_SHORT).show()
        }

    }


    private fun dologin() {
        user_name = binding.usernameEt.text.toString().trim()

        binding.usernameEt.setOnFocusChangeListener{view,hasFocus ->
            if (!hasFocus){

            }
        }


        setlanguage()

        getDatafromOnlineDB(user_name)

    }

    private fun setlanguage() {
        if (language== "en")
        {
            materialLanguage = "English"

        }
        if (language== "de")
        {
            materialLanguage = "Deutsch"

        }
        if (language== "es")
        {
            materialLanguage = "español"

        }
        if (language== "el")
        {
            materialLanguage = "ελληνικά"

        }
    }

    private fun getDatafromOnlineDB(userName: String) {
        viewModel.userList.observe(this) {

            if (it.success)
            {
                checkUserDataInRoom(it)
            }
            else if (!it.success)
            {
                Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Please create an account to continue", Toast.LENGTH_SHORT).show()
            }
        }


        viewModel.errorMessage.observe(this)
        {

            if (it == "500")
            {
                Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Please create an account to continue", Toast.LENGTH_SHORT).show()

                viewModel.errorMessage.postValue(null)

            }


        }

        viewModel.getUserDatabyName(userName)
    }

    private fun checkUserDataInRoom(onlineUserData: OnlineUserData)
    {
        val dao: SeedsDao = SeedsDatabase.getInstance(this).seedsDao
        lifecycleScope.launch {

            if (dao.isUserExists(onlineUserData.data.name))
            {
                goToLoginActivity()
                Log.d("jaooo", language)
                Log.d("jaooo", "Material: $materialLanguage")

                session.save_details(onlineUserData.data.name,onlineUserData.data.age,onlineUserData.data.grade,materialLanguage)
            }
            else
            {
                checklang()
                dao.insertUser(User(
                    onlineUserData.data.name,onlineUserData.data.age,
                    onlineUserData.data.country,onlineUserData.data.grade,
                    onlineUserData.data.language, onlineUserData.data.dev_id,materialLanguage))
                session.save_materialLangPref(materialLanguage)
                session.save_details(onlineUserData.data.name,onlineUserData.data.age,onlineUserData.data.grade,materialLanguage)
                goToLoginActivity()

            }

        }
    }

    private fun goToLoginActivity() {
        val intent = Intent(this@Login, MainActivity::class.java)
            .setAction(Intent.ACTION_VIEW)
            .setData(Uri.parse("success"))
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
        session.createLoginSession(user_name, m_androidId.toString())
        session.save_materialLangPref(materialLanguage)

    }

    private fun getDevID() {
        m_androidId = Secure.getString(contentResolver, ANDROID_ID)
        Log.d("DevID", m_androidId.toString())
    }


    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }

        return false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent = Intent(this@Login, WelcomePage::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
        }
        return super.onKeyDown(keyCode, event)
    }

}



