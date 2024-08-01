package com.eCommerce.eCommerce.service.impl;

import com.eCommerce.eCommerce.builder.ResponseBuilder;
import com.eCommerce.eCommerce.dto.AddressRequest;
import com.eCommerce.eCommerce.entity.Address;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.exceptions.EcommerceException;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.repository.AddressRepository;
import com.eCommerce.eCommerce.repository.UserRepository;
import com.eCommerce.eCommerce.service.AddressService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse addAddress(Long userId, AddressRequest addressRequest) {
        User user = userRepository.findById(userId).orElseThrow(() ->  new EcommerceException("USR001"));
        Address address = new Address();
        modelMapper.map(addressRequest, address);
        address.setUser(user);
        addressRepository.save(address);
        return ResponseBuilder.buildSuccessResponse("message.address.created.success");
    }

    @Override
    public ApiResponse removeAddress(Long userId, Long addressId) {
        return null;
    }

    @Override
    public ApiResponse updateAddress(Long userId, AddressRequest addressRequest) {
        return null;
    }

    @Override
    public ApiResponse findAllAddressByUserId(Long userId) {
        return null;
    }


    ///////////////////////
//    @Override
//    public ApiResponse findAllAddressByUserId(Long userId) {
//        List<Address> addresses = addressRepository.findByUserId(userId);
//        if (addresses.isEmpty()) {
//            return ResponseBuilder.buildResponse("")
//        }
//        return ResponseBuilder.buildSuccessResponse(addresses, "")
//    }


}
