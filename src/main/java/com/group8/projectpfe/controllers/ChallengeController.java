package com.group8.projectpfe.controllers;

import com.group8.projectpfe.domain.dto.ChallengeDTO;
import com.group8.projectpfe.domain.dto.TeamDTO;
import com.group8.projectpfe.services.ChallengeService;
import com.group8.projectpfe.services.Impl.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/challenges")
@RequiredArgsConstructor
public class ChallengeController {


    private final ChallengeService challengeService;

    private final ImageService imageService;


    @GetMapping
    public ResponseEntity<List<ChallengeDTO>> getAllChallenges() {
        List<ChallengeDTO> challenges = challengeService.getAllChallenges();
        return new ResponseEntity<>(challenges, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeDTO> getChallengeById(@PathVariable int id) {
        return challengeService.getChallengeById(id)
                .map(challenge -> new ResponseEntity<>(challenge, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ChallengeDTO> createChallenge(@RequestParam("logo") MultipartFile logo, @ModelAttribute ChallengeDTO challengeDetails) {
        if (!logo.isEmpty()) {
            try {
                String imagePath = imageService.saveImage(logo); // Save the image and get the generated file path
                if (challengeDetails.getLogoPath() != null) {
                    imageService.deleteProfile(challengeDetails.getLogoPath()); // Delete the old profile picture
                }
                challengeDetails.setLogoPath(imagePath); // Set the image path in the DTO instead of the byte array
            } catch (IOException e) {
                // Handle file processing error
                return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        ChallengeDTO createdChallenge = challengeService.createChallenge(challengeDetails);
        return new ResponseEntity<>(createdChallenge, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChallengeDTO> updateChallenge(@PathVariable int id,@RequestParam("logo") MultipartFile logo, @ModelAttribute ChallengeDTO updatedChallengeDetails) {
        if (!logo.isEmpty()) {
            try {
                String imagePath = imageService.saveImage(logo); // Save the image and get the generated file path
                if (updatedChallengeDetails.getLogoPath() != null) {
                    imageService.deleteProfile(updatedChallengeDetails.getLogoPath()); // Delete the old profile picture
                }
                updatedChallengeDetails.setLogoPath(imagePath); // Set the image path in the DTO instead of the byte array
            } catch (IOException e) {
                // Handle file processing error
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        ChallengeDTO updatedChallenge = challengeService.updateChallenge(id, updatedChallengeDetails);
        if (updatedChallenge != null) {
            return new ResponseEntity<>(updatedChallenge, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChallenge(@PathVariable int id) throws IOException {
        ChallengeDTO challengeDTO = challengeService.getChallengeById(id).orElse(null);
        if (challengeDTO != null){
            imageService.deleteProfile(challengeDTO.getLogoPath());
            challengeService.deleteChallenge(id);
        }else{
            return new ResponseEntity<>("Unauthorized delete or sportif not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}