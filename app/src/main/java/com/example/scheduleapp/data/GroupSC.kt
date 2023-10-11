package com.example.scheduleapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class GroupSC (
    @PrimaryKey val id : UUID = UUID.randomUUID(),
    var name : String = "",
    var faculty : String = "",
    var course : Int = 0
)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GroupSC

        if (id != other.id) return false
        if (name != other.name) return false
        if (faculty != other.faculty) return false
        if (course != other.course) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + faculty.hashCode()
        result = 31 * result + course.hashCode()
        return result
    }
}