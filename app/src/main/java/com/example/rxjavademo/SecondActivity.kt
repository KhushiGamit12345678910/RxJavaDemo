package com.example.rxjavademo

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.rxjavademo.databinding.ActivitySecondBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@Suppress("DIVISION_BY_ZERO")
@AndroidEntryPoint
class SecondActivity : AppCompatActivity() {

    lateinit var binding: ActivitySecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivitySecondBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //data(50)
       // illegalArgumentException("nothing")
        coroutineData()
        tryCatchFunction()
        main()

    }

    private fun data(num : Int) {

        try {
            var fName = 20
            num==fName

        }catch (e : Exception){

            Log.d("TAG", "data: $e ")
        }
    }

    private fun coroutineData(){

        CoroutineScope(Dispatchers.IO).launch {
            delay(5000)
            Log.d("Data_Information", "call data 3")
        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            Log.d("data_information","Call data ")
        }
        CoroutineScope(Dispatchers.IO).launch {

            //delay(3000)
            Log.d("data_information","Call data 2")
        }

    }

    private fun tryCatchFunction(){

        try {
            val number = 10/0
        }catch (e : ArithmeticException){
            Log.e(TAG, "tryCatchFunction: $e")
        }catch (e : Exception){
            Log.e("error---->>>>>>>", "not valid value : $e")
        }
    }

    private fun illegalArgumentException(message: String): Nothing {

        throw IllegalArgumentException(message)
    }

    private fun main() = runBlocking {
        repeat(10) {
            launch {
                delay(5000L)
                Log.d(TAG, "main data")
            }
        }
    }
}