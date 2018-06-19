import java.util.concurrent.atomic.AtomicInteger

class StatCounter {
    private var players = AtomicInteger()
    private var guilds = AtomicInteger()

    fun incrementPlayer() {
        players.incrementAndGet()
    }

    fun incrementsGuild() {
        guilds.incrementAndGet()
    }

    fun getPlayers(): Int {
        return players.get()
    }

    fun getGuilds(): Int {
        return guilds.get()
    }

    fun clearAll() {
        players.set(0)
        guilds.set(0)
    }

    override fun toString(): String {
        return "StatCounter(players=${players.get()}, guilds=${guilds.get()})"
    }
}