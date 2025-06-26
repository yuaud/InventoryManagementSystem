package com.yusuf.supplierService.services.impl;


import com.yusuf.supplierService.dtos.Response;
import com.yusuf.supplierService.dtos.SupplierDTO;
import com.yusuf.supplierService.exceptions.NotFoundException;
import com.yusuf.supplierService.models.Supplier;
import com.yusuf.supplierService.repositories.SupplierRepository;
import com.yusuf.supplierService.services.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response addSupplier(SupplierDTO supplierDTO) {
        Supplier supplierToSave = modelMapper.map(supplierDTO, Supplier.class);
        Supplier savedSupplier = supplierRepository.save(supplierToSave);

        return Response.builder()
                .status(200)
                .message("Supplier added successfully")
                .build();
    }

    @Override
    public Response updateSupplier(Long id, SupplierDTO supplierDTO) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier Not Found"));
        if(supplierDTO.getName() != null) existingSupplier.setName(supplierDTO.getName());
        if(supplierDTO.getContactInfo() != null) existingSupplier.setContactInfo(supplierDTO.getContactInfo());
        if(supplierDTO.getAddress() != null) existingSupplier.setAddress(supplierDTO.getAddress());
        supplierRepository.save(existingSupplier);

        return Response.builder()
                .status(200)
                .message("Supplier successfully updated")
                .build();
    }

    @Override
    public Response getAllSupplier() {
        List<Supplier> suppliers = supplierRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<SupplierDTO> supplierDTOList = modelMapper.map(suppliers, new TypeToken<List<SupplierDTO>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("All suppliers successfully retrieved")
                .suppliers(supplierDTOList)
                .build();
    }

    @Override
    public Response getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier Not Found"));
        SupplierDTO supplierDTO = modelMapper.map(supplier, SupplierDTO.class);

        return Response.builder()
                .status(200)
                .message("Supplier successfully retrieved")
                .supplier(supplierDTO)
                .build();
    }

    @Override
    public Response deleteSupplier(Long id) {
        supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier Not Found"));

        supplierRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Supplier successfully deleted")
                .build();
    }
}
