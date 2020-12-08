package entities.mappers;

import entities.RoomEntity;
import entities.dto.RoomDto;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-12-07T23:37:26+0300",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 1.8.0_261 (Oracle Corporation)"
)
public class RoomMapperImpl implements RoomMapper {

    @Override
    public RoomDto roomToDto(RoomEntity room) {
        if ( room == null ) {
            return null;
        }

        RoomDto roomDto = new RoomDto();

        roomDto.setId( room.getId() );
        roomDto.setName( room.getName() );

        return roomDto;
    }
}
