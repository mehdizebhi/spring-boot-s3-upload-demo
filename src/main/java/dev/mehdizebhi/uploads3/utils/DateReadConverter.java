package dev.mehdizebhi.uploads3.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.Date;

@ReadingConverter
public class DateReadConverter implements Converter<Date, Date> {

    @Override
    public Date convert(Date source) {
        return source;
    }
}
