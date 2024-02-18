package com.adamszablewski.SocialMediaApp.utils;



import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
public class UserValidator {

//    public boolean isOwner(long facilityId, String userEmail){
//        RestResponseDTO<String> response =  bookingServiceClient.getOwnerMailById(facilityId);
//        if (response.getValue() == null){
//            throw new MissingFeignValueException();
//        }
//        String ownerEmail = response.getValue();
//        return ownerEmail.equals(userEmail);
//    }

//    public boolean isUser(long userId, String userEmail){
//        return userServiceClient.isUser(userId, userEmail);
//
//    }
//    public boolean isEmployee(long facilityId, String userEmail){
//        RestResponseDTO<String> response =  bookingServiceClient.getEmployeesForFacility(facilityId);
//        if (response.getValues() == null){
//            throw new MissingFeignValueException();
//        }
//        Set<String> employeeMails = response.getValues();
//        return employeeMails.stream()
//                .anyMatch(mail -> mail.equals(userEmail));
//    }
//    public boolean isClient(long appointmentId, String userEmail){
//        RestResponseDTO<String> response =  bookingServiceClient.getClientForAppointment(appointmentId);
//        if (response.getValue() == null){
//            throw new MissingFeignValueException();
//        }
//        String clientEmail = response.getValue();
//        return clientEmail.equals(userEmail);
//    }

//    public boolean isOwnerOrEmployeeOrTheClient(long appointmentId, String usereEmail){
//        RestResponseDTO<Long> response = bookingServiceClient.getFacilityIdForAppointment(appointmentId);
//        if (response.getValue() == null){
//            throw new MissingFeignValueException();
//        }
//        long facilityId = response.getValue();
//        return isClient(appointmentId, usereEmail) || isEmployee(facilityId, usereEmail)
//                || isOwner(facilityId, usereEmail);
//        //todo
//
//    }
//    public boolean isOwnerOrEmployee(long appointmentId, String userEmail){
//        return isOwner(appointmentId, userEmail) || isEmployee(appointmentId, userEmail);
//        //todo
//    }
}
