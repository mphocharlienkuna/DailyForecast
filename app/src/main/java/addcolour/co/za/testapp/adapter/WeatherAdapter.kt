package addcolour.co.za.testapp.adapter

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import addcolour.co.za.testapp.R
import addcolour.co.za.testapp.databinding.RowItemWeatherBinding
import addcolour.co.za.testapp.model.WeatherList
import addcolour.co.za.testapp.viewHolder.WeatherViewHolder

class WeatherAdapter : RecyclerView.Adapter<WeatherViewHolder>() {

    private var mList: List<WeatherList>? = null

    fun setWeatherListList(WeatherListList: List<WeatherList>) {
        if (mList == null) {
            mList = WeatherListList
            notifyItemRangeInserted(0, WeatherListList.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return mList!!.size
                }

                override fun getNewListSize(): Int {
                    return WeatherListList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return mList!![oldItemPosition].dayOfWeek.equals(
                            WeatherListList[newItemPosition].dayOfWeek, ignoreCase = true)
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val newWeatherList = WeatherListList[newItemPosition]
                    val oldWeatherList = mList!![oldItemPosition]
                    return newWeatherList.dayOfWeek == oldWeatherList.dayOfWeek
                }
            })
            mList = WeatherListList
            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = DataBindingUtil
                .inflate<RowItemWeatherBinding>(LayoutInflater.from(parent.context), R.layout.row_item_weather,
                        parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.mBinding.data = mList!![position]
        holder.mBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return if (mList == null) 0 else mList!!.size
    }
}