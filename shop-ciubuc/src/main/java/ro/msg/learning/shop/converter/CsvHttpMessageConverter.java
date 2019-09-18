package ro.msg.learning.shop.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import ro.msg.learning.shop.util.CsvConverter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class CsvHttpMessageConverter extends AbstractGenericHttpMessageConverter {

    private final CsvConverter csvConverter;

    public CsvHttpMessageConverter() {
        super(new MediaType("text", "csv"));

        this.csvConverter = new CsvConverter();
    }

    @Override
    protected void writeInternal(Object o, Type type, HttpOutputMessage httpOutputMessage) throws HttpMessageNotWritableException, IOException {

        List<Object> arrayList;

        if (o instanceof ArrayList)
            arrayList = new ArrayList<>((ArrayList<Object>) o);
        else {
            arrayList = Collections.singletonList(o);
        }

        csvConverter.toCsv(arrayList.get(0).getClass(), arrayList, httpOutputMessage.getBody());
    }

    @Override
    protected Object readInternal(Class aClass, HttpInputMessage httpInputMessage) throws HttpMessageNotReadableException, IOException {
        return csvConverter.fromCsv(aClass, httpInputMessage.getBody());
    }

    @Override
    public Object read(Type type, Class aClass, HttpInputMessage httpInputMessage) throws HttpMessageNotReadableException, IOException {
        return readInternal(aClass, httpInputMessage);
    }
}