package com.yusuf.supplierService.services;

import com.yusuf.supplierService.dtos.Response;
import com.yusuf.supplierService.dtos.SupplierDTO;

public interface SupplierService {

    Response addSupplier(SupplierDTO supplierDTO);

    Response updateSupplier(Long id, SupplierDTO supplierDTO);

    Response getAllSupplier();

    Response getSupplierById(Long id);

    Response deleteSupplier(Long id);

}
