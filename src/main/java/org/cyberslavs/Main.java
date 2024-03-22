package org.cyberslavs;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://rostender.info/region/sankt-peterburg-gorod")
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .get();
        Elements tenders = doc.select(".tender-info__description");
        for (Element element : tenders.select("a"))
            System.out.println(element.text());

    }
}