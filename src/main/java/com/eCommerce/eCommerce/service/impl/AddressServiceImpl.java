package com.eCommerce.eCommerce.service.impl;

import com.eCommerce.eCommerce.builder.ResponseBuilder;
import com.eCommerce.eCommerce.dto.AddressRequest;
import com.eCommerce.eCommerce.dto.response.AddressResponse;
import com.eCommerce.eCommerce.entity.Address;
import com.eCommerce.eCommerce.entity.User;
import com.eCommerce.eCommerce.exceptions.EcommerceException;
import com.eCommerce.eCommerce.model.ApiResponse;
import com.eCommerce.eCommerce.repository.AddressRepository;
import com.eCommerce.eCommerce.repository.UserRepository;
import com.eCommerce.eCommerce.service.AddressService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ApiResponse addAddress(Long userId, AddressRequest addressRequest) {
        log.info("Adding address for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EcommerceException("USR001"));

        if (addressRequest.getIsDefault()) {
            log.info("Setting new default address for user ID: {}", userId);
            addressRepository.findByUserIdAndIsDefaultTrue(userId).ifPresent(defaultAddress -> {
                defaultAddress.setIsDefault(false);
                addressRepository.save(defaultAddress);
            });
        }

        Address address = new Address();
        modelMapper.map(addressRequest, address);
        address.setUser(user);
        address.setIsDefault(addressRequest.getIsDefault());
        addressRepository.save(address);

        log.info("Address added for user ID: {}", userId);
        return ResponseBuilder.buildSuccessResponse("message.address.created.success");
    }

    @Override
    public ApiResponse removeAddress(Long userId, Long addressId) {
        log.info("Removing address ID: {} for user ID: {}", addressId, userId);

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EcommerceException("ADR001"));

        if (!address.getUser().getId().equals(userId)) {
            log.warn("User ID: {} does not match address owner ID: {}", userId, address.getUser().getId());
            throw new EcommerceException("ADR002");
        }

        addressRepository.delete(address);
        log.info("Address ID: {} removed for user ID: {}", addressId, userId);
        return ResponseBuilder.buildSuccessResponse("message.address.deleted.success");
    }

    @Override
    @Transactional
    public ApiResponse updateAddress(Long userId, Long addressId, AddressRequest addressRequest) {
        log.info("Updating address ID: {} for user ID: {}", addressId, userId);

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EcommerceException("ADR001"));

        if (!address.getUser().getId().equals(userId)) {
            log.warn("User ID: {} does not match address owner ID: {}", userId, address.getUser().getId());
            throw new EcommerceException("ADR002");
        }

        if (addressRequest.getIsDefault()) {
            log.info("Setting new default address for user ID: {}", userId);
            addressRepository.findByUserIdAndIsDefaultTrue(userId).ifPresent(defaultAddress -> {
                defaultAddress.setIsDefault(false);
                addressRepository.save(defaultAddress);
            });
        }

        modelMapper.map(addressRequest, address);
        addressRepository.save(address);
        AddressResponse addressResponse = modelMapper.map(address, AddressResponse.class);

        log.info("Address ID: {} updated for user ID: {}", addressId, userId);
        return ResponseBuilder.buildSuccessResponse(addressResponse, "message.address.updated.success");
    }

    @Override
    public ApiResponse findAllAddressByUserId(Long userId) {
        log.info("Finding all addresses for user ID: {}", userId);

        List<Address> addresses = addressRepository.findByUserId(userId);
        if (addresses.isEmpty()) {
            log.info("No addresses found for user ID: {}", userId);
            return ResponseBuilder.buildSuccessResponse("message.address.empty");
        }

        List<AddressResponse> addressResponses = addresses.stream()
                .map(address -> modelMapper.map(address, AddressResponse.class))
                .toList();

        log.info("Found {} addresses for user ID: {}", addresses.size(), userId);
        return ResponseBuilder.buildSuccessResponse(addressResponses);
    }
}
