package vn.edu.fpt.account.config.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import vn.edu.fpt.account.dto.event.CreateProfileEvent;
import vn.edu.fpt.account.exception.BusinessException;
import vn.edu.fpt.account.service.ProfileService;


@Service
@RequiredArgsConstructor
public class CreateAccountConsumer extends Consumer{

    private final ProfileService profileService;
    private final ObjectMapper objectMapper;

    @Override
    @KafkaListener(id = "createProfileConsumer", topics = "flab.profile.create-profile", groupId = "profile_group")
    protected void listen(String value, String topic, String key) {
        super.listen(value, topic, key);
        try {
            CreateProfileEvent event = objectMapper.readValue(value, CreateProfileEvent.class);
            profileService.createProfile(event);
        }catch (Exception ex){
            throw new BusinessException("Can't create profile from event");
        }

    }
}
