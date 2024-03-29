package org.cyberslavs.parser.service;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.cyberslavs.parser.entity.Tender;
import org.cyberslavs.parser.repo.TenderRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.FileWriter;
import java.io.IOException;

import java.time.Duration;
import java.util.*;

public class Parser {
    public static void main(String[] args){
    try {
        Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/postgres", "public_hysteria", "0666");
        conn.setAutoCommit(false);
        if (conn != null) {
            System.out.println("Connected to the database!");
        } else {
            System.out.println("Failed to make connection!");
        }

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

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(500));
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

            try {
                WebElement clicked_info = elem.findElement(By.cssSelector("td[headers='NAME_LINK']"));
                if (!clicked_info.findElements(By.tagName("a")).isEmpty()) {
                    HashMap<String, HashMap> dop_hash_map_inf = new HashMap<>();

                    WebDriver into_driver = new ChromeDriver(webOptions);
                    into_driver.get(clicked_info.findElement(By.tagName("a")).getAttribute("href"));
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
                        ;
                        dop_hash_map_inf.put(tds.get(0).getText(), tr_hash_map);
                    }
                    ;
                    map.put("Информация из вложенной таблицы", dop_hash_map_inf);
                    into_driver.quit();
                } else {
                    map.put("Информация из вложенной таблицы", null);
                }
                ;
            } catch (Throwable e) {
                e.printStackTrace();
            }
            tenders.add(new Tender((String) map.get(head_name.get(2)),
                    (String) map.get(head_name.get(1)),
                    (String) map.get(head_name.get(9)),
                    (String) map.get(head_name.get(8)),
                    (String) map.get(head_name.get(7)),
                    (String) map.get(head_name.get(5))));
            json_list.add(map);
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

        System.out.println(json);
        PreparedStatement ps = null;
            String sql = "INSERT INTO tenders (code,  name) VALUES (?,?)";
            ps = conn.prepareStatement(sql);

            int insertCount = 0;
            for (Tender tender : tenders) {
                ps.setString(1, (String)tender.getCode());
                //ps.setString(2, (String) tender.getDatePublish());
                //ps.setString(3, (String)tender.getDateStart());
                ps.setString(2, (String)tender.getName());
                //ps.setString(5, (String)tender.getPrice());
                //ps.setString(6, (String)tender.getDateEnd());
                ps.addBatch();
                if (++insertCount % 1000 == 0) {
                    ps.executeBatch();
                }
            }
            ps.executeBatch();

                ps.close();
                conn.close();

    }catch (Exception e){
        e.printStackTrace();
    }
    }
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
}