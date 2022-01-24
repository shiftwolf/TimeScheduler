package com.example.scheduler.reminder;

import com.example.scheduler.entities.RemindersEntity;
import com.example.scheduler.repositories.ReminderRepository;
import com.example.scheduler.util.JavaMailUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * Configuration of the reminder
 */
@Configuration
@EnableScheduling
public class ReminderConfig {

    private final JavaMailUtil mailUtil;
    private final ReminderRepository reminderRepository;

    /**
     * @param mailUtil typically initialized using Dependency Injection
     */
    public ReminderConfig(JavaMailUtil mailUtil, ReminderRepository reminderRepository) {
        this.mailUtil = mailUtil;
        this.reminderRepository = reminderRepository;
    }


    /**
     * Complete task every 20 seconds
     */
    @Scheduled(fixedDelay = 20_000)
    public void fixedDelayTask() throws MessagingException {


        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        RemindersEntity remindersEntity = Optional.ofNullable(reminderRepository.findTopByOrderByDateAsc())
                .orElseThrow(MessagingException::new);

        if (remindersEntity.getDate().before(currentTimestamp)) {
            reminderRepository.deleteById(remindersEntity.getId());
            mailUtil.sendMail("timo.wolf@hotmail.de");
            System.out.println("Status: Reminders were sent.");
        }

    }
}
