package com.example.scheduler.util;

import com.example.scheduler.entities.EventsEntity;
import com.example.scheduler.entities.ParticipantsEntity;
import com.example.scheduler.entities.TokensEntity;
import com.example.scheduler.entities.UsersEntity;
import com.example.scheduler.exceptions.EventNotFoundException;
import com.example.scheduler.exceptions.LoginFailedException;
import com.example.scheduler.exceptions.NoAuthorizationException;
import com.example.scheduler.repositories.EventRepository;
import com.example.scheduler.repositories.ParticipantRepository;
import com.example.scheduler.repositories.TokenRepository;
import com.example.scheduler.repositories.UserRepository;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class TxtExportController {
    private final TokenRepository tokenRepository;
    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public TxtExportController(TokenRepository tokenRepository, ParticipantRepository participantRepository, EventRepository eventRepository, UserRepository userRepository){
        this.tokenRepository = tokenRepository;
        this.participantRepository = participantRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/txt/generate")
    public void generateTxt(@RequestHeader("userId") Long userId,
                            @RequestHeader("token") String token,
                            @RequestBody String path,
                            @RequestBody String filename) throws IOException {

        TokensEntity tokensEntity = tokenRepository.findById(token).orElseThrow(LoginFailedException::new);
        if (!(tokensEntity.getUserId().longValue() == userId.longValue())) {
            throw new NoAuthorizationException(userId);
        }

        List<ParticipantsEntity> participantsEntities = participantRepository.findAllByUserId(userId);
        List<EventsEntity> eventsEntities = new ArrayList<>();
        for (ParticipantsEntity entity : participantsEntities) {
            eventsEntities.add(eventRepository.findById(entity.getEventId())
                    .orElseThrow(() -> new EventNotFoundException(entity.getEventId())));
        }
        eventsEntities.sort(new Comparator<EventsEntity>() {
            @Override
            public int compare(EventsEntity o1, EventsEntity o2) {
                return (int) (o1.getDate().getTime() / 1000 - o2.getDate().getTime() / 1000);
            }
        });
        if (eventsEntities.size() == 0)
            System.out.println("No events listed");
        else {
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            Timestamp startTimestamp = new Timestamp(currentTimestamp.getTime());
            startTimestamp.setHours(0);
            startTimestamp.setMinutes(0);
            startTimestamp.setSeconds(0);
            Timestamp endTimestamp = new Timestamp(startTimestamp.getTime() + 604799000);

            for (int i = 0; i < eventsEntities.size(); i++) {
                if (eventsEntities.get(i).getDate().getTime() < startTimestamp.getTime())
                    eventsEntities.remove(i);
                else if (eventsEntities.get(i).getDate().getTime() > endTimestamp.getTime())
                    eventsEntities.remove(i);
            }

            StringBuilder content = new StringBuilder();

            //title
            long endTime = eventsEntities.get(eventsEntities.size() - 1).getDate().getTime()
                    + eventsEntities.get(eventsEntities.size() - 1).getDuration().getTime();
            content.append("Schedule: ")
                    .append(getNeatDate(eventsEntities.get(0).getDate(), new Timestamp(endTime)))
                    .append("\n\n");

            boolean thisday = false;
            for (int i = 0; i < eventsEntities.size(); i++) {
                if (!thisday) {
                    String dayOfWeek = new SimpleDateFormat("EEEE", Locale.UK).format(new Date(eventsEntities.get(i).getDate().getTime()));
                    dayOfWeek += " " + eventsEntities.get(i).getDate().getDate()
                            + "." + (eventsEntities.get(i).getDate().getMonth() + 1)
                            + "." + (eventsEntities.get(i).getDate().getYear() + 1900);
                    content.append(dayOfWeek)
                            .append("\n\n");
                }

                //name
                content.append("Name: \t\t")
                        .append(eventsEntities.get(i).getName())
                        .append("\n");

                //date
                endTime = eventsEntities.get(i).getDate().getTime()
                        + eventsEntities.get(i).getDuration().getTime();
                content.append("Date: \t\t")
                        .append(getNeatDate(eventsEntities.get(i).getDate(), new Timestamp(endTime)))
                        .append("\n");

                //duration
                content.append("Duration: \t")
                        .append(getNeatDuration(eventsEntities.get(i).getDuration().getTime()))
                        .append("\n");

                //location
                content.append("Location: \t")
                        .append(eventsEntities.get(i).getLocation())
                        .append("\n");

                //participants
                String participants = "";
                List<ParticipantsEntity> participantsEntityList = eventsEntities.get(i).getParticipantsEntities();
                for (int j = 0; j < participantsEntityList.size(); j++) {
                    Long userIdP = participantsEntityList.get(j).getUserId();
                    if (!Objects.equals(userIdP, userId)) {
                        UsersEntity usersEntity = userRepository.findUserById(userIdP);
                        participants += usersEntity.getUsername();
                        if (j + 1 < participantsEntityList.size())
                            participants += ", ";
                    }
                }
                if (!participants.equals("")) {
                    content.append("Participants: \t")
                            .append(participants)
                            .append("\n");
                }

                content.append("\n\n");

                if (i + 1 < eventsEntities.size()) {
                    if (eventsEntities.get(i).getDate().getDay() == eventsEntities.get(i).getDate().getDay()) {
                        thisday = true;
                    } else {
                        thisday = false;
                        content.append("\n\n\n");
                    }
                }

            }
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new FileWriter(path + "\\" + filename + ".txt")
            );
            bufferedWriter.write(String.valueOf(content));
            bufferedWriter.close();





        /*
        String path;
        String filename;
        String content;
        BufferedWriter bw = new BufferedWriter(
                    new FileWriter(path + "\\" + filename + ".txt"));
            bw.write(content);
            bw.close();
        */
        }
    }

    private String getNeatDuration(Long duration) {
        String neat = "";

        Long days = duration / 1000L / 60L / 60L / 24L;
        if(days > 1)
            neat += days + " days ";
        else if(days > 0)
            neat += days + " day ";

        Long hours = (duration / 1000L / 60L / 60L) % 24;
        if(hours > 1)
            neat += hours + " hours ";
        else if (hours > 0)
            neat += hours + " hour ";

        Long minutes = (duration / 1000L / 60L) % 60;
        if(minutes > 1)
            neat += minutes + " minutes ";
        else if (minutes > 0)
            neat += minutes + " minute ";

        if(neat.equals(""))
            neat = "0 minutes";

        return neat;
    }

    private String getNeatDate(Timestamp begin, Timestamp end){
        String neat = "";

        neat += begin.getDate();
        neat += "." + (begin.getMonth()+1);
        neat += "." + (begin.getYear()+1900);
        neat += " " + begin.getHours();
        neat += ":" + begin.getMinutes();

        neat += " - ";

        String endDay = end.toString().split(" ")[0];
        String beginDay = begin.toString().split(" ")[0];
        if (!endDay.equals(beginDay)){
            neat += end.getDate();
            neat += "." + (end.getMonth()+1);
            neat += "." + (end.getYear()+1900);
            neat += " ";
        }
        neat += end.getHours();
        neat += ":" + end.getMinutes();

        return neat;
    }
}
