package com.example.exp8

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.GridView
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: FrameGridAdapter
    private lateinit var allFrameItems: List<FrameItem>
    private var displayedItems: MutableList<FrameItem> = mutableListOf()
    private lateinit var tvSelectionBadge: TextView

    private lateinit var btnPillMixed: TextView
    private lateinit var btnPillOcean: TextView
    private lateinit var btnPillRoad: TextView
    private lateinit var btnPillCreative: TextView

    private val detailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            if (data != null) {
                val id = data.getIntExtra(DetailActivity.EXTRA_ID, -1)
                val isSelected = data.getBooleanExtra(DetailActivity.EXTRA_IS_SELECTED, false)
                if (id != -1) {
                    val item = allFrameItems.find { it.id == id }
                    if (item != null && item.isSelected != isSelected) {
                        item.isSelected = isSelected
                        adapter.notifyDataSetChanged()
                        updateSelectionBadge()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MainActivity: onCreate")
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvSelectionBadge = findViewById(R.id.tv_selection_badge)
        val gridView = findViewById<GridView>(R.id.grid_view_frames)
        val btnOptionsMenu = findViewById<ImageButton>(R.id.btn_options_menu)

        btnPillMixed = findViewById(R.id.btn_pill_mixed)
        btnPillOcean = findViewById(R.id.btn_pill_ocean)
        btnPillRoad = findViewById(R.id.btn_pill_road)
        btnPillCreative = findViewById(R.id.btn_pill_creative)

        // Initialize 9 high-quality photographs matching reference UI
        allFrameItems = listOf(
            FrameItem(1, "Coastal Cliff", R.drawable.frame_coastal_cliff, "frame_coastal_cliff.webp", "Turquoise ocean waves crashing against dramatic coastal cliffs.", "Ocean"),
            FrameItem(2, "Winding Road", R.drawable.frame_winding_road, "frame_winding_road.webp", "Aerial view of a scenic winding mountain road in autumn.", "Road"),
            FrameItem(3, "Misty Forest", R.drawable.frame_dark_forest, "frame_dark_forest.webp", "Dense green forest canopy wrapped in early morning atmosphere.", "Creative"),
            FrameItem(4, "Blue Mountains", R.drawable.frame_blue_mountains, "frame_blue_mountains.webp", "Majestic blue mountain peaks rising above cloud layers.", "Mixed"),
            FrameItem(5, "Snow Forest Road", R.drawable.frame_snow_road, "frame_snow_road.webp", "Aerial perspective of a snow-covered road cutting through winter pine forest.", "Road"),
            FrameItem(6, "Sunset Lake", R.drawable.frame_sunset_lake, "frame_sunset_lake.webp", "Tranquil mountain lake reflection during a vibrant pink sunset.", "Ocean"),
            FrameItem(7, "Autumn Peak Vista", R.drawable.frame_autumn_peak, "frame_autumn_peak.webp", "Hiker overlooking vibrant autumn foliage and distant mountain range.", "Creative"),
            FrameItem(8, "Ocean Horizon", R.drawable.frame_ocean_sunset, "frame_ocean_sunset.webp", "Solitary boat floating on a calm open ocean horizon at dusk.", "Ocean"),
            FrameItem(9, "Tropical Island", R.drawable.frame_tropical_island, "frame_tropical_island.webp", "Aerial coastline view of tropical island shores and turquoise water.", "Ocean")
        )

        displayedItems.clear()
        displayedItems.addAll(allFrameItems)

        adapter = FrameGridAdapter(
            context = this,
            items = displayedItems,
            onItemClick = { item, _ ->
                launchDetailView(item)
            },
            onPopupMenuClick = { anchorView, item, position ->
                showImagePopupMenu(anchorView, item, position)
            },
            onSelectionChanged = { _, _ ->
                updateSelectionBadge()
            }
        )

        gridView.adapter = adapter
        updateSelectionBadge()

        // Setup Category Filter Pills
        setupCategoryFilterPills()

        // Top-left options menu control
        btnOptionsMenu.setOnClickListener { anchor ->
            showOptionsMenuPopup(anchor)
        }

        // Bottom Action buttons
        findViewById<View>(R.id.btn_action_select_all).setOnClickListener {
            adapter.selectAll()
            updateSelectionBadge()
        }

        findViewById<View>(R.id.btn_action_deselect_all).setOnClickListener {
            adapter.deselectAll()
            updateSelectionBadge()
        }

        findViewById<View>(R.id.btn_action_view_selected).setOnClickListener {
            val selectedList = allFrameItems.filter { it.isSelected }
            if (selectedList.isNotEmpty()) {
                launchDetailView(selectedList.first())
            } else {
                Toast.makeText(this, "No photos currently selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCategoryFilterPills() {
        val pills = listOf(btnPillMixed, btnPillOcean, btnPillRoad, btnPillCreative)

        fun selectPill(selectedPill: TextView, category: String) {
            pills.forEach { pill ->
                if (pill == selectedPill) {
                    pill.setBackgroundResource(R.drawable.bg_pill_active)
                    pill.setTextColor(ContextCompat.getColor(this, R.color.frame_text_primary))
                } else {
                    pill.setBackgroundResource(R.drawable.bg_pill_inactive)
                    pill.setTextColor(ContextCompat.getColor(this, R.color.frame_text_secondary))
                }
            }

            displayedItems.clear()
            if (category == "Mixed") {
                displayedItems.addAll(allFrameItems)
            } else {
                displayedItems.addAll(allFrameItems.filter { it.category == category || it.category == "Mixed" })
            }
            adapter.notifyDataSetChanged()
            updateSelectionBadge()
        }

        btnPillMixed.setOnClickListener { selectPill(btnPillMixed, "Mixed") }
        btnPillOcean.setOnClickListener { selectPill(btnPillOcean, "Ocean") }
        btnPillRoad.setOnClickListener { selectPill(btnPillRoad, "Road") }
        btnPillCreative.setOnClickListener { selectPill(btnPillCreative, "Creative") }
    }

    private fun updateSelectionBadge() {
        val selectedCount = allFrameItems.count { it.isSelected }
        val totalCount = allFrameItems.size
        tvSelectionBadge.text = getString(R.string.selection_counter_format, selectedCount, totalCount)
    }

    private fun showOptionsMenuPopup(anchorView: View) {
        val popup = PopupMenu(this, anchorView)
        popup.menuInflater.inflate(R.menu.menu_main, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            handleOptionsMenuItemClick(menuItem)
        }
        popup.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return handleOptionsMenuItemClick(item) || super.onOptionsItemSelected(item)
    }

    private fun handleOptionsMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_select_all -> {
                allFrameItems.forEach { it.isSelected = true }
                adapter.notifyDataSetChanged()
                updateSelectionBadge()
                true
            }
            R.id.action_deselect_all -> {
                allFrameItems.forEach { it.isSelected = false }
                adapter.notifyDataSetChanged()
                updateSelectionBadge()
                true
            }
            R.id.action_open_webview -> {
                val intent = Intent(this, WebViewActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_about -> {
                showAboutDialog()
                true
            }
            else -> false
        }
    }

    private fun showImagePopupMenu(anchorView: View, item: FrameItem, position: Int) {
        val popup = PopupMenu(this, anchorView)
        popup.menuInflater.inflate(R.menu.menu_image_item, popup.menu)

        val toggleItem = popup.menu.findItem(R.id.action_toggle_select)
        if (item.isSelected) {
            toggleItem.title = getString(R.string.menu_deselect)
        } else {
            toggleItem.title = getString(R.string.menu_select)
        }

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_view_image -> {
                    launchDetailView(item)
                    true
                }
                R.id.action_toggle_select -> {
                    adapter.toggleSelection(position)
                    updateSelectionBadge()
                    true
                }
                R.id.action_image_details -> {
                    launchDetailView(item)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun launchDetailView(item: FrameItem) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_ID, item.id)
            putExtra(DetailActivity.EXTRA_TITLE, item.title)
            putExtra(DetailActivity.EXTRA_RES_ID, item.drawableResId)
            putExtra(DetailActivity.EXTRA_FILENAME, item.filename)
            putExtra(DetailActivity.EXTRA_DESCRIPTION, item.description)
            putExtra(DetailActivity.EXTRA_IS_SELECTED, item.isSelected)
        }
        detailLauncher.launch(intent)
    }

    private fun showAboutDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.about_title)
            .setMessage(R.string.about_message)
            .setPositiveButton(R.string.about_close) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "MainActivity: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MainActivity: onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "MainActivity: onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "MainActivity: onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MainActivity: onDestroy")
    }

    companion object {
        private const val TAG = "FRAME_LIFECYCLE"
    }
}