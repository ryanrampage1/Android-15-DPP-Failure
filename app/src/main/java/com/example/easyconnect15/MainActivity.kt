package com.example.easyconnect15

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding),
                        ::launchDpp
                    )
                }
            }
        }
    }
    fun launchDpp(uri: String) {
        val wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager

        // check at least version q

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Log.e("DPP-15-Demo","Easy Connect is not supported")
            Toast.makeText(this, "Easy Connect is not supported pre Android 10", Toast.LENGTH_SHORT).show()
            return
        }

        if (wifiManager.isEasyConnectSupported()) {
            val intent = Intent(Settings.ACTION_PROCESS_WIFI_EASY_CONNECT_URI)
            intent.data = Uri.parse(uri)
            startActivityForResult(intent, 1111)
        } else {
            Log.e("DPP-15-Demo","Easy Connect is not supported")
            Toast.makeText(this, "Easy Connect is not supported on this device", Toast.LENGTH_SHORT).show()
        }
  }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1111) {
            if (resultCode == RESULT_OK) {
                Log.d("DPP-15-Demo", "Easy Connect Success")
                Toast.makeText(this, "Easy Connect Success", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("DPP-15-Demo", "Easy Connect Failed $data")
                Toast.makeText(this, "Easy Connect Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier, launchDpp: (String) -> Unit){
    val dppString: MutableState<String> = 
        remember {
            mutableStateOf("DPP:C:81/6;M:f4:00:fa:00:00:5c;I:DPPSAMPLE;K:MDkwEwYHKoZIzj0asddKoZIzj0DAQcDIgACadxEbfdrKAuz1DSNc+Nql94hWYEkpDZW2Exft9ZfUc0=;;")
        }

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Launch DPP Easy Connect Demo",
            modifier = modifier
        )

        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        TextField(value = dppString.value, onValueChange = {
            dppString.value = it
        })

        Button(
            modifier = Modifier.padding(vertical = 16.dp),
            onClick = {
                launchDpp(dppString.value)
            },
            content =  {
                Text(text = "Launch DPP Easy Connect")
            }
        )
    }
}