package com.example.travelagency.controllers;

import com.example.travelagency.entity.Guide;
import com.example.travelagency.service.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/guide")
public class GuideController {

    private final GuideService guideService;

    @Autowired
    public GuideController(GuideService guideService) {
        this.guideService = guideService;
    }

    @GetMapping
    public ResponseEntity<List<Guide>> getAllGuides() {
        List<Guide> guides = guideService.getAllGuides();
        return new ResponseEntity<>(guides, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Guide> getGuideById(@PathVariable Integer id) {
        Optional<Guide> guide = guideService.getGuideById(id);
        return guide.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Guide> createGuide(@RequestBody Guide guide) {
        Guide createdGuide = guideService.createGuide(guide);
        return new ResponseEntity<>(createdGuide, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Guide> updateGuide(@PathVariable Integer id, @RequestBody Guide guideDetails) {
        Guide updatedGuide = guideService.updateGuide(id, guideDetails);
        return new ResponseEntity<>(updatedGuide, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuide(@PathVariable Integer id) {
        guideService.deleteGuide(id);
        return ResponseEntity.noContent().build();
    }
}