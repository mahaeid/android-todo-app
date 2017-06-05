package co.netguru.todolist.data.local.converters;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Nullable;

import org.threeten.bp.LocalDate;

public class LocalDateConverter {

    @TypeConverter
    public static LocalDate fromLong(@Nullable Long epoch) {
        return epoch == null ? null : LocalDate.ofEpochDay(epoch);
    }

    @TypeConverter
    public static Long localDateToEpoch(@Nullable LocalDate localDate) {
        return localDate == null ? null : localDate.toEpochDay();
    }
}
