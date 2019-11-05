package ppkwu;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

@Controller
public class CalendarWeeiaController {

    private static final String WEEIA_CALENDAR_ENDPOINT = "http://www.weeia.p.lodz.pl/pliki_strony_kontroler/kalendarz.php";

    @GetMapping("/ppkwu/calendar/{year}/{month}")
    @ResponseBody
    public ResponseEntity<?> getWeeiaCalendar(@PathVariable int year, @PathVariable int month) {
        try {
            Calendar calendar = parseToICal(getWeeiaCalendarEvents(year,month), year, month);
            return new ResponseEntity<>(calendar.toString(), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Weeia calendar is inactive", HttpStatus.NOT_FOUND);
        }
    }

    private Calendar parseToICal(Elements activeDays, int year, int month){
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        for(Element row:activeDays){
            addDailyEvents(calendar,row, year, month);
        }
        return calendar;
    }

    private void addDailyEvents(Calendar iCalendar, Element row, int year, int month){
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.YEAR, year);
        calendar.set(java.util.Calendar.MONTH, month-1);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, Integer.parseInt(row.select("a.active").first().text()));

        VEvent dailyEvent = new VEvent(new Date(calendar.getTime()), row.select("div.InnerBox").text());
        UidGenerator ug = new RandomUidGenerator();
        dailyEvent.getProperties().add(ug.generateUid());

        iCalendar.getComponents().add(dailyEvent);
    }

    private Elements getWeeiaCalendarEvents(int year, int month) throws IOException {
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
        return filterOnlyActiveDays(responseMessage.toString());
    }

    private Elements filterOnlyActiveDays(String calendarHtml){
        Document doc = Jsoup.parse(calendarHtml);
        return doc.select("td.active");
    }
}
