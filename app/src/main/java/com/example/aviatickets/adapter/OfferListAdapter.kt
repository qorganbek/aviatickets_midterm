package com.example.aviatickets.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aviatickets.R
import com.example.aviatickets.databinding.ItemOfferBinding
import com.example.aviatickets.model.entity.Offer

class OfferListAdapter : RecyclerView.Adapter<OfferListAdapter.ViewHolder>() {

    val items: ArrayList<Offer> = arrayListOf()

    fun setItems(offerList: List<Offer>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = items.size
            override fun getNewListSize(): Int = offerList.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                items[oldItemPosition].id == offerList[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                items[oldItemPosition] == offerList[newItemPosition]
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(offerList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOfferBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(
        private val binding: ItemOfferBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        fun bind(offer: Offer) {
            val flight = offer.flight

            with(binding) {
                departureTime.text = flight.departureTimeInfo
                arrivalTime.text = flight.arrivalTimeInfo
                route.text = context.getString(
                    R.string.route_fmt,
                    flight.departureLocation.code,
                    flight.arrivalLocation.code
                )
                duration.text = context.getString(
                    R.string.time_fmt,
                    getTimeFormat(flight.duration).first.toString(),
                    getTimeFormat(flight.duration).second.toString()
                )
                direct.text = context.getString(R.string.direct)
                price.text = context.getString(R.string.price_fmt, offer.price.toString())

                val imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRFpbRgOME1T7VXwjR9EVqoLPs_yzIDiTP7vszxC_mhIZ1rr55j2mfdLS0rDbQv1ptQphc&usqp=CAU"
                Glide.with(context)
                    .load(imageUrl)
                    .into(airlineImage)
            }
        }

        private fun getTimeFormat(minutes: Int): Pair<Int, Int> = Pair(
            first = minutes / 60,
            second = minutes % 60
        )

    }
}