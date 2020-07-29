package context.boot.converter;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Eddie
 * @version 1.0
 * @date 2020/7/29 16:02
 */
public class DateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        System.out.println("converting");
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = simpleDateFormat.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
