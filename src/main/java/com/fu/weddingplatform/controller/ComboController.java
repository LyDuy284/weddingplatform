package com.fu.weddingplatform.controller;

import com.fu.weddingplatform.constant.comboService.ComboServiceStatus;
import com.fu.weddingplatform.constant.comboService.ComboSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.constant.role.RolePreAuthorize;
import com.fu.weddingplatform.request.combo.CreateComboService;
import com.fu.weddingplatform.request.combo.UpdateComboInfor;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.combo.ComboResponse;
import com.fu.weddingplatform.service.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("combo")
@CrossOrigin("*")
public class ComboController {

    @Autowired
    ComboService comboService;

    @PostMapping("getAllCombo")
    public ResponseEntity<?> getAllCombo(@RequestParam(required = false) String comboName,
                                         @RequestParam(defaultValue = "0") int pageNo,
                                         @RequestParam(defaultValue = "10") int pageSize,
                                         @RequestParam(defaultValue = "id") String sortBy,
                                         @RequestParam(defaultValue = "true") boolean isAscending){
        List<ComboResponse> comboResponses = comboService.getComboByFilter(comboName, pageNo, pageSize, sortBy,  isAscending);
        ListResponseDTO<ComboResponse> responseDTO = new ListResponseDTO<>();
        responseDTO.setData(comboResponses);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ComboSuccessMessage.CREATE);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("create")
    @PreAuthorize(RolePreAuthorize.ROLE_STAFF)
    public ResponseEntity<?> createCombo(@Validated @RequestBody CreateComboService request){
        ComboResponse comboResponse = comboService.createComboService(request);
        ResponseDTO<ComboResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setData(comboResponse);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ComboSuccessMessage.CREATE);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("update")
    @PreAuthorize(RolePreAuthorize.ROLE_STAFF)
    public ResponseEntity<?> updateInforCombo(@Validated @RequestBody UpdateComboInfor request){
        ComboResponse comboResponse = comboService.updateComboInfor(request);
        ResponseDTO<ComboResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setData(comboResponse);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ComboSuccessMessage.CREATE);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("disable")
    @PreAuthorize(RolePreAuthorize.ROLE_STAFF)
    public ResponseEntity<?> disableCombo(@RequestParam String id){
        ComboResponse comboResponse = comboService.updateStatusCombo(id, ComboServiceStatus.DISABLE);
        ResponseDTO<ComboResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setData(comboResponse);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ComboSuccessMessage.CREATE);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("active")
    @PreAuthorize(RolePreAuthorize.ROLE_STAFF)
    public ResponseEntity<?> activeCombo(@RequestParam String id){
        ComboResponse comboResponse = comboService.updateStatusCombo(id, ComboServiceStatus.ACTIVATED);
        ResponseDTO<ComboResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setData(comboResponse);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ComboSuccessMessage.CREATE);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<?> getComboById(@PathVariable String id){
        ComboResponse comboResponse = comboService.getById(id);
        ResponseDTO<ComboResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setData(comboResponse);
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(ComboSuccessMessage.GET);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
