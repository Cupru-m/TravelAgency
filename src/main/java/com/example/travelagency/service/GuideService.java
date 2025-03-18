package com.example.travelagency.service;

import com.example.travelagency.entity.Guide;
import com.example.travelagency.repository.GuideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuideService {

    private final GuideRepository guideRepository;

    @Autowired
    public GuideService(GuideRepository guideRepository) {
        this.guideRepository = guideRepository;
    }

    public List<Guide> getAllGuides() {
        return guideRepository.findAll();
    }

    public Optional<Guide> getGuideById(Integer id) {
        return guideRepository.findById(id);
    }

    public Guide createGuide(Guide guide) {
        return guideRepository.save(guide);
    }

    public Guide updateGuide(Integer id, Guide guideDetails) {
        guideDetails.setId(id);
        return guideRepository.save(guideDetails);
    }

    public void deleteGuide(Integer id) {
        guideRepository.deleteById(id);
    }
}