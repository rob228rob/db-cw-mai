package com.mai.db_cw_rbl.LawyerPackage;

import com.fasterxml.uuid.Generators;
import com.mai.db_cw_rbl.InfrastructurePackage.CommonPackage.CustomExceptions.EntityNotFoundException;
import com.mai.db_cw_rbl.LawyerPackage.Dao.LawyerDao;
import com.mai.db_cw_rbl.LawyerPackage.Dto.LawyerCreationRequest;
import com.mai.db_cw_rbl.LawyerPackage.Dto.LawyerResponse;
import com.mai.db_cw_rbl.SpecializationPackage.SpecializationService;
import com.mai.db_cw_rbl.UserPackage.User;
import com.mai.db_cw_rbl.UserPackage.Dto.UserResponse;
import com.mai.db_cw_rbl.UserPackage.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LawyerService {

    private final UserService userService;

    private final LawyerDao lawyerDao;
    private final ModelMapper modelMapper;

    private final SpecializationService specializationService;

    private void saveSpecializationIfNotExists(String name) {
        if (specializationService.existsSpecializationByName(name)) {
            return;
        }
        specializationService.saveSpecialization(name);
    }

    @Transactional
    public LawyerResponse saveLawyer(LawyerCreationRequest lawyerDto) {
        var user = userService.saveUserWithRoles(lawyerDto.getUserData(), List.of("ROLE_LAWYER"));
        //temp solution!
        saveSpecializationIfNotExists(lawyerDto.getSpecialization());

        Lawyer lawyer = Lawyer.builder()
                .id(Generators.timeBasedEpochGenerator().generate())
                .userId(user.getId())
                .licenceNumber(lawyerDto.getLicence())
                .yearsExperience(lawyerDto.getExperienceYears())
                .specializationId(
                        specializationService
                                .findSpecializationByName(lawyerDto.getSpecialization())
                                .getId())
                .build();

        lawyerDao.save(lawyer);

        return mapLawyerToResponse(lawyer, user);
    }

    private LawyerResponse mapLawyerToResponse(Lawyer lawyer, User user) {
        LawyerResponse lawyerResponse = modelMapper.map(lawyer, LawyerResponse.class);
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        lawyerResponse.setUserData(userResponse);
        lawyerResponse.setSpecializationName(
                specializationService
                        .findSpecializationById(lawyer.getSpecializationId())
                        .getName());

        return lawyerResponse;
    }

    public LawyerResponse findLawyerById(UUID lawyerId) {
        var lawyer = lawyerDao.findById(lawyerId).orElseThrow(
                        () -> new EntityNotFoundException("Lawyer not found: Lawyer with id " + lawyerId));
        var specialization = specializationService.findSpecializationById(lawyer.getSpecializationId());
        var userDto = userService.findUserByIdReturningDto(lawyer.getUserId());

        return LawyerResponse.builder()
                .id(lawyerId)
                .licenceNumber(lawyer.getLicenceNumber())
                .yearsExperience(lawyer.getYearsExperience())
                .specializationName(specialization.getName())
                .userData(userDto)
                .build();
    }

    public List<LawyerResponse> findAllLawyers(int batchSize) {
        if (batchSize <= 0) {
            return Collections.emptyList();
        }

        var lawyers = lawyerDao.findAllWithBatch(batchSize)
                .stream()
                .map(this::mapLawyerToResponse)
                .toList();

        return lawyers;
    }

    private LawyerResponse mapLawyerToResponse(Lawyer lawyer) {
        var response = modelMapper.map(lawyer, LawyerResponse.class);
        var user = userService.findUserByIdReturningDto(lawyer.getUserId());
        response.setUserData(user);

        return response;
    }

    @Transactional
    public LawyerResponse findLawyerByUserId(UUID userId) {
        var lawyer = lawyerDao.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Lawyer not found: Lawyer with id " + userId)
        );

        var specName = specializationService
                .findSpecializationById(lawyer.getSpecializationId()).getName();
        var user = userService.findUserByIdReturningDto(userId);
        var lawyerResponse = modelMapper.map(lawyer, LawyerResponse.class);
        lawyerResponse.setUserData(user);
        lawyerResponse.setSpecializationName(specName);

        return lawyerResponse;
    }
}
