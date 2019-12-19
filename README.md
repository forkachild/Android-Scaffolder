# Android-Scaffolder

Isn't it cumbersome to scaffold a new Android Studio project from scratch? It takes hours and requires too much of repetitive work. Here comes a simple, elegant, blazing fast scaffolding tool. It is a NodeJS script which generates a project based on your requirements.

### Terminology

- **NAME** is the display name of the project as well as the project directory name
- **PACKAGE** is the package name of the app (e.g com.example.sample)
- **TEMPLATE** is the name of the template directory name
- **OUTPUT** is the directory name in which the project directory will be created. It is optional

### Prerequisites

You need a working installation of NodeJS (any modern version will do)

### Usage

```sh
cd Android-Scaffolder
node scaffold.js NAME=Sample PACKAGE=com.example.sample TEMPLATE=MVVM OUTPUT=Generated
```

It will create a directory named Generated inside which there will be another directory named Sample which contains the project.

### Templates

A basic MVVM template is provided for brevity. Creating one is pretty easy, in the following steps

- **Create** or **open** an Android Studio project
- For the sake of example, we consider the project & directory name to be **Sample** and package as **com.example.sample**
- Remove the **local.properties** file
- Press **Ctrl + Shift + R** on Windows/Linux or **Cmd + Shift + R** on a Mac
- Replace all occurences of **Sample** with **{{NAME}}**
- Replace all occurences of **com.example.sample** with **{{PACKAGE}}**
- Change the directory structure of **com/example/sample** to **{{PACKAGE}}** (e.g java/com/example/sample/SampleActivity.java will be java/{{PACKAGE}}/SampleActivity.java)
- Remove the **.gradle**, **.idea** and **gradle** directories
- Ensure that no private files like **google-services.json** and fields like **api_key** is removed
- Rename the root directory of the project to **{{NAME}}**
- Move it to the same directory as **scaffold.js**