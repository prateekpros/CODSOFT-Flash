package com.example.flash
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.flash.ui.theme.FlashTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val cameraM=getSystemService(CAMERA_SERVICE) as CameraManager
            FlashTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    FlashLightComposable(cameraM = cameraM)
                }
            }
        }
    }
}


@Composable
fun FlashLightComposable(cameraM: CameraManager) {

    var state by remember {
        mutableStateOf(false)
    }
    val flash by remember{
        mutableStateOf(true)
    }


    val rearCamera  = cameraM.cameraIdList.getOrNull(0)


    if(rearCamera != null ) {
        DisposableEffect(state) {
            try {
                if (state)
                    cameraM.setTorchMode(rearCamera, true)
            } catch (e: Exception) {
                Log.e("TAG", e.printStackTrace().toString())
            }

            onDispose {
                try {
                    cameraM.setTorchMode(rearCamera, false)
                } catch (e: Exception) {
                    Log.e("TAG", e.printStackTrace().toString())
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center,
        ){


               Button(onClick = { state = !state }, Modifier.size(100.dp)) {
                Text(text = if(state) "ON" else "OFF")
               }
        }

    }
    else{
        Text(text = "NO Flash Light Found")
    }

}
