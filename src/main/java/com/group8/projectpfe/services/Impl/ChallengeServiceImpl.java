package com.group8.projectpfe.services.Impl;

import com.group8.projectpfe.domain.dto.ChallengeDTO;
import com.group8.projectpfe.domain.dto.SportifDTO;
import com.group8.projectpfe.domain.dto.TeamDTO;
import com.group8.projectpfe.entities.Challenge;
import com.group8.projectpfe.entities.Sport;
import com.group8.projectpfe.entities.Team;
import com.group8.projectpfe.entities.User;
import com.group8.projectpfe.mappers.impl.ChallengeMapperImpl;
import com.group8.projectpfe.repositories.ChallengeRepository;
import com.group8.projectpfe.repositories.SportRepository;
import com.group8.projectpfe.repositories.TeamRepository;
import com.group8.projectpfe.services.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {


    private final ChallengeRepository challengeRepository;

    private final TeamRepository teamRepository;
    private final SportRepository sportRepository;
    private final ChallengeMapperImpl challengeMapper;

    @Override
    public List<ChallengeDTO> getAllChallenges() {
        List<Challenge> challenges = challengeRepository.findAll();
        return challenges.stream().map(challengeMapper::mapTo).collect(Collectors.toList());
    }

    @Override
    public Optional<ChallengeDTO> getChallengeById(int id) {
        Optional<Challenge> challengeOptional = challengeRepository.findById(id);
        return challengeOptional.map(challengeMapper::mapTo);
    }

    @Override
    public ChallengeDTO createChallenge(ChallengeDTO challengeDetails) {
        Challenge challengeToCreate = challengeMapper.mapFrom(challengeDetails);

        Sport existingSport = sportRepository.findById(challengeDetails.getSport().getId()).orElse(null);

        if (existingSport != null) {
            challengeToCreate.setSport(existingSport);
        }
        List<Team> teams = new ArrayList<>();
        for (Team member : challengeToCreate.getTeams()) {
            Team team = teamRepository.getById(member.getId());
            teams.add(team);
        }

        challengeToCreate.setTeams(teams);
        challengeToCreate.setLogoPath(challengeToCreate.getLogoPath());
        Challenge savedChallenge = challengeRepository.save(challengeToCreate);
        return challengeMapper.mapTo(savedChallenge);
    }

    @Override
    public ChallengeDTO updateChallenge(int id, ChallengeDTO updatedChallengeDetails) {
        Optional<Challenge> optionalChallenge = challengeRepository.findById(id);

        if (optionalChallenge.isPresent()) {
            Challenge existingChallenge = optionalChallenge.get();

            Sport sport = sportRepository.getReferenceById(updatedChallengeDetails.getSport().getId());
            existingChallenge.setSport(sport);
            // Assuming you're getting the members as a list of User IDs in the DTO
            List<Integer> teamTds = updatedChallengeDetails.getTeams().stream()
                    .map(TeamDTO::getId) // Assuming getId() returns the user ID as an Integer
                    .collect(Collectors.toList());
            List<Team> teams = teamRepository.findAllById(teamTds);
            existingChallenge.setTeams(teams);
            existingChallenge.setLogoPath(updatedChallengeDetails.getLogoPath());
            Challenge updatedChallenge = challengeRepository.save(existingChallenge);
            return challengeMapper.mapTo(updatedChallenge);
        } else {
            return null;
        }
    }

    @Override
    public void deleteChallenge(int id) {
        challengeRepository.deleteById(id);
    }




}
