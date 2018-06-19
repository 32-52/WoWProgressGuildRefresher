import org.jsoup.Jsoup
import org.pmw.tinylog.Logger

class Player(val nickname: String,
             private val url: String) : Runnable {
    init {
        Logger.info("Создан объект игрока с параметрами: ${toString()}")
        counter.incrementPlayer()
    }

    override fun run() {
        updatePlayer()
    }

    private fun updatePlayer() {
        Logger.info("Обновление игрока $nickname...")
        Jsoup.connect(url).data("update", "1").post()
        Logger.info("Обновление игрока $nickname завершенно")
    }

    override fun toString(): String {
        return "Player(nickname='$nickname', url='$url')"
    }
}
