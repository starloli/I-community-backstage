package com.example.demo2.service;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo2.dto.request.CalendarRequest;
import com.example.demo2.dto.response.CalendarResponse;
import com.example.demo2.entity.Calendar;
import com.example.demo2.repository.CalendarDao;

import lombok.RequiredArgsConstructor;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.DtStart;

@Service
@Transactional
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarDao calendarDao;
    private static final String ICS_URL =
        "https://calendar.google.com/calendar/ical/zh.taiwan%23holiday%40group.v.calendar.google.com/public/basic.ics";

    public List<CalendarResponse> getHolidays(String start) {
        try {
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = YearMonth.from(startDate).atEndOfMonth();

            URL url = new URL(ICS_URL);
            InputStream inputStream = url.openStream();

            CalendarBuilder builder = new CalendarBuilder();
            net.fortuna.ical4j.model.Calendar calendar = builder.build(inputStream);

            List<CalendarResponse> result = new ArrayList<>();

            for (Object comp : calendar.getComponents(Component.VEVENT)) {
                VEvent event = (VEvent) comp;

                DtStart dtStart = event.getStartDate();
                Date date = dtStart.getDate();

                LocalDate eventDate = date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                // 篩選日期區間
                if (!eventDate.isBefore(startDate) && !eventDate.isAfter(endDate)) {
                    CalendarResponse dto = new CalendarResponse();
                    dto.setDate(eventDate.toString());
                    dto.setTitle(event.getSummary().getValue());

                    result.add(dto);
                }
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch holidays", e);
        }
    }

    public List<CalendarResponse> getEvents(String start) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = YearMonth.from(startDate).atEndOfMonth();
        return calendarDao.findByDateBetween(startDate, endDate).stream()
                .map(CalendarResponse::from)
                .toList();
    }

    public void deleteEvent(Integer id) {
        calendarDao.deleteById(id);
    }

    public CalendarResponse createEvent(CalendarRequest request) {
        Calendar c = new Calendar();
        c.setDate(request.getDate());
        c.setTitle(request.getTitle());
        calendarDao.save(c);
        return CalendarResponse.from(c);
    }
}
