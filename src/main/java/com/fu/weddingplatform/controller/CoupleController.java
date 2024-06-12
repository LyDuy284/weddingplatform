package com.fu.weddingplatform.controller;

import com.fu.weddingplatform.constant.couple.CoupleSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.couple.CoupleResponse;
import com.fu.weddingplatform.service.CoupleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("couple")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CoupleController {

    @Autowired
    CoupleService coupleService;

    @GetMapping("getAllCoupleByAdmin")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<?> getAllCouple(@RequestParam(defaultValue = "0") int pageSize,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "id") String sortBy,
                                          @RequestParam(defaultValue = "true") boolean isAscending){
        List<CoupleResponse> coupleList = coupleService.getAllCouple(pageSize, size, sortBy, isAscending);
        ListResponseDTO<CoupleResponse> responseDTO = new ListResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(CoupleSuccessMessage.GET_ALL_COUPLE);
        responseDTO.setData(coupleList);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("getCouple/{coupleId}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<?> getCoupleById(@PathVariable String coupleId){
        CoupleResponse coupleResponse = coupleService.getCoupleById(coupleId);
        ResponseDTO<CoupleResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(CoupleSuccessMessage.GET_COUPLE);
        responseDTO.setData(coupleResponse);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("deleteCouple/{coupleId}")
    @PreAuthorize(RolePreAuthorize.ROLE_ADMIN)
    public ResponseEntity<?> deleteCouple(@PathVariable String coupleId){
        coupleService.deleteCouple(coupleId);
        ResponseDTO<CoupleResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(CoupleSuccessMessage.DELETE_COUPLE);
        responseDTO.setData(null);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
