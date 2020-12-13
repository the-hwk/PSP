package entities.mappers;

import entities.RoomEntity;
import entities.dto.RoomDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoomMapper {
    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    RoomDto roomToDto(RoomEntity room);
}
