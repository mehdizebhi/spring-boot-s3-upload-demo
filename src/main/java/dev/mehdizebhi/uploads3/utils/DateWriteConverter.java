package dev.mehdizebhi.uploads3.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.Date;

@WritingConverter
public class DateWriteConverter implements Converter<Date, Date> {

    @Override
    public Date convert(Date source) {
        return source;
    }
}