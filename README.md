# Pomodoro Focus

PomodoroFocus is a minimalistic timer application, distinguished by its unique **Custom LED Matrix Design**. Built to boost productivity using the standard **25-minute Pomodoro Technique**, it utilizes **Local Data Storage** to securely track your **Completed Tasks**. The app's responsive, custom-built interface ensures a focused, distraction-free experience.

<div align="start">
  <a href="https://github.com/MalichGrigoriy/Pomodoro/releases/latest">
    <img height="100" alt="get-it-on-github" src="https://github.com/user-attachments/assets/f2567dc7-b242-422f-b43b-3187ed51f508" />
  </a>
</div>

## Screenshots

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/7529ff99-26c0-446a-8a34-111905dc8054" width="200" /></td>
    <td><img src="https://github.com/user-attachments/assets/0abc7298-90b7-40ea-b79f-5da325287fea" width="200" /></td>
    <td><img src="https://github.com/user-attachments/assets/2ed31d85-3dab-43d3-8fa5-e7673be76b3e" width="200" /></td>
    <td><img src="https://github.com/user-attachments/assets/21a61d40-2b89-490c-8b53-7b151bd7858f" width="200" /></td>
  </tr>
    <tr>
    <td><img src="https://github.com/user-attachments/assets/f6d10749-5a03-4f57-a042-daaa9dd09764" width="200" /></td>
    <td><img src="https://github.com/user-attachments/assets/5eda4a25-484e-48eb-8456-6c5e6ffb26bf" width="200" /></td>
    <td><img src="https://github.com/user-attachments/assets/12fa1c81-b588-43eb-bb8a-2f8f1f24cd3f" width="200" /></td>
    <td><img src="https://github.com/user-attachments/assets/3760b17f-80cb-4ed2-a034-428dfcfe4819" width="200" /></td>
  </tr>
</table>

## Technical Overview

- **Custom Compose Graphics**: Implemented a fully custom **LED Matrix Timer** using low-level Compose **Canvas** and **DrawScope**, carrying out complex pixel-grid calculations for responsive rendering on any screen size.

- **Clean Architecture & MVVM**: Strict separation of concerns (Domain/Data/UI) using **UseCases** and **Unidirectional Data Flow (UDF)**.

- **Modern Stack**: 100% **Kotlin** + **Jetpack Compose** following **Single Activity** architecture and **Material 3** design systems.

- **Data Persistence**: Optimized local storage using **Room** integrated with **Paging 3** for efficient handling of large datasets.

- **State Management**: robust UI state handling using **ViewModel**, **StateFlow**, and the **State Holder** pattern.

- **Coroutines & Flow**: Managed concurrency and reactive streams.

- **Hilt**: Dependency Injection for decoupling components.

## Future Improvements (Planned)

- **Type-Safe Navigation**: Migrating from string-based routes to the new **Navigation 3** with control of back stack.
  
- **Background Execution**: Implementing **Foreground Services** with Android 14+ compatibility to ensure the timer runs reliably even when the app is minimized or the screen is off.
  
- **App Widgets (Glance)**: Creating reactive Home Screen and Lock Screen widgets using **Jetpack Glance** for quick timer access.
  
- **Cloud Synchronization & Auth**: Adding user accounts (e.g., Firebase Auth) and cross-device sync (Offline-First architecture) to allow seamless transition between devices.
  
- **CI/CD Pipeline**: Automated GitHub Actions for linting and testing.
