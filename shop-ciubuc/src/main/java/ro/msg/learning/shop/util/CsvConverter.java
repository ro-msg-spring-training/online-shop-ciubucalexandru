package ro.msg.learning.shop.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class CsvConverter<T> {

    public List<T> fromCsv(Class<T> csvClass, InputStream csvData) throws IOException {

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(csvClass);

        MappingIterator<T> it = mapper.readerFor(csvClass).with(schema)
                .readValues(csvData);

        return it.readAll();
    }

    public void toCsv(Class<T> csvClass, List<T> itemsList, OutputStream csvOutput) throws IOException {

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(csvClass);
        ObjectWriter objectWriter = mapper.writer(schema.withLineSeparator("\n"));

        StringBuilder csvValue = new StringBuilder();
        for (T item : itemsList) {
            csvValue.append(objectWriter.writeValueAsString(item));
        }

        csvOutput.write(csvValue.toString().getBytes());
    }
}
