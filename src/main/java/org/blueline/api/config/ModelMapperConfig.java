package org.blueline.api.config;

import org.blueline.api.model.User;
import org.blueline.api.model.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(UserDto.class, User.class).addMappings(mapper -> {
            mapper.skip(User::setIsAdmin);
        });
        
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        return modelMapper;
    }
}
