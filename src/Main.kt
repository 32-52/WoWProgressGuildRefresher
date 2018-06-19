import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.HashMap
import kotlin.collections.set
import org.pmw.tinylog.Logger

@JvmField
var counter = StatCounter()

fun main(args: Array<String>) {
    val settingsStream = Guild::class.java.getResourceAsStream("config.prop")
    val properties = Properties()
    properties.load(settingsStream)
    val settings = Settings(
            guildThreadsPoolSize = properties.getProperty("guildThreadsPoolSize").toInt(),
            playerThreadsPoolSize = properties.getProperty("playerThreadsPoolSize").toInt(),
            logFile = properties.getProperty("logFile"),
            isDebug = properties.getProperty("isDebug")!!.toBoolean(),
            siteUrl = properties.getProperty("siteUrl"))
    Logger.info("Starting WoWPGR...")
    Logger.info("settings = $settings")

    //Loading guild list
    val guildList = HashMap<String, String>()
    val guildsFile = Guild::class.java.getResource("guilds.list").readText()
    val guildsLines = guildsFile.split("\r\n")
    for (line in guildsLines) {
        val prop = line.split("=")
        println(prop)
        guildList[prop[0]] = settings.siteUrl + prop[1]
    }

    Logger.info("siteUrl: $settings.siteUrl")
    Logger.info("guildList:$guildList")

    Logger.info("Запуск WoWStat ${System.getProperty("user.dir")}...")
    val startTime = System.currentTimeMillis()
    Logger.info("Обновление списка игроков гильдий...")
    val executor = Executors.newFixedThreadPool(settings.guildThreadsPoolSize)
    for (guildKey in guildList.keys) {
        val guildWorker = Guild(name = guildKey, guildUrl = guildList[guildKey].toString(),
                playerThreadPoolSize = settings.playerThreadsPoolSize)
        executor.execute(guildWorker)
    }
    executor.shutdown()
    while (!executor.isTerminated) {
        Logger.info(counter.toString())
        Thread.sleep(1000)
    }
    val endTime = System.currentTimeMillis()
    val totalTime = (endTime - startTime) / 1000
    Logger.info("Обновление списка игроков гильдий завершенно\n")
    Logger.info("За итерацию добавленно: ${counter.getGuilds()} гильдий, " +
            "${counter.getPlayers()} игроков")
    Logger.info("Обновление заняло $totalTime секунд")
    Logger.info(counter.toString())
    counter.clearAll()
    Logger.info("Очистка счетчика")
    Logger.info(counter.toString())
}