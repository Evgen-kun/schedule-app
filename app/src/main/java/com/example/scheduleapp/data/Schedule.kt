package com.example.scheduleapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.time.Duration.Companion.minutes

@Entity
data class Schedule(
    @PrimaryKey val id : UUID = UUID.randomUUID(),
    var groupID: UUID = UUID.randomUUID(),
    var date: Date = Date(),
    var discipline : String = "",
    var teacherName : String = "",
    var position : String = "",
    var duration : Int = 0,
    var classroom : String = "",
    var building : Int = 0,
    var coupleNumber : Int = 0,
    var type : String = ""
    )
{
    val day : String
        get() {
            val gregorianCalendar1 = GregorianCalendar()
            gregorianCalendar1.timeInMillis = date.time
            val d = when (gregorianCalendar1.get(GregorianCalendar.DAY_OF_WEEK)) {
                1 -> "Воскресенье"
                2 -> "Понедельник"
                3 -> "Вторник"
                4 -> "Среда"
                5 -> "Четверг"
                6 -> "Пятница"
                7 -> "Суббота"
                else -> "Не определено"
            }
            return d
        }

    val time : String
        get() {
            val gregorianCalendar1 = GregorianCalendar()
            gregorianCalendar1.timeInMillis = date.time
//            val t = "${gregorianCalendar1.get(GregorianCalendar.HOUR_OF_DAY)}:${gregorianCalendar1.get(GregorianCalendar.MINUTE)}"
//            return t
            val t = when (coupleNumber) {
                1 -> "8:00 - 9:20"
                2 -> "9:30 - 10:50"
                3 -> "11:10 - 12:30"
                4 -> "12:40 - 14:00"
                5 -> "14:10 - 15:30"
                6 -> "15:40 - 17:00"
                7 -> "17:10 - 18:30"
                8 -> "18:40 - 20:00"
                9 -> "20:10 : 21:30"
                else -> "Не определено"
            }
            return t
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Schedule

        if (id != other.id) return false
        if (date != other.date) return false
        if (discipline != other.discipline) return false
        if (teacherName != other.teacherName) return false
        if (position != other.position) return false
        if (duration != other.duration) return false
        if (classroom != other.classroom) return false
        if (building != other.building) return false
        if (coupleNumber != other.coupleNumber) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + discipline.hashCode()
        result = 31 * result + teacherName.hashCode()
        result = 31 * result + position.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + classroom.hashCode()
        result = 31 * result + building.hashCode()
        result = 31 * result + coupleNumber.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}
