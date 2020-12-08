package entities.mappers;

import entities.MessageEntity;
import entities.dto.MessageDto;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-12-07T23:38:58+0300",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 1.8.0_261 (Oracle Corporation)"
)
public class MessageMapperImpl implements MessageMapper {

    @Override
    public MessageDto messageToDto(MessageEntity message) {
        if ( message == null ) {
            return null;
        }

        MessageDto messageDto = new MessageDto();

        return messageDto;
    }
}
