package com.example.tmjsoup

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    var year = mutableListOf<String>()
    var team = mutableListOf<String>()
    var teamImage = mutableListOf<String>()
    var countryImage = mutableListOf<String>()
    var transerType = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            var document = Document(null)
            var document2 = Document(null)
            var elements : Elements
            var elements2 : Elements
            var flag : Boolean

            try {
                document = Jsoup.connect("https://www.transfermarkt.com/cristiano-ronaldo/profil/spieler/8198").get()
            } catch (e: IOException){
                Log.i("TAG","Error")
            }

            elements = document.getElementsByClass("tm-player-transfer-history-grid__date")
            flag = true
            for(element in elements){
                if (flag){
                    flag = false
                    continue
                }
                //Log.i("TAG", "${element.text().takeLast(4)}")
                year.add(element.text().takeLast(4))
            }

            elements = document.getElementsByClass("tm-player-transfer-history-grid__new-club")
            flag = true
            for(element in elements){
                if (flag){
                    flag = false
                    continue
                }
                team.add(element.text())
                //Log.i("TAG", "${element.text()}")
            }

            elements = document.getElementsByClass("tm-player-transfer-history-grid__club-link")
            flag = false
            for(element in elements){
                flag = !flag
                if(flag)
                    continue
                //Log.i("TAG", "https://www.transfermarkt.com${element.attr("href")}")

                try {
                    document2 = Jsoup.connect("https://www.transfermarkt.com${element.attr("href")}").get()
                } catch (e: IOException){
                    Log.i("TAG","Error")
                }

                elements2 = document2.getElementsByClass("dataBild").select("img")
                var url : String
                for(element in elements2){
                    url = element.attr("src")
                    if(url!="")
                        teamImage.add(url)
                        //Log.i("TAG", "$url")
                }

            }

            elements = document.getElementsByClass("tm-player-transfer-history-grid__new-club").select("img")
            var url : String
            for(element in elements){
                url = element.attr("data-src")
                if(url!="")
                    countryImage.add(url.drop(52).dropLast(14))
                    //Log.i("TAG", "${url.drop(52).dropLast(14)}")
            }

            elements = document.getElementsByClass("tm-player-transfer-history-grid__fee")
            flag = true
            for(element in elements){
                if (flag){
                    flag = false
                    continue
                }
                if (element.text() == "End of loan")
                    transerType.add("END OF LOAN")
                    //Log.i("TAG", "END OF LOAN")
                else if (element.text().contains("loan") or element.text().contains("Loan"))
                    transerType.add("LOAN")
                    //Log.i("TAG", "LOAN")
                else
                    transerType.add("TRANSFER")
                    //Log.i("TAG", "TRANSFER")
            }
            Log.i("TAG", year.toString())
            Log.i("TAG", team.toString())
            Log.i("TAG", teamImage.toString())
            Log.i("TAG", countryImage.toString())
            Log.i("TAG", transerType.toString())

        }
    }
}