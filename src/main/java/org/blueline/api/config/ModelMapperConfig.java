package org.blueline.api.config;

import java.util.Set;
import java.util.stream.Collectors;

import org.blueline.api.model.Event;
import org.blueline.api.model.User;
import org.blueline.api.model.dto.EventDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

    @Component
    public class ModelMapperConfig {

        @Bean
        public ModelMapper modelMapper() {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.typeMap(Event.class, EventDto.class).addMappings(mapper -> {
                mapper.map(src -> src.getOrganizer().getId(), EventDto::setOrganizerId);
                mapper.map(Event::getStatus, EventDto::setStatus);
            }).setPostConverter(context -> {
                Event source = context.getSource();
                EventDto destination = context.getDestination();
                Set<Long> participantIds = source.getParticipants().stream()
                    .map(User::getId)
                    .collect(Collectors.toSet());
                destination.setParticipantIds(participantIds);
                return context.getDestination();
            });

            modelMapper.getConfiguration()
                    .setFieldMatchingEnabled(true)
                    .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
            return modelMapper;
            }
    }
