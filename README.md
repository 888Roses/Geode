![Banner](/banner.png)

&nbsp;
&nbsp;
&nbsp;
&nbsp;
[![Fabric Icon](https://cdn.modrinth.com/data/cached_images/eb2e255e5856e26b67266ee48a4d5dfc0b0b65b1_0.webp)](https://modrinth.com/mod/fabric-api)
[![Discord Icon Link](https://cdn.modrinth.com/data/cached_images/2078d412754ba08736b0cd1b8b315f3d5a12bc9f_0.webp)](https://discord.gg/kTz6gjMWEr)
[![CCA Icon](https://cdn.modrinth.com/data/cached_images/b9185edc5ba385bb3a7b5b74619367b8471df591_0.webp)](https://modrinth.com/mod/cardinal-components-api)

> [!Important]
> Geode is a library mod. It won't do anything on it's own, only add it if other mods require it.

> [!CAUTION]
> **Geode will not be ported to Forge/NeoForge/Quilt**. It will also **not be backported** and **only be maintained in the newest available version**.

# 🤔 What is Geode?
Geode is a library enhancing many aspects of Minecraft modding, including:
- 🛠️ **Powerful Builder Based Registration**<br/>A new way of registering your items, blocks, entities and more using builders, all in one centralized Geode instance.
- 📐 **Extensive Math API**<br/>A large collection of common Math functions, as well as _niche_ ones such as the inverse square root.
- 📖 **Plenty Helpers**<br/>Many many helper classes to aid with every aspect of minecraft development, from raymarching to getting an identifier from registries.

# 💻 Importing Geode
You may import Geode in your repositories using the Modrinth API:

In your `gradle.properties` file:
```properties
# Geode Version.
geode = 1.0.0 # Add the version here
```

In your `build.gradle` file:
```groovy
repositories {
    // Add the modrinth maven
    maven { url = "https://api.modrinth.com/maven" }
}

dependencies {
    // Adding a Geode dependency for Fabric.
    modImplementation include("maven.modrinth:geode:fabric-${project.geode}")
}
```

<br/>
<hr/>

<sub>@ 2026 The Collective. All Rights Reserved.</sub><br/>
<sub>Geode is licensed under Apache-2.0.</sub>

