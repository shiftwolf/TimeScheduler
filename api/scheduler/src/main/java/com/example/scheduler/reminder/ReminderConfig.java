package com.example.scheduler.reminder;

import com.example.scheduler.entities.EventsEntity;
import com.example.scheduler.entities.ParticipantsEntity;
import com.example.scheduler.entities.RemindersEntity;
import com.example.scheduler.entities.UsersEntity;
import com.example.scheduler.repositories.EventRepository;
import com.example.scheduler.repositories.ParticipantRepository;
import com.example.scheduler.repositories.ReminderRepository;
import com.example.scheduler.repositories.UserRepository;
import com.example.scheduler.util.JavaMailUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Configuration of the reminder
 */
@Configuration
@EnableScheduling
public class ReminderConfig {

    private final JavaMailUtil mailUtil;
    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;

    /**
     * @param mailUtil typically initialized using Dependency Injection
     */
    public ReminderConfig(JavaMailUtil mailUtil, ReminderRepository reminderRepository,
                          UserRepository userRepository, EventRepository eventRepository,
                          ParticipantRepository participantRepository) {
        this.mailUtil = mailUtil;
        this.reminderRepository = reminderRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.participantRepository = participantRepository;
    }


    /**
     * Complete task every 60 seconds
     */
    @Scheduled(fixedDelay = 60_000)
    public void fixedDelayTask() throws MessagingException {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        List<RemindersEntity> remindersEntities = Optional.ofNullable(reminderRepository.findByOrderByDateAsc())
                .orElseThrow(MessagingException::new);

        for (RemindersEntity remindersEntity : remindersEntities) {
            if (remindersEntity.getDate().before(currentTimestamp) && !remindersEntity.getCompleted()) {
                remindersEntity.setCompleted(true);
                reminderRepository.save(remindersEntity);

                List<ParticipantsEntity> participantsEntities = participantRepository.findAllByEventId(remindersEntity.getEventId());

                for(ParticipantsEntity participantsEntity : participantsEntities) {
                    UsersEntity usersEntity = userRepository.findUserById(participantsEntity.getUserId());
                    EventsEntity eventsEntity = eventRepository.findEventById(remindersEntity.getEventId());
                    mailUtil.sendMail(usersEntity.getEmail(),
                            "Reminder for Event: " + eventsEntity.getName(),
                            "Your event " + eventsEntity.getName()
                                    + " is scheduled in " + getNeatDuration(eventsEntity.getDate().getTime() - currentTimestamp.getTime()));
                    System.out.println("Status: Reminders were sent.");
                }
            }
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
}
