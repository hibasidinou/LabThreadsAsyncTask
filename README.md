#  Thread & AsyncTask Lab — Android App

A native Android application developed using Java and XML that demonstrates background threading concepts by simulating image loading with a manual Thread and heavy computation with AsyncTask, while keeping the UI fully responsive at all times.

---

## Features

* Animated status label that updates in real time during background tasks
* Image loading simulation using a background Thread with Handler to post back to the UI thread
* Heavy computation (100-step loop) executed via AsyncTask with live ProgressBar updates
* Toast button always responsive — even while background tasks are running
* Clean yellow & baby pink Material Design theme

---

## Technologies

* Java
* XML
* Android Studio
* Handler / Looper
* AsyncTask
* BitmapFactory

---

## Project Structure

```
ThreadAsyncTaskLab/
└── app/
    └── src/
        └── main/
            ├── java/com/example/labthreadsasynctask/
            │   └── MainActivity.java
            └── res/
                └── layout/
                    └── activity_main.xml
```

---

## How It Works

The application contains a single activity with three interactive buttons.

* **Charger image (Thread)** — launches a background Thread that sleeps for 1.2 seconds to simulate a network delay, then decodes a Bitmap and posts the result back to the UI thread via a `Handler` linked to `Looper.getMainLooper()`
* **Calcul lourd (AsyncTask)** — executes a nested loop of 100 × 200 000 iterations on a worker thread, publishing progress at each step so the ProgressBar fills smoothly from 0 to 100
* **Afficher Toast** — displays a Toast instantly at any time, proving the main thread is never blocked

---

## Threading Concepts Demonstrated

| Concept | Implementation |
|---|---|
| Background Thread | `new Thread(() -> { ... }).start()` |
| UI update from Thread | `uiHandler.post(() -> { ... })` |
| AsyncTask lifecycle | `onPreExecute` → `doInBackground` → `onProgressUpdate` → `onPostExecute` |
| Progress reporting | `publishProgress(step)` inside `doInBackground` |
| Non-blocking UI | Toast responds immediately during any background task |

---

## Architecture

| Component | Role |
|---|---|
| `MainActivity.java` | Single activity managing UI references, button listeners, Thread logic, and AsyncTask inner class |
| `activity_main.xml` | Linear layout with TextView, ProgressBar, ImageView, and 3 Buttons |

---


## Min SDK

API 24 — Android 7.0 (Nougat)

---

## Demo


https://github.com/user-attachments/assets/33b01b09-eac3-49f1-ad52-a25bb3901b86


---

## Notes

This project was built to practice core Android threading concepts including creating and starting background threads manually, using Handler and Looper to safely update the UI from a non-main thread, understanding the four lifecycle methods of AsyncTask, reporting incremental progress with publishProgress, and validating UI responsiveness under concurrent background load.
