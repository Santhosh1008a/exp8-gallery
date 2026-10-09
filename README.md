# FRAME — Android Photography Gallery (Experiment 8)

[![Android SDK](https://img.shields.io/badge/Android%20SDK-37-brightgreen.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)
[![License](https://img.shields.io/badge/License-MIT-orange.svg)](LICENSE)

An editorial photography gallery application developed for **Android Experiment 8: "Implement Menus and WebView in an Android Application"**.

Built using native Kotlin and Android XML, **FRAME** showcases modern 2026 dark editorial UI design, local WebP photographic asset management, a 3x3 `GridView` with a custom adapter, native Options menus, PopupMenus, and an in-app WebView archive.

---

## 📱 Features & Experiment Highlights

### 1. 3×3 Photography Gallery (`GridView`)
- Custom `FrameGridAdapter` (`BaseAdapter`) rendering nine local, high-resolution WebP photographs (`frame_coastal_cliff.webp`, `frame_winding_road.webp`, etc.) in a portrait 3x3 grid.
- Full-bleed photo cards with 10dp rounded corners, subtle dark borders, and tight spacing.
- Central selection state tracking (`X / 9 SELECTED`) with electric-lime checkmark badges on selected tiles.

### 2. Category Filter Pills
- Floating category scroll container ("Mixed", "Ocean", "Road", "Creative") positioned above bottom controls.
- Dynamic filtering updates the GridView in real time.

### 3. Native Options Menu
- Top-left options menu control invoking real Android `onCreateOptionsMenu` / `onOptionsItemSelected` popup menu.
- Options include:
  - **Select All**: Selects all 9 items and updates counter to `9 / 9 SELECTED`.
  - **Deselect All**: Clears selection and updates counter to `0 / 9 SELECTED`.
  - **Open Web Archive**: Launches `WebViewActivity`.
  - **About Gallery**: Displays an architectural dialog describing the project.

### 4. Image Popup Menu
- Real Android `PopupMenu` anchored to individual card 3-dot buttons with dark circular scrims for legibility.
- Actions: `View Image`, `Select Photo` / `Deselect Photo`, and `Photo Details`.
- Independent click anchor prevents card tap events from firing accidentally.

### 5. Detail View Activity (`DetailActivity`)
- Full-screen photo inspection view displaying the photograph, title, resource filename, selection status, and detailed editorial description.
- Allows selection toggling directly within the inspection view and sends result callbacks back to `MainActivity`.

### 6. In-App WebView Activity (`WebViewActivity`)
- In-app browser loading secure HTTPS web content (`https://unsplash.com`).
- Header bar with page title, back action, and refresh button.
- Horizontal loading progress bar linked to `WebChromeClient`.
- Friendly offline/error overlay state with a "Try Again" button handling load failures via `WebViewClient`.
- Browser history back navigation (`webView.canGoBack()`).

---

## 📸 Screenshots

The application includes a dark editorial gallery, photo detail inspection, and in-app web archive experience.

> Add emulator captures to a `screenshots/` directory using the filenames below to display them here:

| Gallery | Photo Details | Web Archive |
|---|---|---|
| `screenshots/gallery.png` | `screenshots/photo-details.png` | `screenshots/web-archive.png` |

```markdown
![Gallery](screenshots/gallery.png)
![Photo Details](screenshots/photo-details.png)
![Web Archive](screenshots/web-archive.png)
```

---

## 🎨 Design System: FRAME

- **Background**: Deep Charcoal (`#0D0D0E`)
- **Card Surface**: Dark Graphite (`#16161A`)
- **Typography**: Warm Off-White (`#F4F3EF`)
- **Accent**: Electric Lime (`#D4FF00`)
- **Edge-to-Edge**: Full transparent status bar and navigation bar insets.

---

## 🛠️ Project Structure

```
app/src/main/
├── java/com/example/exp8/
│   ├── MainActivity.kt             # Main gallery activity & options menu handler
│   ├── DetailActivity.kt           # Photo inspection activity
│   ├── WebViewActivity.kt          # In-app web archive activity
│   ├── FrameGridAdapter.kt         # Custom GridView BaseAdapter
│   └── FrameItem.kt                # Data model for gallery items
└── res/
    ├── drawable/                   # 9 local WebP photographs & UI icons
    ├── layout/
    │   ├── activity_main.xml       # Header, GridView, filter pills & bottom bar
    │   ├── activity_detail.xml     # Detail inspection layout
    │   ├── activity_webview.xml     # WebView, progress bar & error overlay
    │   └── grid_item_frame.xml      # Portrait tile item layout
    ├── menu/
    │   ├── menu_main.xml           # Options menu
    │   └── menu_image_item.xml     # Item PopupMenu
    └── values/
        ├── colors.xml              # Color tokens
        ├── strings.xml             # UI string resources
        └── themes.xml              # Dark editorial theme
```

---

## 🚀 Building & Running

1. Clone this repository:
   ```bash
   git clone https://github.com/Santhosh1008a/exp8-gallery.git
   ```
2. Open the project in **Android Studio 2026.1+**.
3. Sync Gradle project files (`compileSdk = 37`, `minSdk = 24`).
4. Select `app` run configuration and launch on an Android Virtual Device (API 24+) or physical device.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
