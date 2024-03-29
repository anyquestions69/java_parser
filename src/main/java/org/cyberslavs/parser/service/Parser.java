package org.cyberslavs.parser.service;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.cyberslavs.parser.entity.Additional;
import org.cyberslavs.parser.entity.Tender;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Parser {
    private static SessionFactory factory;
    public static void main(String[] args){
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 2);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        Timer timer = new Timer();
        timer.schedule(task, today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
    }
    static TimerTask task = new TimerTask() {
        public void run() {

            try {
                factory = new Configuration().addAnnotatedClass(Tender.class).addAnnotatedClass(Additional.class).configure().buildSessionFactory();
                Session session = factory.openSession();
                Transaction tx = null;

                WebDriver driver;
                ChromeOptions webOptions;
                WebDriverManager.chromedriver().setup();

                System.setProperty("webdriver.chrome.driver", "/Users/public_hysteria/Downloads/chromedriver-mac-arm64/chromedriver");
                webOptions = new ChromeOptions();
                webOptions.addArguments("--headless");
                webOptions.addArguments("--ignore-certificate-errors");
                driver = new ChromeDriver(webOptions);
                driver.get("https://etp.tatneft.ru/pls/tzp/f?p=220:562:11281430464650::::P562_OPEN_MODE,GLB_NAV_ROOT_ID,GLB_NAV_ID:,12920020,12920020");
                List<Tender> tenders = new ArrayList<>();
                List<String> option_list = Arrays.asList("Предложение", "Тендер", "Завершенные", "Материалы");

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(300));
                wait.pollingEvery(Duration.ofMillis(250));
                for (int i = 0; i < 3; i++) {
                    List<WebElement> selectors_panel = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("select.selectlist"))).subList(0, 3);
                    selectors_panel.get(i).click();
                    try {
                        List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("option")));
                        for (WebElement elem : options) {
                            if (elem.getText().equals(option_list.get(i))) {
                                elem.click();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Ошибка при взаимодействии с элементом: " + e.getMessage());
                    }
                }
                ;

                WebElement table = driver.findElement(By.cssSelector("table.a-IRR-table")).findElement(By.tagName("tbody"));
                List<WebElement> target_list = table.findElements(By.tagName("tr"));
                List<HashMap> json_list = new ArrayList<>();
                List<String> head_name = new ArrayList<>();

                for (WebElement elem : target_list.get(0).findElements(By.className("a-IRR-headerLabel"))) {
                    head_name.add(elem.getText());
                }
                ;
                System.out.println(head_name);
                for (WebElement elem : target_list.subList(1, target_list.size())) {
                    List<WebElement> into_list = elem.findElements(By.tagName("td"));
                    HashMap<String, Object> map = new HashMap<>();

                    for (int i = 1; i < into_list.size(); i++) {
                        WebElement web_elem = into_list.get(i);
                        String key = head_name.get(i).replace("\n", " ");
                        String val = extractTextFromElement(web_elem);
                        map.put(key, val);
                    }
                    ;
                    String href;
                    List<Additional> add=new ArrayList<Additional>();
                    try {
                        WebElement clicked_info = elem.findElement(By.cssSelector("td[headers='NAME_LINK']"));
                        if (!clicked_info.findElements(By.tagName("a")).isEmpty()) {
                            HashMap<String, HashMap> dop_hash_map_inf = new HashMap<>();

                            WebDriver into_driver = new ChromeDriver(webOptions);
                            into_driver.get(clicked_info.findElement(By.tagName("a")).getAttribute("href"));
                            href=clicked_info.findElement(By.tagName("a")).getAttribute("href");
                            WebElement dop_info_table = into_driver.findElement(By.className("ReportTbl"));

                            List<WebElement> data_blocks = dop_info_table.findElements(By.tagName("tbody"));

                            List<String> pre_table_heads = new ArrayList<>();

                            for (WebElement pre_elem : data_blocks.get(0).findElements(By.tagName("th"))) {
                                pre_table_heads.add(pre_elem.getText());
                            }
                            ;

                            for (WebElement tr : data_blocks.get(1).findElements(By.tagName("tr"))) {
                                List<WebElement> tds = tr.findElements(By.tagName("td"));
                                HashMap<String, String> tr_hash_map = new HashMap<>();
                                for (int j = 1; j < tds.size(); j++) {
                                    tr_hash_map.put(pre_table_heads.get(j).replace("\n", " "), tds.get(j).getText());
                                }
                                if((String)tr_hash_map.get(tds.get(2).getText())==null){
                                    add.add(new Additional(tr_hash_map.get("Наименование"), 0));

                                }else{
                                    add.add(new Additional(tr_hash_map.get("Наименование"), Integer.valueOf(tr_hash_map.get("Кол-во"))));

                                }

                                ;
                                dop_hash_map_inf.put(tds.get(0).getText(), tr_hash_map);
                            }
                            ;
                            map.put("Информация из вложенной таблицы", dop_hash_map_inf);
                            into_driver.quit();
                        } else {
                            map.put("Информация из вложенной таблицы", null);
                            href="";
                        }
                        ;
                    Tender tender= new Tender((String) map.get(head_name.get(2)),
                            (String) map.get(head_name.get(1)),
                            (String) map.get(head_name.get(9)),
                            (String) map.get(head_name.get(8)),
                            (String) map.get(head_name.get(7)),
                            (String) map.get(head_name.get(5)),
                            href
                    );
                    tenders.add(tender);
                    tender.setAdditional(add);
                    json_list.add(map);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                ;
                driver.quit();

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(json_list);

                try (FileWriter file = new FileWriter("./data_tatneft.json")) {
                    file.write(json);
                    System.out.println("Successfully Copied JSON Object to File...");
                    System.out.println("\nJSON Object: " + json);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //System.out.println(json);
                tx = session.beginTransaction();
                for(Tender tender:tenders){
                    session.save(tender);
                    for(Additional add: tender.getAdditional()){
                        add.setTender(tender);
                        session.save(add);
                    }
                    System.out.println(tender);
                }

                tx.commit();

            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("Task performed on: " + new Date() + "n" +
                "Thread's name: " + Thread.currentThread().getName());
            SecondParser();
        }


    };

    private static String extractTextFromElement(WebElement element) {
        List<WebElement> children = element.findElements(By.xpath("./*"));

        if (children.isEmpty()) {
            return element.getText();
        } else {
            StringBuilder text = new StringBuilder();
            for (WebElement child : children) {
                text.append(extractTextFromElement(child));
            }
            return text.toString();
        }
    }

    public static void SecondParser(){
        Session session = factory.openSession();
        Transaction tx = null;
        String url = "https://etp.metal-it.ru/trades?documents=N4IgLgTghgJgpgZTlCBjAFgbQA4Fc3pQDOcRAdCShgLogBcoEpuANmALID289oAblBa449cFABGLEQF8ANCE4R4EXiACWMegHZ5YNWCmjAhCCB%2BEEB8IIEEQM4EYQAASB2EEAMIIC4QQMIgJ54A4QEPIC2UMABJVE4AO1EUCE4AdwB9XGxolE15bAg1fwgATwBRAA800iI1MNViVDhQmDVQgHN6SGFUqOw4CDAs0QAFACUAeQBhAFUenPYcgDkAFVjxqYAJPoAREDkQOAKmImKwonpMUHLK6rqGiCaQNM5W9s66EF7BkbHJmbnFlelqaR%2BgA";
        WebDriver driver;
        ChromeOptions webOptions;
        WebDriverManager.chromedriver().setup();

        System.setProperty("webdriver.chrome.driver", "/Users/public_hysteria/Downloads/chromedriver-mac-arm64/chromedriver");
        webOptions = new ChromeOptions();
        webOptions.addArguments("--headless");
        webOptions.addArguments("--ignore-certificate-errors");
        driver = new ChromeDriver(webOptions);
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(500));
        wait.pollingEvery(Duration.ofMillis(200));

        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("table")));

        List<WebElement> th_header = table.findElement(By.tagName("thead")).findElements(By.tagName("th"));
        List<Object> cols_name_header = new ArrayList<>();
        for(WebElement elem: th_header) {
            cols_name_header.add(elem.getText());
        };

        List<WebElement> list_page_btn = driver.findElements(By.cssSelector("um-paginator div.ng-star-inserted > div.ng-star-inserted"));

        List<HashMap> data_list = new ArrayList<>();
        for(int i = 0; i < list_page_btn.size(); i++) {
            driver.findElements(By.cssSelector("um-paginator div.ng-star-inserted > div.ng-star-inserted")).get(i).click();

            WebElement t_body = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table > tbody")));
            List<WebElement> list_page_tr = t_body.findElements(By.tagName("tr"));
            for(WebElement tr: list_page_tr) {

                List<WebElement> tds = tr.findElements(By.tagName("td"));
                HashMap<String, Object> inner_data_tr = new HashMap<>();
                for(int j = 1; j < tds.size(); j++) {
                    if(j == 1) {
                        inner_data_tr.put( "НОМЕР", tds.get(j).findElement(By.tagName("div")).getText() );
                        inner_data_tr.put( "НАИМЕНОВАНИЕ", tds.get(j).findElement(By.tagName("span")).getText() );
                    } else {
                        inner_data_tr.put( (String) cols_name_header.get(j), tds.get(j).getText() );
                    }
                }

                WebDriver new_driver = new ChromeDriver(webOptions);
                new_driver.get( tr.findElement(By.tagName("a")).getAttribute("href") );

                WebDriverWait new_wait = new WebDriverWait(new_driver, Duration.ofMillis(500));
                new_wait.pollingEvery(Duration.ofMillis(200));
                WebElement intro_table = new_wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".k-grid-aria-root")));

                List<String> th_name_intro_table = Arrays.asList("№", "Наименование, технические характеристики", "Характеристики", "Количество", "Ед. изм.", "Заказчик", "Потребитель", "Грузополучатель", "Срок поставки", "Комментарий организатора");
                List<WebElement> intro = intro_table.findElement(By.cssSelector("table.k-grid-table")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
                HashMap<String, HashMap> target_intro_hashmap = new HashMap();
                for(int t = 0; t < intro.size(); t++) {
                    List<WebElement> intro_td = intro.get(t).findElements(By.tagName("td"));
                    HashMap<String, String> target_intro = new HashMap();
                    for(int k = 1; k < th_name_intro_table.size(); k++) {
                        target_intro.put( th_name_intro_table.get(k), intro_td.get(k).getText() );
                    }
                    target_intro_hashmap.put("Page_" + t , target_intro);
                }
                new_driver.quit();
                System.out.println(target_intro_hashmap);
                inner_data_tr.put("Внутренняя информация:", target_intro_hashmap);
                data_list.add( inner_data_tr );
            }
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(data_list);

        try (FileWriter file = new FileWriter("./data_metal_it.json")) {
            file.write(json);
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        driver.quit();
    }
}