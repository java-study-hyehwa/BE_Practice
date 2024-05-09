package server.domain.user.mapper;

import server.domain.user.banker.Banker;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BankerMapper {

  BankerMapper INSTANCE = Mappers.getMapper(BankerMapper.class);

  Banker userDtoToUserModel(Banker banker);

}
