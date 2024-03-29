package com.group8.projectpfe.mappers.impl;

import com.group8.projectpfe.domain.dto.ChallengeDTO;
import com.group8.projectpfe.domain.dto.SportDTO;
import com.group8.projectpfe.domain.dto.SportifDTO;
import com.group8.projectpfe.domain.dto.TeamDTO;
import com.group8.projectpfe.entities.Challenge;
import com.group8.projectpfe.entities.Sport;
import com.group8.projectpfe.entities.Team;
import com.group8.projectpfe.entities.User;
import com.group8.projectpfe.mappers.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChallengeMapperImpl implements Mapper<Challenge, ChallengeDTO> {

    private final ModelMapper modelMapper;
    private final TeamMapperImpl teamMapper;
    private final SportMapperImpl sportMapper;
    @Override
    public ChallengeDTO mapTo(Challenge challenge) {
        ChallengeDTO challengeDTO = modelMapper.map(challenge, ChallengeDTO.class);

        SportDTO sportDTO = sportMapper.mapTo(challenge.getSport());
        List<TeamDTO> teamDTOList = challenge.getTeams().stream()
                .map(teamMapper::mapTo)
                .collect(Collectors.toList());

        challengeDTO.setSport(sportDTO);
        challengeDTO.setTeams(teamDTOList);

        return challengeDTO;
    }
    @Override
    public Challenge mapFrom(ChallengeDTO challengeDTO) {
        Challenge challenge = modelMapper.map(challengeDTO, Challenge.class);

        List<Team> teamList = challengeDTO.getTeams().stream()
                .map(teamMapper::mapFrom)
                .collect(Collectors.toList());

        Sport sport = sportMapper.mapFrom(challengeDTO.getSport());

        challenge.setSport(sport);
        challenge.setTeams(teamList);

        return challenge;
    }

}
