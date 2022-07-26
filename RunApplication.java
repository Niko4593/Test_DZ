package Lesson_7;


import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RunApplication {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void getWeather() throws IOException, InterruptedException {
        System.out.println("Write city for English: ");
        Scanner scanner = new Scanner(System.in);
        String city = scanner.nextLine();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://weatherapi-com.p.rapidapi.com/forecast.json?q="+city+"&days=3"))
                .header("X-RapidAPI-Key", "c6f0be2b08msh65efddef73a60a3p19f87cjsnfa71b2ad25cb")
                .header("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        System.out.println("########################################");

//Ниже реализовал вывод погоды на текущий день используя JsonNode.
        JsonNode City = objectMapper.readTree(response.body()).at("/location/name");
        JsonNode CurrentDay = objectMapper.readTree(response.body()).at("/current/last_updated");
        JsonNode CurrentDayTemperature = objectMapper.readTree(response.body()).at("/current/temp_c");
        JsonNode CurrentDayConditions = objectMapper.readTree(response.body()).at("/current/condition/text");
        System.out.println("In city "+City+" today: "+CurrentDay+" temperature is: "+CurrentDayTemperature+", the weather forecast: "+CurrentDayConditions);


//Пробую прочитать json из строки - выдает как я понимаю место в памяти.
         WeatherResponce weather = objectMapper.readValue(response.body(), WeatherResponce.class);
        System.out.println("============ "+weather);


//Пробую прочитать значения из списка - ошибка - как я понимаю из-за того, что "responce.body()" не является списком:
        List<WeatherResponce> weatherResponceList = objectMapper.readValue(response.body(), new TypeReference<List<WeatherResponce>>() {});
        System.out.println("++++++++++++ "+weatherResponceList);




        System.out.println("########################################");
        System.out.println("If you need exit from this program write 'EXIT' ");

        if(scanner.nextLine().equals("exit")){
            System.out.println("Good bye ;) ");
            System.exit(0);
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class WeatherResponce{
    private String date;
    private double avgtemp_c;
    private String text;



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getAvgtemp_c() {
        return avgtemp_c;
    }

    public void setAvgtemp_c(Double avgtemp_c) {
        this.avgtemp_c = avgtemp_c;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public WeatherResponce(String date, Double avgtemp_c, String text) {
        this.date = date;
        this.avgtemp_c = avgtemp_c;
        this.text = text;
    }

    public WeatherResponce(){

    }
}
