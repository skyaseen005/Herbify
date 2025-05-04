package com.example.herbify


import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

class MainActivity2 : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var mList = ArrayList<PlantData>()
    private lateinit var adapter: PlantAdapter
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: MaterialToolbar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // Set up the toolbar
        toolbar = findViewById(R.id.toolbarTitle)
        setSupportActionBar(toolbar)
        toolbar.title = "Welcome Back"

        // Initialize views
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        // Handle navigation icon click
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu -> Toast.makeText(this, "Menu Clicked", Toast.LENGTH_SHORT).show()
                R.id.menu1 -> Toast.makeText(this, "Menu 1 Clicked", Toast.LENGTH_SHORT).show()
                // Add other menu items as needed
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Initialize RecyclerView and SearchView
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        // Set up the adapter
        addDataToList()
        adapter = PlantAdapter(this, mList)
        recyclerView.adapter = adapter

        // Set up search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    // Filter the plant list based on search query
    private fun filterList(query: String?) {
        val filteredList = ArrayList<PlantData>()
        if (!query.isNullOrEmpty()) {
            for (plant in mList) {
                if (plant.name.lowercase().contains(query.lowercase()) ||
                    plant.scientificName.lowercase().contains(query.lowercase())
                ) {
                    filteredList.add(plant)
                }
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show()
        }

        adapter.setFilteredList(filteredList)
    }

    private fun addDataToList() {
        mList.add(
            PlantData(
                "Neem",
                "Azadirachta indica",
                "Neem is a medicinal plant known for its antibacterial, antifungal, and anti-inflammatory properties.",
                R.drawable.neem,
                ""
            )
        )
        mList.add(
            PlantData(
                "Tulsi",
                "Ocimum tenuiflorum",
                "Tulsi is revered for its healing properties and used in Ayurvedic medicine for centuries.",
                R.drawable.tulasi,
                ""

            )
        )
        mList.add(
            PlantData(
                "Abscess root",
                "Polemonium reptans",
                        "It is used to reduce fever, inflammation, and cough.",
                R.drawable.abscess,
                ""

            )
        )
        mList.add(
            PlantData(
                "Acai",
                "Euterpe oleracea",
                "Although acai berries are a longstanding food source for indigenous people of the Amazon, there is no evidence that they have historically served a medicinal, as opposed to nutritional role.",
                R.drawable.acai,
                ""

            )

        )

        mList.add(
            PlantData(
                "Alder buckthorn",
                    "Frangula alnus",
                "Bark (and to a lesser extent the fruit) has been used as a laxative, due to its 3 – 7% anthraquinone content.",
                R.drawable.alder,
                ""

            )

        )
        mList.add(
            PlantData(
                "Alfalfa",
                "Medicago sativa",
                        "The leaves are used to lower cholesterol, as well as forum kidney and urinary tract ailments, although there is insufficient scientific evidence for its efficacy.",
                            R.drawable.alfalfa,
                ""

            )

        )
        mList.add(
            PlantData(
                "Aloe vera",
                "Aloe vera",
                "Leaves are widely used to heal burns, wounds and other skin ailments.\n",
                R.drawable.alovera,
                ""


            )

        )
        mList.add(
            PlantData(
                " Amargo, bitter-wood",
                "Quassia amara",
                "A 2012 study found a topical gel with 4% Quassia extract to be a safe and effective cure of rosacea.",

                R.drawable.amarago,
                ""
            )
        )
        mList.add(
            PlantData(
                "Arnica",
                " Arnica montana",
                "Used as an anti-inflammatory and for osteoarthritis. The US Food and Drug Administration has classified Arnica montana as an unsafe herb because of its toxicity. It should not be taken orally or applied to broken skin where absorption can occur.",
                R.drawable.arnica,
                ""
            )


        )
        mList.add(
            PlantData(
                "Barberry",
                " Berberis vulgaris",
                        "Long history of medicinal use, dating back to the Middle Ages particularly among Native Americans. Uses have included skin ailments, scurvy and gastro-intestinal ailments.:",
                R.drawable.barberry,
                ""



            )

        )
        mList.add(
            PlantData(
                "Bitter leaf",
                "Vernonia amygdalina",
                "The plant is used by both primates and indigenous peoples in Africa to treat intestinal ailments such as dysentery.",
                R.drawable.bitter,
                ""


            )

        )
        mList.add(
            PlantData(
                " Black cohosh",
                " Actaea racemosa",
                "Historically used for arthritis and muscle pain, used more recently for conditions related to menopause and menstruation.",
                R.drawable.cohosh,
                ""

            )

        )
        mList.add(
            PlantData(
                "Blessed thistle",
                "Cnicus benedictus",
                "Used during the Middle Ages to treat bubonic plague. In modern times, herbal teas made from blessed thistle are used for loss of appetite, indigestion and other purposes.",
                R.drawable.thistle,
                ""


            )
        )


        mList.add(
            PlantData(
                "Asafoetida",
                "Ferula assa-foetida",
                "Might be useful for IBS, high cholesterol, and breathing problems.",
                R.drawable.asafoetida,
                ""

            )
        )
        mList.add(
            PlantData(
                "Ashoka tree",
                "Saraca indica",
                "The plant is used in Ayurvedic traditions to treat gynecological disorders. The bark is also used to combat oedema or swelling.",
                R.drawable.ashoka,
                ""

            )
        )
        mList.add(
            PlantData(
                " Ashwagandha",
                "Withania somnifera",
                "The plant's long, brown, tuberous roots are used in traditional medicine. In Ayurveda, the berries and leaves are applied externally to tumors, tubercular glands, carbuncles, and ulcers.",
                R.drawable.ashwagandha,
                ""

            )
        )
        mList.add(
            PlantData(
                "Asthma-plant",
                "Euphorbia hirta",
                "Used traditionally in Asia to treat bronchitic asthma and laryngeal spasm. It is used in the Philippines for dengue fever.",
                R.drawable.asthma,

                ""
            )
        )
        mList.add(
            PlantData(
                "Astragalus",
                "Astragalus propinquus",
                "Long been used in traditional Chinese medicine to strengthen the immune system, and is used in modern China to treat hepatitis and as an adjunctive therapy in cancer.",
                R.drawable.astragalus,

                ""
            )
        )




    }
}
