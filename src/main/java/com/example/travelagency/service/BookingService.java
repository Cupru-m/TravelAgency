package com.example.travelagency.service;

import com.example.travelagency.entity.Booking;
import com.example.travelagency.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Integer id) {
        return bookingRepository.findById(id);
    }

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Integer id, Booking bookingDetails) {
        bookingDetails.setId(id);
        return bookingRepository.save(bookingDetails);
    }

    public void deleteBooking(Integer id) {
        bookingRepository.deleteById(id);
    }
}