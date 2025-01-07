package com.utility;

import com.dto.XboxProfileDTO;
import com.models.XboxModel.XboxProfile;
import com.models.UserModel.User;

public class XboxProfileMapper {

    /**
     * Convert from DTO to Entity (for saving in DB).
     */
    public static XboxProfile toEntity(XboxProfileDTO dto, User user) {
        XboxProfile entity = new XboxProfile();
        entity.setUser(user); // Or set null if you don't have a User

        entity.setXboxId(dto.getId());
        entity.setXboxGamertag(dto.getGamertag());
        entity.setAppDisplayName(dto.getAppDisplayName());
        entity.setGameDisplayName(dto.getGameDisplayName());
        entity.setAppDisplayPicRaw(dto.getAppDisplayPicRaw());
        entity.setGameDisplayPicRaw(dto.getGameDisplayPicRaw());
        entity.setAccountTier(dto.getAccountTier());
        entity.setTenureLevel(dto.getTenureLevel());
        entity.setGamerscore(dto.getGamerscore());
        return entity;
    }

    /**
     * Convert from Entity to DTO (for returning in HTTP responses).
     */
    public static XboxProfileDTO toDTO(XboxProfile entity) {
        XboxProfileDTO dto = new XboxProfileDTO();
        dto.setId(entity.getXboxId());
        dto.setGamertag(entity.getXboxGamertag());
        dto.setAppDisplayName(entity.getAppDisplayName());
        dto.setGameDisplayName(entity.getGameDisplayName());
        dto.setAppDisplayPicRaw(entity.getAppDisplayPicRaw());
        dto.setGameDisplayPicRaw(entity.getGameDisplayPicRaw());
        dto.setAccountTier(entity.getAccountTier());
        dto.setTenureLevel(entity.getTenureLevel());
        dto.setGamerscore(entity.getGamerscore());
        return dto;
    }
}
