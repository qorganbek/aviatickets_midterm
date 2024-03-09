package com.example.aviatickets.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.aviatickets.R
import com.example.aviatickets.adapter.OfferListAdapter
import com.example.aviatickets.databinding.FragmentOfferListBinding
import com.example.aviatickets.model.entity.Offer
import com.example.aviatickets.model.network.ApiClient
import com.example.aviatickets.model.service.FakeService


class OfferListFragment : Fragment() {

    companion object {
        fun newInstance() = OfferListFragment()
    }

    private var _binding: FragmentOfferListBinding? = null
    private val binding
        get() = _binding!!

    private val adapter: OfferListAdapter by lazy {
        OfferListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOfferListBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        fetchOffers()
//        adapter.setItems(FakeService.offerList)
    }

    private fun fetchOffers() {
        ApiClient.getOfferService().getOfferList().enqueue(object : retrofit2.Callback<List<Offer>> {
            override fun onResponse(call: retrofit2.Call<List<Offer>>, response: retrofit2.Response<List<Offer>>) {
                if (response.isSuccessful) {
                    // Update adapter's dataset
                    print(response.body())
                    adapter.setItems(response.body() ?: emptyList())
                } else {
                    // Handle the case where the response is not successful
                    Toast.makeText(context, "Failed to fetch offers", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Offer>>, t: Throwable) {
                // Handle failure, such as a network error
                Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupUI() {
        with(binding) {
            offerList.adapter = adapter

            sortRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.sort_by_price -> {
                        val sortedList = adapter.items.sortedBy { it.price }
                        adapter.setItems(sortedList)
                    }

                    R.id.sort_by_duration -> {
                        val sortedList = adapter.items.sortedBy { it.flight.duration }
                        adapter.setItems(sortedList)
                    }
                }
            }
        }
    }
}