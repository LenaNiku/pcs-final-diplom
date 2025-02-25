import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BooleanSearchEngine implements SearchEngine {

    private final Map<String, List<PageEntry>> index = new HashMap<>();

    private void readPdf(File pdf) throws IOException {
        var doc = new PdfDocument(new PdfReader(pdf));
        var numberOfPages = doc.getNumberOfPages();

        for (int pageNumber = 1; pageNumber <= numberOfPages; pageNumber++) {

            var page = doc.getPage(pageNumber);
            String text = PdfTextExtractor.getTextFromPage(page);
            String[] words = text.split("\\P{IsAlphabetic}+");

            Map<String, Integer> freqs = new HashMap<>(); // мапа, где ключом будет слово, а значением - частота
            for (var word : words) { // перебираем слова
                if (word.isEmpty()) {
                    continue;
                }
                word = word.toLowerCase();
                freqs.put(word, freqs.getOrDefault(word, 0) + 1);
            }
            for (Map.Entry<String, Integer> entry : freqs.entrySet()) {

                PageEntry pageEntry = new PageEntry(pdf.getName(), pageNumber, entry.getValue());
                List<PageEntry> listOfPageEntry = new ArrayList<>();
                listOfPageEntry.add(pageEntry);

                index.merge(
                        entry.getKey(),
                        listOfPageEntry,
                        (listOne, listTwo) ->
                                Stream.concat(listOne.stream(), listTwo.stream()).collect(Collectors.toList()));
            }

        }
    }

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы

        for (File file: Objects.requireNonNull(pdfsDir.listFiles())) {
            readPdf(file);
        }
        for (Map.Entry<String, List<PageEntry>> entry : index.entrySet()) {
            entry.getValue().sort(PageEntry::compareTo);
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        // тут реализуйте поиск по слову
        return index.getOrDefault(word.toLowerCase(Locale.ROOT), List.of());
    }
}
