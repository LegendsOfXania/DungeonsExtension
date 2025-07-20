plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
    id("com.typewritermc.module-plugin") version "1.3.0"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
}

repositories {}
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    paperweight.paperDevBundle("1.21.3-R0.1-SNAPSHOT")
}


group = "fr.xania"
version = "0.9.0"

typewriter {
    namespace = "legendsofxania"

    extension {
        name = "Dungeons"
        shortDescription = "Create dungeons in Typewriter."
        description = """
            |The Dungeons extension allows you to create 
            |Simple or Jigsaw Dungeons in Typewriter.
            |Create by the Legends of Xania.
            """.trimMargin()
        engineVersion = "0.9.0-beta-162"
        channel = com.typewritermc.moduleplugin.ReleaseChannel.BETA

        dependencies {
            dependency("typewritermc", "Quest")
            paper()
        }
    }
}

kotlin {
    jvmToolchain(21)
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION