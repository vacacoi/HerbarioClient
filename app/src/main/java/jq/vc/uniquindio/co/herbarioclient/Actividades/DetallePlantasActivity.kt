package jq.vc.uniquindio.co.herbarioclient.Actividades

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity;
import jq.vc.uniquindio.co.herbarioclient.R
import android.support.v4.view.ViewPager
import android.util.Log

import jq.vc.uniquindio.co.herbarioclient.vo.ImagenSlider

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import com.viewpagerindicator.CirclePageIndicator
import jq.vc.uniquindio.co.herbarioclient.util.AdaptadorImagen
import jq.vc.uniquindio.co.herbarioclient.vo.ListaPlantas
import kotlinx.android.synthetic.main.activity_detalle_pokemon.*
import kotlinx.android.synthetic.main.resumen_lista_plantas.*





class DetallePlantasActivity : AppCompatActivity() {

    private var mPager: ViewPager? = null
    private var currentPage = 0
    private var NUM_PAGES = 0
    private var p0:Int?=0
    private lateinit var imageModelArrayList: ArrayList<ImagenSlider>
    lateinit var listaPlantas:ArrayList<ListaPlantas> //Recibe la lista de plantas

    private val myImageList = intArrayOf(
        R.drawable.silueta_planta,
        R.drawable.silueta_planta,
        R.drawable.silueta_planta,
        R.drawable.silueta_planta,
        R.drawable.silueta_planta,
        R.drawable.silueta_planta

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pokemon)

        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        listaPlantas =intent.getSerializableExtra("lista") as ArrayList<ListaPlantas> //Recibe la lista de obtetos de lista de plantas
        p0 = intent.extras.getInt("p0")//Recibe la posición en la cual se oprimio el botón para ver detallado de plantas


        textNombreDet.text = " "+listaPlantas.get(p0!!).nombre
        textGeneroDet.text = " "+listaPlantas.get(p0!!).genero
        textFamiliaDet.text = " "+listaPlantas.get(p0!!).familia
        textSubFamiliaDet.text = " "+listaPlantas.get(p0!!).subfamilia
        textTribuDet.text = " "+listaPlantas.get(p0!!).tribu
        textEspecieDet.text = " "+listaPlantas.get(p0!!).especie
        textDetalleDet.text = " "+listaPlantas.get(p0!!).detalle
        textAutorDet.text = " "+listaPlantas.get(p0!!).autor


        imageModelArrayList = ArrayList()
        imageModelArrayList = populateList()

        init()

    }

    /**
     * Permite agregar la fecha de presionar atrás
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    private fun populateList(): ArrayList<ImagenSlider> {

        val list : ArrayList<ImagenSlider> = ArrayList()

        for (i in 0..5) {
            val imageModel = ImagenSlider()
            imageModel.setImage_drawable(myImageList[i])
            list.add(imageModel)
        }

        return list
    }

    private fun init() {

        mPager = findViewById(R.id.pager) as ViewPager
        mPager!!.adapter = AdaptadorImagen(this@DetallePlantasActivity, imageModelArrayList)

        val indicator = findViewById(R.id.indicator) as CirclePageIndicator

        indicator.setViewPager(mPager)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator.radius = 5 * density

        NUM_PAGES = imageModelArrayList.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage === NUM_PAGES) {
                currentPage = 0
            }
            mPager!!.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)

        // Pager listener over indicator
        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentPage = position

            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })

    }
}
