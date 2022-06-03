package root.util

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class CommonTestConstants {

    public static final JSON_CONTENT_TYPE = 'application/json'

    public static final TIMESTAMP = 1641273825000L
    public static final CLOCK = Clock.fixed(Instant.ofEpochMilli(TIMESTAMP), ZoneId.systemDefault())
}
