package com.example.spectacleprojet.network;
import com.example.spectacleprojet.DTO.BilletDTO;
import com.example.spectacleprojet.DTO.BilletResponseDTO;
import com.example.spectacleprojet.DTO.CategoryDTO;
import com.example.spectacleprojet.DTO.ProgrammationDTO;
import com.example.spectacleprojet.DTO.ReservationDTO;
import com.example.spectacleprojet.DTO.SpectacleDTO;
import com.example.spectacleprojet.DTO.SpectacleDetailsDTO;
import com.example.spectacleprojet.DTO.User;
import com.example.spectacleprojet.DTO.UserDTO;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;


public interface ApiService {
    @GET("api/spectacles")
    Call<List<SpectacleDTO>> getAllSpectacles();

    @GET("api/reservations/user/{userId}")
    Call<List<BilletDTO>> getReservationsByUserId(@Path("userId") int userId);
    @GET("api/spectacles/{id}")
    Call<SpectacleDetailsDTO> getSpectacleDetails(@Path("id") Integer id);
    @POST("/api/users/login")
    Call<User> login(@Query("username") String username, @Query("password") String password);

    @GET("/api/reservations/user/{userId}")
    Call<List<BilletResponseDTO>> getBilletDetailsByUserId(@Path("userId") int userId);
    @GET("programmations/{id}")
    Call<ProgrammationDTO> getProgrammationById(@Path("id") int id);

    @DELETE("/api/reservations/{billetId}")
    Call<Void> cancelBillet(@Path("billetId") int billetId);
    @POST("api/users/register")
    Call<User> register(@Body UserDTO userDTO);

    @POST("api/reservations")
    Call<BilletDTO> createReservation(@Body ReservationDTO reservationDTO);

    @GET("categories/{id}")
    Call<CategoryDTO> getCategoryById(@Path("id") int id);

    @GET("api/lieux")
    Call<List<String>> getAllLieux();
}