package com.gym.management.service;

import com.gym.management.dto.request.UserLoginRequest;
import com.gym.management.dto.request.UserProfileUpdateRequest;
import com.gym.management.dto.request.UserRegisterRequest;
import com.gym.management.dto.response.AuthResponse;
import com.gym.management.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    // --- Authentication & OTP ---

    // ثبت نام عادی
    UserResponse registerUser(UserRegisterRequest userRegisterRequest);

    // ورود با نام کاربری/رمز عبور
    AuthResponse login(UserLoginRequest userLoginRequest);

    // ارسال کد یک‌بارمصرف به شماره موبایل
    void sendOtp(String mobileNumber);

    // ورود/ثبت‌نام خودکار با OTP
    AuthResponse verifyOtpAndLogin(String mobileNumber, String otpCode);

    // تمدید توکن JWT
    AuthResponse refreshToken(String refreshToken);


    // --- Profile & User Management ---

    // دریافت یک کاربر با آیدی
    UserResponse getUserById(Long userId);

    // دریافت کاربر با شماره موبایل
    UserResponse getUserByMobileNumber(String mobileNumber);

    // دریافت لیست تمام کاربران (مخصوص مدیر/پذیرش)
    List<UserResponse> getAllUsers();

    // آپدیت پروفایل کاربر
    UserResponse updateUserProfile(Long userId, UserProfileUpdateRequest request);

    // حذف کاربر
    void deleteUser(Long userId);

    // تغییر وضعیت فعال/غیرفعال بودن حساب کاربر
    void toggleUserActiveStatus(Long userId, boolean isActive);


    // --- Authorization & Access Control ---

    // بررسی فعال بودن حساب کاربر
    boolean isUserActive(Long userId);

    // دریافت لیست دسترسی‌های کاربر
    List<String> getUserPermissions(Long userId);




}
