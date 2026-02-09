# Pomodoro Focus

PomodoroFocus is a minimalistic timer application, distinguished by its unique **Custom LED Matrix Design**. Built to boost productivity using the standard **25-minute Pomodoro Technique**, it utilizes **Local Data Storage** to securely track your **Completed Tasks**. The app's responsive, custom-built interface ensures a focused, distraction-free experience.

<a href="https://github.com/MalichGrigoriy/Pomodoro/releases/latest">
  <img height="100" alt="Get it on GitHub" src="https://github.com/user-attachments/assets/e5aa56e5-80be-4c7c-8cfb-c547909d0b80" />
</a>

## Screenshots

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/71d48991-9856-475e-9df9-b8059dd7d0a1" width="200" alt="Timer Screen Screenshot 1" /></td>
    <td><img src="https://github.com/user-attachments/assets/6926f082-b180-42f6-a3df-53e07b416e12" width="200" alt="Timer Screen Screenshot 2" /></td>
    <td><img src="https://github.com/user-attachments/assets/e2d9c5b5-c40c-4dbd-8b45-17794f2ae3ae" width="200" alt="Timer Screen Screenshot 3" /></td>
    <td><img src="https://github.com/user-attachments/assets/128cc881-7c52-459c-9468-959a7b9b0b70" width="200" alt="Statistic Screen Screenshot 1" /></td>
  </tr>
    <tr>
    <td><img src="https://github.com/user-attachments/assets/da42810d-d492-418d-ab3f-d442d5a8c145" width="200" alt="Statistic Screen Screenshot 2" /></td>
    <td><img src="https://github.com/user-attachments/assets/6461623f-ca81-436e-870f-13e16223bda2" width="200" alt="Settings Screen Screenshot 1" /></td>
    <td><img src="https://github.com/user-attachments/assets/5b6e7531-089d-4b68-b0e4-80a4529a45f0" width="200" alt="Settings Screen Screenshot 2" /></td>
    <td><img src="https://github.com/user-attachments/assets/9ab9d280-e9df-488f-8a2f-2982648c0e82" width="200" alt="Settings Screen Screenshot 3" /></td>
  </tr>
</table>

## Technical Overview

- **Custom Compose Graphics**: Developed as a practice and demonstration of Jetpack Compose, this project features a fully custom [**LED Matrix Timer**](app/src/main/java/com/timemanager/pomodorofocus/ui/timer/pixelView/LedMatrixTimer.kt). It leverages low-level **Canvas** and **DrawScope** for complex pixel-grid calculations and responsive rendering on any screen size.

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
