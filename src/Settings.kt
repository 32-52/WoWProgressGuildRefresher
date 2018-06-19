import org.pmw.tinylog.Configurator
import org.pmw.tinylog.Level
import org.pmw.tinylog.writers.ConsoleWriter
import org.pmw.tinylog.writers.FileWriter

class Settings(val guildThreadsPoolSize: Int,
               val playerThreadsPoolSize: Int,
               val logFile:String,
               val isDebug: Boolean,
               val siteUrl: String) {
    private val loggerFormat = "{date:yyyy-MM-dd HH:mm:ss} {message}"

    init {
        if (isDebug) {
            Configurator.defaultConfig()
                    .formatPattern(loggerFormat)
                    .writer(FileWriter(logFile, true, true))
                    .addWriter(ConsoleWriter())
                    .level(Level.DEBUG).activate()
        } else {
            Configurator.defaultConfig()
                    .formatPattern(loggerFormat)
                    .writer(FileWriter(logFile, true, true))
                    .addWriter(ConsoleWriter())
                    .level(Level.INFO).activate()
        }
    }

    override fun toString(): String {
        return "Settings(guildThreadsPoolSize=$guildThreadsPoolSize, playerThreadsPoolSize=$playerThreadsPoolSize, siteUrl='$siteUrl', logFile='$logFile', isDebug=$isDebug, loggerFormat='$loggerFormat')"
    }
}