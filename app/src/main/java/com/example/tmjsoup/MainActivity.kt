package com.example.tmjsoup

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dw = descriptionWebscrape().execute()
    }

    class descriptionWebscrape() : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String? {
            var document = Document(null)

            try {
                document = Jsoup.connect("https://www.transfermarkt.com/brandon-thomas/profil/spieler/251680").get()
            } catch (e: IOException){
                Log.i("TAG","Error")
            }

            var elements : Elements
            var theDescription : String
            var flag : Boolean

            elements = document.getElementsByClass("tm-player-transfer-history-grid__date")
            flag = true
            for(element in elements){
                if (flag){
                    flag = false
                    continue
                }
                Log.i("TAG", "${element.text().takeLast(4)}")
            }

            elements = document.getElementsByClass("tm-player-transfer-history-grid__new-club")
            flag = true
            for(element in elements){
                if (flag){
                    flag = false
                    continue
                }
                Log.i("TAG", "${element.text()}")
            }

            elements = document.getElementsByClass("tm-player-transfer-history-grid__club-link")
            flag = false
            for(element in elements){
                flag = !flag
                if(flag)
                    continue
                Log.i("TAG", "https://www.transfermarkt.com${element.attr("href")}")
            }

            elements = document.select("img[src]")
            var absoluteURL : String
            var srcValue : String
            for(element in elements){
                if(element.attr("class").equals("tm-player-transfer-history-grid__flag.lazy.entered.loaded")){
                    absoluteURL = element.absUrl("srcset")
                    srcValue = element.attr("srcset")
                    Log.i("TAG", "${absoluteURL}")
                    Log.i("TAG", "${srcValue}")
                }
            }

            elements = document.getElementsByClass("tm-player-transfer-history-grid__fee")
            flag = true
            for(element in elements){
                if (flag){
                    flag = false
                    continue
                }
                if (element.text() == "End of loan")
                    Log.i("TAG", "END OF LOAN")
                else if (element.text().contains("loan") or element.text().contains("Loan"))
                    Log.i("TAG", "LOAN")
                else
                    Log.i("TAG", "TRANSFER")
            }


            return null
        }

        override fun onPreExecute() {
            super.onPreExecute()
            // ...
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            // ...
        }
    }
}