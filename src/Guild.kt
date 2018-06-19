import org.jsoup.Jsoup
import java.util.concurrent.Executors
import org.pmw.tinylog.Logger

class Guild(val name: String,
            private val guildUrl: String,
            private val playerThreadPoolSize: Int) : Runnable {

    private val playerList = HashMap<String, String>() // {Хранит ключ - никнейм игрока и значение - url

    init {
        Logger.info("Создан объект гильдия с параметрами: ${toString()}")
        counter.incrementsGuild()
    }

    override fun run() {
        Logger.info("Обновление гильдии $name ...")
        refreshPlayerList()
        val executor = Executors.newFixedThreadPool(playerThreadPoolSize)
        Logger.debug("$executor")
        for (playerData in playerList) {
            val playerWorker = Player(nickname = playerData.key, url = playerData.value)
            executor.execute(playerWorker)
        }
        executor.shutdown()
        while (!executor.isTerminated) {
            Logger.debug(executor.toString())
            Thread.sleep(1000)
        }
        Logger.info("Обновление гильдии $name завершенно")
    }

    private fun refreshPlayerList() {
        Logger.info("Очистка списка игроков для гильдии ${toString()} ...")
        playerList.clear()
        Logger.info("Очистка списка игроков для гильдии ${toString()} произведена")

        val guildRequestUrl = "$guildUrl?roster"
        Logger.info("Запрос по url на: $guildRequestUrl")
        val response = Jsoup.connect(guildRequestUrl).get()
        Logger.info("Размер ответа ${response.text().length} для гильдии ${toString()}")

        if (response.text().isNotEmpty()) {
            Logger.info("Проверка на размерность ответа для гильдии ${toString()} пройдена")
            val responseElements = response.getElementsByTag("a")
            val playerElements = responseElements.slice(IntRange(13, responseElements.size - 1))

            if (playerElements.isNotEmpty()) {
                Logger.info("Найдено ${playerElements.size} игроков в гильдии $name")
                for (elementText in playerElements) {
                    Logger.debug(elementText.toString())
                    val playerUrl = elementText.attr("abs:href")
                    val playerNickname = elementText.text()
                    playerList[playerNickname] = playerUrl
                }
                for (player in playerList) {
                    Logger.info(player.toString())
                }
            } else {
                Logger.warn("Не найдено игроков в гильдии $name")
            }
        }
    }

    override fun toString(): String {
        return "Guild(name='$name', guildUrl='$guildUrl', playerList=$playerList)"
    }
}