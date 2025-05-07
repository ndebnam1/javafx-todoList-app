## ✅ JavaFX To-Do List App

A simple JavaFX-based to-do list desktop application.

---

### Requirements

* **Java 24 (JDK 24)**
  Make sure `java -version` outputs something like:

  ```
  java version "24.0.x"
  ```

* **Maven Daemon (mvnd)**
  Install from: [https://github.com/mvndaemon/mvnd](https://github.com/mvndaemon/mvnd)
  Once installed, ensure `mvnd` is in your PATH:

  ```bash
  mvnd -v
  ```

* **JavaFX 23.0.1**
  No manual installation needed — dependencies are pulled automatically via Maven.

---

### ⚙️ Setup

1. **Clone the repository:**

   ```bash
   git clone https://github.com/ndebnam1/javafx-todoList-app.git
   cd javafx-todoList-app
   ```

2. **Run the app:**

   ```bash
   mvnd javafx:run
   ```

   If this fails due to Java version issues, double-check that Java 24 is set as your `JAVA_HOME`.

---

### 📦 Troubleshooting

* If you get an error like:

  ```
  invalid source release 23 with --enable-preview
  ```

  That means you're **not using Java 24**. Upgrade to JDK 24 and set `JAVA_HOME` correctly.

* To set `JAVA_HOME` on Windows:

  * Open *System Properties > Environment Variables*
  * Add a new **System variable**:

    ```
    Name: JAVA_HOME
    Value: C:\Program Files\Java\jdk-24
    ```
  * Restart VS Code or your terminal.

---
