# TataConnect 🏠🤝

TataConnect is a modern Android application designed to bridge the gap between families looking for domestic services and housekeepers/employees seeking work. Whether you're looking for a nanny, a cook, or a full-time housekeeper, TataConnect provides a secure and intuitive platform to find your perfect match.

## 🚀 Features

### For Families (Employers)
- **Profile Completion:** Set your family size, specific service needs (cooking, laundry, pet care), and budget range (FCFA/hr).
- **Browse Professionals:** Search through a verified list of employees with detailed skill sets.
- **Direct Communication:** Chat in real-time with potential candidates to discuss details.

### For Employees (Professionals)
- **Professional Resume:** Showcase your years of experience, specific skills, and expected salary.
- **Job Dashboard:** View interested families and manage your incoming inquiries.
- **Availability Status:** Toggle your status to let families know when you are ready for work.

### General Features
- **Secure Authentication:** Firebase-powered login and registration with mandatory **Email Verification** for safety.
- **Real-time Messaging:** Fast and private chat system built on Cloud Firestore.
- **Material 3 UI:** A clean, modern interface using Material Design 3 components, floating labels, and responsive feedback.
- **Offline Support:** Local caching of user data for a smoother experience.

## 🛠 Tech Stack

- **Language:** Java
- **UI Framework:** XML (Material Design 3)
- **Backend:** Firebase (Authentication, Cloud Firestore, Storage)
- **Architecture:** MVVM-ready structure with Room Database for local persistence.
- **Build System:** Gradle

## 📥 Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/TataConnect.git
   ```

2. **Firebase Setup:**
   - Create a project on the [Firebase Console](https://console.firebase.google.com/).
   - Download the `google-services.json` file.
   - Place it in the `app/` directory of the project.
   - Enable **Email/Password** authentication in the Firebase console.
   - Create a **Firestore Database** in your preferred region.

3. **Configure Security Rules:**
   Paste the following rules into your Firestore "Rules" tab to ensure user data privacy:
   ```javascript
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /users/{userId} {
         allow read: if request.auth != null;
         allow write: if request.auth != null && request.auth.uid == userId;
       }
       match /chats/{chatId}/messages/{messageId} {
         allow read, write: if request.auth != null && chatId.contains(request.auth.uid);
       }
     }
   }
   ```

4. **Run the app:**
   Open the project in **Android Studio** and click **Run**.

## 🛡 Security
TataConnect prioritizes user safety by enforcing:
- **Email Verification:** Users must verify their email before accessing the dashboard.
- **Authenticated Access:** Only logged-in users can view profiles or send messages.
- **Private Data:** Sensitive information is only editable by the owner of the account.

---
*Built with ❤️ for a safer and more connected domestic service industry.*
