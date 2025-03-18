package com.example.travelagency.service;

import com.example.travelagency.entity.Hotel;
import com.example.travelagency.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Optional<Hotel> getHotelById(Integer id) {
        return hotelRepository.findById(id);
    }

    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel(Integer id, Hotel hotelDetails) {
        hotelDetails.setId(id);
        return hotelRepository.save(hotelDetails);
    }

    public void deleteHotel(Integer id) {
        hotelRepository.deleteById(id);
    }
}
