package vn.edu.fpt.account.config.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import vn.edu.fpt.account.dto.event.SendEmailEvent;
import vn.edu.fpt.account.dto.event.SendSmsEvent;

import java.util.UUID;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 26/12/2022 - 15:10
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Service
@Slf4j
public class SendSMSProducer extends Producer {

    private static final String TOPIC = "flab.notification.send_sms";
    private ObjectMapper objectMapper;

    @Autowired
    public SendSMSProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        super(kafkaTemplate);
        this.objectMapper = objectMapper;
    }

    public void sendMessage(SendSmsEvent event) {
        try {
            String value = objectMapper.writeValueAsString(event);
            super.sendMessage(TOPIC, UUID.randomUUID().toString(), value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
