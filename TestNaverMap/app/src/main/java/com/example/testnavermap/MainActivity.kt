package com.example.testnavermap


import android.content.Intent
import android.graphics.PointF
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.testnavermap.database.SpotTable
import com.google.android.material.navigation.NavigationView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMap.OnMapClickListener
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.InfoWindow.DefaultViewAdapter
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private lateinit var mapView: MapView

    protected var spotTable: SpotTable? = null
    protected var spots = ArrayList<Spot>()
    private val k = 0

    private val markers = arrayOfNulls<Marker>(16)

    private var infoWindow: InfoWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //intent
        val Search : Button = findViewById(R.id.main_btn_search)


        btn_navi.setOnClickListener {
            layout_drawer.openDrawer(GravityCompat.START)
        }

        naviView.setNavigationItemSelectedListener(this)  //네비게이션 메뉴 아이템에 클릭 속성 부여

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
                ?: MapFragment.newInstance().also {
                    fm.beginTransaction().add(R.id.map, it).commit()
                }
        mapFragment.getMapAsync(this)

        spotTable = SpotTable.instance(applicationContext)

        insertToDB()


        spots = spotTable?.loadByTable() as ArrayList<Spot>
    }

    private fun insertToDB() {
        //System.out.println("main-insertToDB");
        spotTable?.insert("웨이브온 커피", "부산 기장군 장안읍 해맞이로 286 웨이브온커피", "waveon")
        spotTable?.insert("우즈 베이커리", "부산 기장군 일광면 일광로 346", "wooz")
        spotTable?.insert("해운대 블루라인파크", "부산 해운대구 청사포로 116", "blueline")
        spotTable?.insert("해운대 미포끝집", "부산 해운대구 달맞이길62번길 77", "mipo")
        spotTable?.insert("해운대 백년식당", "부산 해운대구 구남로 23", "rest100")
        spotTable?.insert("더베이 101", "부산 해운대구 동백로 52", "thebay101",
                1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0)
        spotTable?.insert("트릭아이 뮤지엄", "부산 중구 대청로126번길 12 부산영화체험박물관", "trick",
                0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1)
        spotTable?.insert("부산 영화 체험 박물관",
                "부산 중구 대청로126번길 12", "busanmovie", 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0)
        spotTable?.insert("부산 근대 역사관", "부산 중구 대청로 104", "history",
                1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0)
        spotTable?.insert("용두산공원", "부산 중구 용두산길 37-55", "yongdu",
                0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1)
        spotTable?.insert("UN기념공원", "부산 남구 대연4동 799-14", "un",
                1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0)
        spotTable?.insert("부산박물관", "부산 남구 유엔평화로 63 부산광역시시립박물관", "museum")
        spotTable?.insert("부산 시민회관", "부산 동구 자성로133번길 16 부산시민회관", "simin")
        spotTable?.insert("해운대 해수욕장", "부산 해운대구 우동", "haesu",
                1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1)
        spotTable?.insert("씨라이프 부산 아쿠아리움", "부산 해운대구 해운대해변로 266", "aqua")
        spotTable?.insert("부산 시립미술관", "부산 해운대구 APEC로 58 부산시립미술관", "gallery")
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {        //네비게이션 메뉴 아이템 클릭 시 수행
        when(item.itemId)
        {
            R.id.hae -> {
                val intent = Intent(this, Haeundae_info::class.java)
                startActivity(intent)
            }
            R.id.nam -> {
                val intent = Intent(this, Nampo_info::class.java)
                startActivity(intent)
            }
            R.id.yeong -> {
                val intent = Intent(this, Yeongdo_info::class.java)
                startActivity(intent)
            }
            R.id.mus -> {
                val intent = Intent(this, Museum_info::class.java)
                startActivity(intent)
            }
            R.id.nat -> {
                val intent = Intent(this, Nature_info::class.java)
                startActivity(intent)
            }
            R.id.cafe -> {
                val intent = Intent(this, Cafe_info::class.java)
                startActivity(intent)
            }
        }
        layout_drawer.closeDrawers()  //네비게이션 뷰 닫기
        return false
    }

    override fun onBackPressed() {
        if(layout_drawer.isDrawerOpen(GravityCompat.START))
        {
            layout_drawer.closeDrawers()
        }
        else {
            super.onBackPressed()    // 일반 백버튼 기능 실행
        }
    }

    @UiThread
    @Override
    override fun onMapReady(naverMap: NaverMap) {
        for (i in 0..15) {
            markers[i] = Marker()
            markers[i]!!.tag = spots.get(i)
        }

        markers[0]!!.position = LatLng(35.322452, 129.270128)
        markers[1]!!.position = LatLng(35.272325, 129.253047)
        markers[2]!!.position = LatLng(35.160193, 129.170820)
        markers[3]!!.position = LatLng(35.156733, 129.174748)
        markers[4]!!.position = LatLng(35.162132, 129.160361)
        markers[5]!!.position = LatLng(35.156784, 129.152138)
        markers[6]!!.position = LatLng(35.101962, 129.033688)
        markers[7]!!.position = LatLng(35.101895, 129.033776)
        markers[8]!!.position = LatLng(35.102841, 129.031203)
        markers[9]!!.position = LatLng(35.100689, 129.032633)
        markers[10]!!.position = LatLng(35.128027, 129.096134)
        markers[11]!!.position = LatLng(35.129625, 129.094148)
        markers[12]!!.position = LatLng(35.127267, 129.093665)
        markers[13]!!.position = LatLng(35.158717, 129.160447)
        markers[14]!!.position = LatLng(35.159354, 129.161001)
        markers[15]!!.position = LatLng(35.166772, 129.137073)


        naverMap.setContentPadding(0, 0, 0, 500)

        infoWindow = InfoWindow()

        naverMap.onMapClickListener = OnMapClickListener { coord: PointF?, point: LatLng? -> infoWindow!!.close() }

        val listener = Overlay.OnClickListener { overlay: Overlay ->
            val marker = overlay as Marker
            if (marker.infoWindow == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                infoWindow!!.open(marker)
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow!!.close()
            }
            true
        }

        infoWindow!!.adapter = object : DefaultViewAdapter(this) {
            override fun getContentView(infoWindow: InfoWindow): View {
                val marker = infoWindow.marker
                val spot = marker!!.tag as Spot?
                val view = View.inflate(this@MainActivity, R.layout.simple_info, null)
                (view.findViewById<View>(R.id.name2) as TextView).text = spot!!.name
                (view.findViewById<View>(R.id.imageView25) as ImageView).setImageResource(resources.getIdentifier(spot!!.image, "drawable", packageName))
                return view
            }
        }

        infoWindow!!.onClickListener = Overlay.OnClickListener { overlay: Overlay? ->
            val marker = infoWindow!!.marker
            val spot = marker!!.tag as Spot?
            val intent = Intent(this@MainActivity, DetailView::class.java)
            intent.putExtra("spot", spot)
            startActivity(intent)
            true
        }

        for (j in 0..13) {
            markers[j]!!.map = naverMap
            markers[j]!!.onClickListener = listener
        }

    }
}