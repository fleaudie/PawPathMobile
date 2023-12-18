package com.fleaudie.pawpath

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridLayout
import android.widget.GridView
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.fleaudie.pawpath.databinding.FragmentEmojiDialogBinding

class EmojiDialogFragment(private val emojiSelectedListener: (String) -> Unit) : DialogFragment() {

    private val emojiResourceIds = arrayOf(
        R.drawable.emoji_dog_walking, R.drawable.emoji_bird, R.drawable.emoji_bird_feed, R.drawable.emoji_bird_feed2,
        R.drawable.emoji_dog_bath, R.drawable.emoji_cat_bath, R.drawable.emoji_cat_feed, R.drawable.emoji_bird_release,
        R.drawable.emoji_clean_litter, R.drawable.emoji_clean_litter2, R.drawable.emoji_cat_playing,
        R.drawable.emoji_pet_bed, R.drawable.emoji_pet_bath, R.drawable.emoji_dog_toys, R.drawable.emoji_dog_playing,
        R.drawable.emoji_dog_house, R.drawable.emoji_dog_food, R.drawable.emoji_close_cage
    )

    private lateinit var binding: FragmentEmojiDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmojiDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridView: GridView = binding.emojiContainer
        val columnCount = 3

        val adapter = EmojiAdapter(requireContext(), emojiResourceIds)
        gridView.adapter = adapter

        gridView.setOnItemClickListener { _, _, position, _ ->
            emojiSelectedListener.invoke(emojiResourceIds[position].toString())
            dismiss()
        }
    }

    class EmojiAdapter(private val context: Context, private val emojiResourceIds: Array<Int>) : BaseAdapter() {

        override fun getCount(): Int {
            return emojiResourceIds.size
        }

        override fun getItem(position: Int): Any {
            return emojiResourceIds[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val emojiImageView = ImageView(context)
            emojiImageView.setImageResource(emojiResourceIds[position])
            emojiImageView.layoutParams = AbsListView.LayoutParams(
                150,  // Genişlik
                150   // Yükseklik
            )
            return emojiImageView
        }
    }
}

