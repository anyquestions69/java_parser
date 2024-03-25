package org.cyberslavs.parser.service;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.cyberslavs.parser.dto.TenderDto;
import org.cyberslavs.parser.entity.Tender;
import org.cyberslavs.parser.repo.TenderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileWriter;
import java.io.IOException;

import java.time.Duration;
import java.util.*;

public class Parser {
    WebDriver driver;
    @Autowired
    TenderRepository tenderRepository;
    public Parser(){
        this.driver = new ChromeDriver();
    }

    public List<Tender> parse() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\chromedriver-win64");
        WebDriverManager.chromedriver().setup();
        Scanner scanner = new Scanner(System.in);
        this.driver.get("https://etp.tatneft.ru/pls/tzp/f?p=220:562:10615457418812::NO:::");
        List<Tender> tenders = new ArrayList<>();
        //System.out.print("Введите вид: [ Спрос / Предложение ]\n--> ");
        //String request_type = scanner.nextLine();
        //System.out.println();
        String request_type = "Спрос";

        //System.out.print("Введите тип торгов: [ Все / Аукцион / Запрос предложений / Редукцион / Тендер / Редукцион по голландскому методу ]\n--> ");
        //String bidding_type = scanner.nextLine();
        //System.out.println();
        String bidding_type = "Все";

        //System.out.print("Введите cостояние : [ Текущие / Завершенные ]\n--> ");
        //String condition_type = scanner.nextLine();
        //System.out.println();
        String condition_type = "Текущие";

        //System.out.print("Введите класс заказа: [ Все / Оборудование / Материалы / Услуги ]\n--> ");
        //String order_class = scanner.nextLine();
        //System.out.println();
        String order_class = "Материалы";

        List<String> option_list = Arrays.asList(request_type, bidding_type, condition_type, order_class);
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        for(int i = 0; i < 3; i++) {
            // Переопределение элементов перед каждым взаимодействием
            List<WebElement> selectors_panel = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("select.selectlist"))).subList(0, 3);
            WebElement pre_val = selectors_panel.get(i);
            pre_val.click();
            try {
                WebDriverWait waitForOption = new WebDriverWait(this.driver, Duration.ofSeconds(1));
                List<WebElement> options = waitForOption.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("option")));
                for(WebElement elem: options) {
                    if(elem.getText().equals(option_list.get(i))) {
                        elem.click();
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Ошибка при взаимодействии с элементом: " + e.getMessage());
            }
        };

        //WebDriverWait waitForButton = new WebDriverWait(driver, 10);
        //WebElement btn_list = waitForButton.until(ExpectedConditions.elementToBeClickable(By.cssSelector("img[src='/i/list_gray.gif']")));
        //btn_list.click();

        WebElement table = this.driver.findElement(By.cssSelector("table.a-IRR-table")).findElement(By.tagName("tbody"));
        List<WebElement> target_list = table.findElements(By.tagName("tr"));
        List<HashMap> json_list = new ArrayList<>();
        List<String> head_name = new ArrayList<>();

        for(WebElement elem: target_list.get(0).findElements(By.className("a-IRR-headerLabel"))) {
            head_name.add(elem.getText());
        };

        for( WebElement elem: target_list.subList(1, target_list.size()) ) {
            List<WebElement> into_list = elem.findElements(By.tagName("td"));
            HashMap<String, Object> map = new HashMap<>();
            String name;
            for(int i = 1; i < into_list.size(); i++) {
                WebElement web_elem = into_list.get(i);
                String key = head_name.get(i);
                String val = extractTextFromElement( web_elem );
                map.put(key, val);
            };
            try {
                WebElement clicked_info = elem.findElement(By.cssSelector("td[headers='NAME_LINK']"));
                if( !clicked_info.findElements( By.tagName("a") ).isEmpty() ) {
                    HashMap<String, HashMap> dop_hash_map_inf = new HashMap<>();
                    clicked_info.click();
                    WebDriverWait clicked_wait = new WebDriverWait(this.driver, Duration.ofSeconds(1));
                    WebElement dop_info_table = clicked_wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ReportTbl")));
                    List<WebElement> data_blocks = dop_info_table.findElements(By.tagName("tbody"));

                    List<String> pre_table_heads = new ArrayList<>();

                    for(WebElement pre_elem: data_blocks.get(0).findElements(By.tagName("th"))) {
                        pre_table_heads.add(pre_elem.getText());
                    };

                    for(WebElement tr: data_blocks.get(1).findElements(By.tagName("tr"))) {
                        List<WebElement> tds = tr.findElements(By.tagName("td"));
                        HashMap<String, String> tr_hash_map = new HashMap<>();
                        for(int j = 1; j < tds.size(); j++) {
                            tr_hash_map.put(pre_table_heads.get(j), tds.get(j).getText());
                        };
                        dop_hash_map_inf.put(tds.get(0).getText(), tr_hash_map);
                    };
                    map.put("Информация из вложенной таблицы", dop_hash_map_inf);
                    this.driver.navigate().back();
                } else {
                    map.put("Информация из вложенной таблицы", "");
                };
            }  catch (Throwable e) {
                e.printStackTrace();
            }
            //System.out.println(head_name);
            tenders.add(new Tender((String) map.get(head_name.get(2)), (String) map.get(head_name.get(1)), "", (String) map.get(head_name.get(8)), (String) map.get(head_name.get(7)), (String) map.get(head_name.get(5))));
            json_list.add(map);
        };

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
        this.driver.quit();
        return tenders;
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