package entities.mappers;

import entities.MessageEntity;
import entities.dto.MessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageDto messageToDto(MessageEntity message);
}
