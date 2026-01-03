package com.app.booking.controller;

import com.app.booking.config.JwtAuthenticationEntryPoint;
import com.app.booking.config.JwtAuthenticationFilter;
import com.app.booking.dto.request.BookingRequest;
import com.app.booking.dto.response.BookingResponse;
import com.app.booking.entity.Booking;
import com.app.booking.service.BookingService;
import com.app.booking.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import com.app.booking.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testCreateBooking_Success() throws Exception {
        BookingRequest request = new BookingRequest();
        request.setShowId(1L);
        request.setSeatIds(Arrays.asList(1L, 2L));

        BookingResponse response = BookingResponse.builder()
                .bookingId(1L)
                .status(Booking.Status.CONFIRMED)
                .build();

        when(bookingService.createBooking(any())).thenReturn(response);

        mockMvc.perform(post("/bookings")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1L))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
        
        verify(bookingService).createBooking(any());
    }

    @Test
    @WithMockUser
    public void testCreateBooking_ValidationError() throws Exception {
        BookingRequest request = new BookingRequest();
        // Missing fields to trigger validation error

        mockMvc.perform(post("/bookings")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testGetBooking_Success() throws Exception {
        BookingResponse response = BookingResponse.builder()
                .bookingId(1L)
                .status(Booking.Status.CONFIRMED)
                .build();

        when(bookingService.getBookingById(1L)).thenReturn(response);

        mockMvc.perform(get("/bookings/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1L))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }
}
