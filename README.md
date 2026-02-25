# 🌿 Herbify — Herbal Plant Encyclopedia App

![Android](https://img.shields.io/badge/Platform-Android-green?logo=android)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple?logo=kotlin)
![API](https://img.shields.io/badge/API-24%2B-brightgreen)
![License](https://img.shields.io/badge/License-MIT-blue)

> **Discover the Power of Nature** — Your personal herbal plant companion powered by the Perenual Plant API.

---

## 📱 Screenshots

| Home | Explore | Plant Detail | My Garden | About |
|------|---------|--------------|-----------|-------|
| ![Home](screenshots/home.png) | ![Explore](screenshots/explore.png) | ![Detail](screenshots/detail.png) | ![Garden](screenshots/garden.png) | ![About](screenshots/about.png) |

> *(Add your own screenshots in a `/screenshots` folder)*

---

## ✨ Features

| Feature | Description |
|---|---|
| 🏠 **Home** | Hero banner with 6 herbal importance cards and quick facts |
| 🔍 **Explore Plants** | Search + 6 filter chips (Edible, Indoor, Perennial, Annual, Full Sun) with pagination |
| 🌿 **Plant Detail** | Collapsing toolbar, info cards, poison warnings, hardiness zone, family & more |
| 🌻 **Virtual Garden** | Add/remove plants to a personal local garden using Room database |
| ℹ️ **About & Constraints** | App info + 6 important safety disclaimers for herbal plant use |

---

## 🏗️ Project Structure

```
herbify/
├── app/src/main/java/com/example/herbify/
│   ├── MainActivity.kt
│   ├── SplashActivity.kt
│   ├── adapter/
│   │   ├── PlantAdapter.kt          # RecyclerView adapter for plant list
│   │   └── GardenAdapter.kt         # RecyclerView adapter for garden
│   ├── model/
│   │   ├── Plant.kt                
│   │   └── PlantDetail.kt          
│   ├── network/
│   │   ├── ApiService.kt            
│   │   └── RetrofitClient.kt        
│   ├── ui/
│   │   ├── home/HomeFragment.kt
│   │   ├── explore/
│   │   │   ├── ExploreFragment.kt
│   │   │   └── PlantDetailActivity.kt
│   │   ├── garden/
│   │   │   ├── GardenFragment.kt
│   │   │   ├── GardenPlant.kt      
│   │   │   └── GardenDatabase.kt   
│   │   └── about/AboutFragment.kt
│   └── viewmodel/
│       ├── PlantViewModel.kt
│       └── GardenViewModel.kt
└── res/
    ├── layout/                      # All XML layout files
    ├── menu/bottom_nav_menu.xml
    ├── navigation/nav_graph.xml
    ├── drawable/                    # Icons and shape drawables
    └── values/
        ├── colors.xml
        ├── strings.xml
        ├── themes.xml
        └── dimens.xml
```

---

## 🔌 API Reference

This app is powered by the **[Perenual Plant API](https://perenual.com/docs/api)**.

### Base URL
```
https://perenual.com/api/v2/
```

### Endpoints Used

#### 1. Species List
```
GET /species-list?key=YOUR_API_KEY
```

| Parameter | Type | Description |
|---|---|---|
| `key` | string | **Required.** Your API key |
| `page` | integer | Page number (default: 1) |
| `q` | string | Search query (e.g. `monstera`) |
| `edible` | boolean | Filter edible plants (`1`) |
| `poisonous` | boolean | Filter poisonous plants (`1`) |
| `cycle` | string | `perennial`, `annual`, `biennial`, `biannual` |
| `watering` | string | `frequent`, `average`, `minimum`, `none` |
| `sunlight` | string | `full_shade`, `part_shade`, `full_sun` |
| `indoor` | boolean | Filter indoor plants (`1`) |
| `hardiness` | integer | Hardiness zone `1`–`13` |
| `order` | string | Sort order: `asc` or `desc` |

#### 2. Species Detail
```
GET /species/details/{id}?key=YOUR_API_KEY
```

Returns full detail for a single plant by its ID, including family, origin, dimensions, soil, pest info, description, and more.

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| **Kotlin** | Primary language |
| **MVVM Architecture** | ViewModel + LiveData pattern |
| **Retrofit 2** | API networking |
| **Gson** | JSON parsing |
| **Glide** | Image loading & caching |
| **Room Database** | Local storage for Virtual Garden |
| **Navigation Component** | Bottom nav fragment routing |
| **ViewBinding** | Type-safe view access |
| **Coroutines** | Async API calls |
| **Material Components** | UI components (Chips, Cards, FAB) |
| **OkHttp Logging** | Network request debugging |

---

## ⚙️ Setup & Installation

### Prerequisites
- Android Studio **Hedgehog (2023.1.1)** or newer
- JDK 8+
- Android SDK API **24+**
- Internet connection

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/herbify.git
   cd herbify
   ```

2. **Open in Android Studio**
   - File → Open → select the `herbify` folder

3. **Add your API Key**

   Open `RetrofitClient.kt` and replace the key:
   ```kotlin
   const val API_KEY = "your-api-key-here"
   ```

4. **Sync Gradle**
   - Click **Sync Now** when prompted, or go to File → Sync Project with Gradle Files

5. **Create Vector Assets**

   Go to **File → New → Vector Asset** and create:
   - `ic_home`, `ic_explore`, `ic_garden` (use `nature`), `ic_info`
   - `ic_search`, `ic_water` (use `water_drop`), `ic_sun` (use `wb_sunny`)
   - `ic_back` (use `arrow_back`), `ic_delete`, `ic_herb_logo` (use `eco`)

6. **Build & Run**
   - Connect a device or start an emulator
   - Press **Run ▶** or `Shift + F10`

---

## 📦 Dependencies

Add these to your `app/build.gradle`:

```gradle
dependencies {
    // Navigation
    implementation 'androidx.navigation:navigation-fragment-ktx:2.7.4'
    implementation 'androidx.navigation:navigation-ui-ktx:2.7.4'

    // Lifecycle
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.6.2'

    // Retrofit + Gson
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.11.0'

    // Glide
    implementation 'com.github.bumptech.glide:glide:4.16.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.16.0'

    // Room
    implementation 'androidx.room:room-runtime:2.6.0'
    implementation 'androidx.room:room-ktx:2.6.0'
    kapt 'androidx.room:room-compiler:2.6.0'

    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'

    // Material
    implementation 'com.google.android.material:material:1.10.0'
}
```

---

## ⚠️ Important Constraints & Disclaimers

> These are displayed in the **About** section of the app:

1. Always consult a healthcare professional before using any herbal remedy for medical purposes.
2. Some plants marked as medicinal may be toxic in large doses. Follow recommended guidelines.
3. Pregnant or breastfeeding women should avoid self-medicating with herbal plants.
4. Children under 12 should not consume concentrated herbal extracts without medical advice.
5. Plant information is for **educational purposes only** and is not a substitute for professional advice.
6. Some herbs may interact with prescription medications — always disclose herb use to your doctor.

---

## 🗂️ Key Files Quick Reference

| File | Location | Purpose |
|---|---|---|
| `RetrofitClient.kt` | `network/` | API base URL + key config |
| `ApiService.kt` | `network/` | All API endpoint definitions |
| `PlantViewModel.kt` | `viewmodel/` | Manages plant list + detail state |
| `GardenViewModel.kt` | `viewmodel/` | Manages local garden CRUD |
| `GardenDatabase.kt` | `ui/garden/` | Room DB setup + DAO |
| `nav_graph.xml` | `res/navigation/` | Bottom nav routing |
| `themes.xml` | `res/values/` | App-wide style definitions |
| `colors.xml` | `res/values/` | Full green + amber color palette |

---

## 🐛 Common Errors & Fixes

| Error | Fix |
|---|---|
| `Unresolved reference 'ItemPlantBinding'` | Enable `viewBinding true` in `build.gradle`, then Clean + Rebuild |
| `import android.R` causing drawable errors | Change to `import com.example.herbify.R` |
| `Unresolved reference 'chipGroup'` | XML ID is `chip_group_filter` → use `binding.chipGroupFilter` |
| `ActivitySplashBinding` unresolved | Wrong import — import `databinding.ActivitySplashBinding` not `ActivityMainBinding` |
| Room build errors | Add `id 'kotlin-kapt'` plugin and use `kapt` instead of `annotationProcessor` for Room compiler |

---

## 📄 License

```
MIT License

Copyright (c) 2024 Herbify

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software.
```

---

## 🙏 Credits

- Plant data powered by **[Perenual API](https://perenual.com)**
- Icons from **Material Design Icons**
- Images loaded via **Glide**

---

<p align="center">Made with 🌿 and Kotlin</p>
