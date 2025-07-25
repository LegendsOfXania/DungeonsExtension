plugins {
    /* Kotlin */
    kotlin("jvm") version "2.0.21"
    /* Typewriter */
    id("com.typewritermc.module-plugin") version "1.3.0"
}

repositories {}
dependencies {}


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
