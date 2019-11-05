package ppkwu;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.lang.model.element.Element;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

@Controller
public class CalendarWeeiaController {

    private static final String WEEIA_CALENDAR_ENDPOINT = "http://www.weeia.p.lodz.pl/pliki_strony_kontroler/kalendarz.php";

    @GetMapping("/ppkwu/calendar/{year}/{month}")
    @ResponseBody
    public ResponseEntity<String> getWeeiaCalendar(@PathVariable int year, @PathVariable int month) {
        try {
            return new ResponseEntity<>(getWeeiaCalendarEvents(year,month), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Weeia calendar is inactive", HttpStatus.NOT_FOUND);
        }
    }

    public String getWeeiaCalendarEvents(int year, int month) throws IOException {
        StringBuilder urlAddress = new StringBuilder(WEEIA_CALENDAR_ENDPOINT).append("?rok=")
                .append(year)
                .append("&miesiac=")
                .append(month)
                .append("&lang=1");
        URL url = new URL(urlAddress.toString());
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();


        StringBuilder responseMessage = new StringBuilder();
        BufferedReader br;
        if(connection.getResponseCode() == 200){
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            responseMessage.append(br.lines().collect(Collectors.joining()));
        }else{
            responseMessage.append("error in loading weeia calendar");
        }
        return responseMessage.toString();
    }
}
