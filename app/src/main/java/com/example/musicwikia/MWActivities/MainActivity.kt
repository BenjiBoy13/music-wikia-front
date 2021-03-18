package com.example.musicwikia.MWActivities

import MWApi.RetrofitClient
import MWModels.LoginResponse
import MWStorage.SharedPrefManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.musicwikia.R
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val txtUser = findViewById<EditText>(R.id.txtEditUser)
        val txtPassword = findViewById<EditText>(R.id.txtEditPassword)



        btnLogin.setOnClickListener {
            val userEmail = txtUser.text.toString().trim()
            val password = txtPassword.text.toString().trim()
            val intent: Intent = Intent(this, CentralActivity::class.java)
            when {
                userEmail.isBlank() -> {
                    txtUser.error = "Introduce tu usuario"
                    txtUser.requestFocus()
                }
                password.isBlank() -> {
                    txtPassword.error = "Introduce tu contraseña"
                    txtPassword.requestFocus()
                }
                else -> {
                    //Verificar correo y contraseña
                    RetrofitClient.instance.login(userEmail, password).enqueue(object: retrofit2.Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>?,
                            response: Response<LoginResponse>?
                        ) {
                            if(!response?.body()?.error!!){
                                SharedPrefManager.getInstance(applicationContext).saveUser(response.body().user)

                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            } else
                                Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_LONG).show()
                        }
                        override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                            Toast.makeText(applicationContext, t?.message, Toast.LENGTH_LONG).show()
                        }
                    })
                }
            }
        }
    }
}