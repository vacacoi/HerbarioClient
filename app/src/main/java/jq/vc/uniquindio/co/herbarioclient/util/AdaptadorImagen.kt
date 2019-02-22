package jq.vc.uniquindio.co.herbarioclient.util


import android.support.v4.view.PagerAdapter
import android.view.View
import android.os.Parcelable
import android.R.*
import android.content.Context
import android.os.Parcel
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageView
import jq.vc.uniquindio.co.herbarioclient.R
import jq.vc.uniquindio.co.herbarioclient.vo.ImagenSlider


class AdaptadorImagen() : PagerAdapter(), Parcelable {
    private lateinit var imageModelArrayList: ArrayList<ImagenSlider>
    private lateinit var inflater: LayoutInflater
    private lateinit var context: Context

    constructor(parcel: Parcel) : this() {

    }


    constructor(context: Context, imageModelArrayList: ArrayList<ImagenSlider>) : this() {
        this.context = context
        this.imageModelArrayList = imageModelArrayList
        inflater = LayoutInflater.from(context)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return imageModelArrayList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.slider_imagen, view, false)!!

        val imageView = imageLayout!!
            .findViewById(R.id.image) as ImageView


        imageView.setImageResource(imageModelArrayList[position].getImage_drawable())

        view.addView(imageLayout, 0)

        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdaptadorImagen> {
        override fun createFromParcel(parcel: Parcel): AdaptadorImagen {
            return AdaptadorImagen(parcel)
        }

        override fun newArray(size: Int): Array<AdaptadorImagen?> {
            return arrayOfNulls(size)
        }
    }


}