package com.etrichmond.driver_management.driver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.etrichmond.driver_management.auth.UserEntity;
import com.etrichmond.driver_management.auth.UserRepository;
import com.etrichmond.driver_management.auth.UserRole;
import com.etrichmond.driver_management.common.exception.ResourceNotFoundException;
import com.etrichmond.driver_management.vehicle.VehicleAssignmentEntity;
import com.etrichmond.driver_management.vehicle.VehicleAssignmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverService {

        private final DriverRepository driverRepository;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final VehicleAssignmentRepository vehicleAssignmentRepository;

        /**
         * Admin creates driver
         * Creates:
         * 1. User account for login
         * 2. Driver profile
         */
        public DriverResponse create(DriverRequest request) {

                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new IllegalArgumentException("Email already exists");
                }

                // Create login user
                UserEntity user = new UserEntity();
                user.setName(
                                request.getFirstName() + " " + request.getLastName());
                user.setEmail(request.getEmail());

                // Temporary password
                user.setPassword(
                                passwordEncoder.encode("Temp@123"));

                user.setRole(UserRole.DRIVER);

                UserEntity savedUser = userRepository.save(user);

                // Create driver profile
                DriverEntity driver = new DriverEntity();

                driver.setUser(savedUser);

                driver.setFirstName(request.getFirstName());
                driver.setMiddleName(request.getMiddleName());
                driver.setLastName(request.getLastName());

                driver.setPhone(request.getPhone());
                driver.setSecondaryPhone(request.getSecondaryPhone());

                driver.setAddress(request.getAddress());

                driver.setEmployeeId(request.getEmployeeId());

                driver.setBranch(request.getBranch());

                driver.setDriverType(request.getDriverType());

                driver.setNationality(request.getNationality());

                driver.setGender(request.getGender());

                driver.setLicenseNumber(request.getLicenseNumber());

                driver.setLicenseExpiry(request.getLicenseExpiry());

                driver.setStatus(request.getStatus());

                DriverEntity savedDriver = driverRepository.save(driver);

                return toResponse(savedDriver);
        }

        public List<DriverResponse> listAll() {

                return driverRepository.findAll()
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        public DriverResponse getById(String id) {

                DriverEntity driver = driverRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Driver not found"));

                return toResponse(driver);
        }

        public DriverResponse update(String id, DriverRequest request) {

                DriverEntity driver = driverRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Driver not found"));

                // Update User information
                UserEntity user = driver.getUser();

                user.setName(
                                request.getFirstName()
                                                + " "
                                                + request.getLastName());

                user.setEmail(request.getEmail());

                userRepository.save(user);

                // Update Driver information

                driver.setFirstName(request.getFirstName());

                driver.setMiddleName(request.getMiddleName());

                driver.setLastName(request.getLastName());

                driver.setPhone(request.getPhone());

                driver.setSecondaryPhone(
                                request.getSecondaryPhone());

                driver.setAddress(request.getAddress());

                driver.setEmployeeId(
                                request.getEmployeeId());

                driver.setBranch(
                                request.getBranch());

                driver.setDriverType(
                                request.getDriverType());

                driver.setLicenseNumber(
                                request.getLicenseNumber());

                driver.setLicenseExpiry(
                                request.getLicenseExpiry());

                driver.setNationality(request.getNationality());

                driver.setGender(request.getGender());

                driver.setStatus(
                                request.getStatus());

                driver.setPhotoUrl(request.getPhotoUrl());

                return toResponse(
                                driverRepository.save(driver));
        }

        public List<DriverResponse> search(
                        String search,
                        DriverStatus status,
                        int page,
                        int limit) {

                List<DriverEntity> drivers = driverRepository.findAll();

                if (search != null && !search.isBlank()) {

                        String value = search.trim().toLowerCase();

                        drivers = drivers.stream()
                                        .filter(driver -> (driver.getFirstName() != null
                                                        && driver.getFirstName().toLowerCase().contains(value))

                                                        || (driver.getLastName() != null
                                                                        && driver.getLastName().toLowerCase()
                                                                                        .contains(value))

                                                        || (driver.getUser() != null
                                                                        && driver.getUser().getEmail() != null
                                                                        && driver.getUser().getEmail().toLowerCase()
                                                                                        .contains(value))

                                                        || (driver.getLicenseNumber() != null
                                                                        && driver.getLicenseNumber().toLowerCase()
                                                                                        .contains(value)))
                                        .toList();
                }

                if (status != null) {

                        drivers = drivers.stream()
                                        .filter(driver -> driver.getStatus() != null
                                                        && driver.getStatus().equals(status))
                                        .toList();
                }

                int start = Math.max(0, page * limit);
                int end = Math.min(start + limit, drivers.size());

                List<DriverEntity> result = start >= end
                                ? List.of()
                                : drivers.subList(start, end);

                return result.stream()
                                .map(this::toResponse)
                                .toList();
        }

        private DriverResponse toResponse(DriverEntity driver) {

                String assignedVehicle = vehicleAssignmentRepository
                                .findByDriverIdAndReleasedDateIsNull(driver.getId())
                                .map(VehicleAssignmentEntity::getVehicle)
                                .map(vehicle -> vehicle.getPlateNumber())
                                .orElse(null);

                return new DriverResponse(
                                driver.getId(),
                                driver.getFirstName(),
                                driver.getMiddleName(),
                                driver.getLastName(),
                                driver.getUser() != null ? driver.getUser().getEmail() : null,
                                driver.getPhone(),
                                driver.getSecondaryPhone(),
                                driver.getAddress(),
                                driver.getEmployeeId(),
                                driver.getBranch(),
                                driver.getDriverType(),
                                driver.getLicenseNumber(),
                                driver.getGender(),
                                driver.getNationality(),
                                driver.getLicenseExpiry(),
                                driver.getStatus(),
                                driver.getPhotoUrl(),
                                assignedVehicle,
                                calculateProfileCompleteness(driver));
        }

        public DriverEntity getEntityByEmail(String email) {

                return driverRepository
                                .findByUserEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Driver profile not found"));
        }

        /**
         * Driver updates his own profile
         */
        public DriverResponse updateProfile(
                        String email,
                        DriverProfileRequest request) {

                DriverEntity driver = getEntityByEmail(email);

                driver.setPhone(
                                request.getPhone());

                driver.setSecondaryPhone(
                                request.getSecondaryPhone());

                driver.setAddress(
                                request.getAddress());

                DriverEntity saved = driverRepository.save(driver);

                return toResponse(saved);
        }

        public DriverResponse getProfileByEmail(
                        String email) {

                return toResponse(
                                getEntityByEmail(email));
        }

        private int calculateProfileCompleteness(
                        DriverEntity driver) {

                int completed = 0;

                int total = 10;

                if (driver.getFirstName() != null)
                        completed++;

                if (driver.getLastName() != null)
                        completed++;

                if (driver.getPhone() != null)
                        completed++;

                if (driver.getAddress() != null)
                        completed++;

                if (driver.getLicenseNumber() != null)
                        completed++;

                if (driver.getLicenseExpiry() != null)
                        completed++;

                if (driver.getBranch() != null)
                        completed++;

                if (driver.getDriverType() != null)
                        completed++;

                if (driver.getUser() != null)
                        completed++;

                if (driver.getStatus() != null)
                        completed++;

                return completed * 100 / total;
        }

        public DriverResponse approve(String driverId) {

                DriverEntity driver = driverRepository.findById(driverId)
                                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

                driver.setStatus(DriverStatus.ACTIVE);

                driverRepository.save(driver);

                return toResponse(driver);
        }

        public String uploadPhoto(MultipartFile file) {

                if (file == null || file.isEmpty()) {
                        throw new IllegalArgumentException("Photo file is required");
                }

                String contentType = file.getContentType();

                if (contentType == null || !contentType.startsWith("image/")) {
                        throw new IllegalArgumentException("Only image files are allowed");
                }

                try {
                        Path uploadDirectory = Paths.get("uploads", "drivers");

                        Files.createDirectories(uploadDirectory);

                        String originalFileName = file.getOriginalFilename();

                        String extension = "";

                        if (originalFileName != null && originalFileName.contains(".")) {
                                extension = originalFileName.substring(
                                                originalFileName.lastIndexOf("."));
                        }

                        String storedFileName = UUID.randomUUID() + extension;

                        Path targetPath = uploadDirectory.resolve(storedFileName);

                        Files.copy(
                                        file.getInputStream(),
                                        targetPath,
                                        StandardCopyOption.REPLACE_EXISTING);

                        return "/uploads/drivers/" + storedFileName;

                } catch (IOException e) {
                        throw new RuntimeException("Failed to upload driver photo", e);
                }
        }

}