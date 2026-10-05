# My AI

## 📌 Description

My AI is a mobile conversational application built to deliver an accessible, intuitive platform for real-time interactions with an AI assistant. The application allows users to securely log in, initiate chat sessions, and receive instant automated responses within a clean messaging workflow. Designed for daily convenience, it offers a seamless experience for handling quick inquiries, problem-solving, and everyday conversations.

---

## 🛠️ Tech Stack

| Category                    | Technologies Used                               |
| :-------------------------- | :---------------------------------------------- |
| 🌐 **Programming Language** | `Kotlin`                                        |
| 🧩 **Framework**            | `Jetpack Compose`                               |
| ⚛️ **Libraries**            | `Google AI Android SDK`, `System UI Controller` |
| 🤖 **Generative AI Model**  | `Google Gemini`                                 |
| 👾 **IDE**                  | `Android Studio`                                |

---

## ⚙️ Setup Instructions

1. **Prerequisites**
   - Gradle JDK 21 (Eclipse Temurin) installed on your system.
   - Git installed on your system.
   - Android Studio IDE with the following SDK components installed:
     - Android SDK (Android 16.0 / API Level 36)
     - Android SDK Build-Tools (Version 36)
     - Android SDK Command-line Tools
     - Android Emulator
     - Android SDK Platform-Tools
   - An active [Google AI Studio](https://aistudio.google.com) account and API Key.

2. **Google AI API Key Setup**
   - Visit the official [Google AI Studio](https://aistudio.google.com) website.
   - Navigate to the **Dashboard** menu on the sidebar, then select **API Keys**.
   - Click **Create API key**, enter a name for your key and project, then click **Create key**.
   - Copy the generated **API Key** value to use during environment configuration.

3. **Clone the Repository**

```bash
git clone https://github.com/Fikri-Rouzan/my-ai.git
cd my-ai
```

4. **Configure Local Properties**

   Create or edit the `local.properties` file in the root directory of the project, then add the following line:

   ```properties
   API_KEY="YOUR_API_KEY"
   ```
