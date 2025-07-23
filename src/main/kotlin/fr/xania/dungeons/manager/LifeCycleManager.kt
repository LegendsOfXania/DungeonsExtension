package fr.xania.dungeons.manager

import com.typewritermc.core.extension.Initializable
import com.typewritermc.core.extension.annotations.Singleton

@Singleton
object LifeCycleManager : Initializable {
    override suspend fun initialize() {
        if (!WorldManager.worldExists()) WorldManager.worldCreate()
    }

    override suspend fun shutdown() {
        // Do something when the extension is shutdown
    }
}