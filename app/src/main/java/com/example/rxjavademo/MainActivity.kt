package com.example.rxjavademo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rxjavademo.adapter.MyAdapter
import com.example.rxjavademo.databinding.ActivityMainBinding
import com.example.rxjavademo.models.DataModel
import com.example.rxjavademo.network.ApiInterface
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var retrofit: Retrofit

    lateinit var binding: ActivityMainBinding

    private var dataList: ArrayList<DataModel> = arrayListOf()
    private lateinit var myAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //allFunctions()
        //-------- FUNCTION CALL ------//
        filteringOperator()
        observerData()
        initData()
    }

    private fun allFunctions(){
        simpleObservable()
        observableJoin()
        observableMap()
        combineData()
        observeConcat()
        takeLast()
        main()
    }

    private fun initData() {

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
    }

    @SuppressLint("CheckResult")
    private fun observerData() {

        try {
            val apiInterFace = retrofit.create(ApiInterface::class.java)

            apiInterFace.apiData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
//                .subscribe {
//                    Log.d("data---->>>>", "observerData:${it.toString()} ")
//                }

        } catch (e: Exception) {
            Log.e("TAG-------->>>>>", "list:$e")
        }
    }

    private fun handleResponse(myList: ArrayList<DataModel>) {

        dataList = ArrayList(myList)
        myAdapter = MyAdapter(this, dataList)
        binding.recyclerView.adapter = myAdapter

    }

    private fun handleError(error: Throwable) {

        Log.d("error --------->>>>>", "handleError:${error.localizedMessage} ")

        Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
    }

    private fun simpleObservable() {

        val myList = listOf("f", "b", "g", "d", "e", "a", "c")
        val observable = Observable.fromIterable(myList)

        observable.subscribe(object : Observer<String> {
            override fun onSubscribe(d: Disposable) {

                Log.d("TAG", "onSubscribe: ")
            }

            override fun onError(e: Throwable) {
                Log.d("error------>>>>>>", "onError:$e ")
            }

            override fun onComplete() {

                Log.d("TAG", "onComplete:")
            }

            override fun onNext(t: String) {

                Log.d("tag------>>>>>>", "onNext:$t ")
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun observableMap() {

        val listOne = arrayOf("C", "Java", "kotlin")
        val listTwo = arrayOf("HTML", "Boostrap")

        val observableOne = Observable.fromArray(listOne.toString())
        val observableTwo = Observable.fromArray(listTwo.toString())


        Observable.concat(observableOne, observableTwo)
            .subscribe(object : Observer<String?> {

                override fun onSubscribe(d: Disposable) {
                    Log.d("TAG", "onSubscribe: ")
                }

                override fun onError(e: Throwable) {
                    Log.d("TAG", "onError:$e ")
                }

                override fun onComplete() {
                    Log.d("TAG", "onComplete:")
                }

                override fun onNext(t: String) {
                    Log.d("TAG", "onNext: $t")

                }
            })

        try {

            val letters = arrayOf("a", "b", "c", "d","a", "b", "c", "d")

            val result = StringBuilder()

            val observable = Observable.fromArray(*letters)


            observable.take(5).subscribe { letter: String? -> result.append(letter) }
            Log.d("result-------->>>>>", "observableMap:$result ")

        }catch (e: Exception){
            Log.e("Error ---->>>>>>", "observableMap:$e ")
        }

    }


    @SuppressLint("CheckResult")
    private fun observableJoin() {
        val namesObservable = Observable.just(listOf("A", "B", "C"), listOf("D", "E", "F"))
        namesObservable
            .flatMapSingle { names -> addCharSingle(names) }
            .subscribe { item ->
                Log.d("spreadToString ------->>>>", "observableJoin: ${item.joinToString()}")
            }
    }

    private fun addCharSingle(letters: List<String>): Single<List<String>> {
        return Single.fromCallable { letters.map{ letter -> "$letter" } }
    }

    @SuppressLint("CheckResult")
    private fun observeConcat(){

        val namesObservable = Observable.just(listOf("A"), listOf("E", "F"))

        namesObservable
            .map { names -> Single.just(names).concatWith(addConcatSingle(names)).toObservable() }
            .subscribe { item ->

                Log.d("joinToString ------->>>>", "observeConcat: $item ")

                Log.d("Alphabet----->>>>", "observeConcat: $namesObservable ")
            }
    }

    private fun addConcatSingle(latters : List<String>): Single<List<String>>{
        return Single.fromCallable { latters.map { latter -> latter } }
    }

    @SuppressLint("CheckResult")
    private fun combineData() {
        try {
            val numbers = arrayOf(1, 2, 3, 4, 5, 6)
            val letters = arrayOf("a", "b", "c", "d", "e", "f")
            val result = java.lang.StringBuilder()

            val observable1 = Observable.fromArray(*letters)
            val observable2 = Observable.fromArray(*numbers)

            Observable.combineLatest(observable1, observable2) { a: String, b: Int -> a + b }
                .subscribe { letter: String? -> result.append(letter) }

            Log.d("join ------->>>>", "combineData: $result ")

        }catch (e: Exception){
            Log.e("Error-------->>>>>>", "combineData: $e" )
        }
    }

    @SuppressLint("CheckResult")
    private fun main() {
        val letters = arrayOf("ONE, ","TWO, ","THREE, ","FOUR")
        val result = java.lang.StringBuilder()
        val observable = Observable.fromArray(*letters)
        observable
            .map { data: String ->
                data.uppercase(
                    Locale.getDefault()
                )
            }
            .subscribe { letter: String? ->
                result.append(
                    letter
                )
            }
        Log.d("map----->>>>>>>>>", "main:$result ")
    }

    private fun filteringOperator(){

        val d: Disposable = Observable.just("Hello world!")
            .delay(1, TimeUnit.SECONDS)
            .subscribeWith(object : DisposableObserver<String?>() {

                override fun onStart() {
                    Log.d("start------>>>>", "onStart: Start!! ")
                }
                override fun onNext(t: String) {
                    Log.d("TimeUnit------>>>>", "onNext: $t")
                }
                override fun onError(t: Throwable) {
                    Log.e("Error----->>>", "onError: $t", )
                }
                override fun onComplete() {
                    Log.d("TAG------->>>>", "onComplete: Done ")
                }
            })

        Log.d("Data------->>>>>", "onNext: $d ")

    }
    @SuppressLint("CheckResult")
    private fun takeLast(){
        val latters = arrayOf("java", "python","html","Ruby")
        val char = arrayOf("DS","kotlin","dart","c")
        val result = java.lang.StringBuilder()
        val observeOne = Observable.fromArray(*latters)
        val observeTwo = Observable.fromArray(*char)
        Observable.combineLatest(observeOne,observeTwo){ a : String, b: String -> a+b}
            .subscribe { latter : String -> result.append(latter) }

        Log.d("combineWith------>>>>>", "takeLast: $result")
    }
}






