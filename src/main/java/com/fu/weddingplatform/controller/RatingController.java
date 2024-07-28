package com.fu.weddingplatform.controller;

import com.fu.weddingplatform.constant.rating.RatingSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.request.rating.CreateRatingDTO;
import com.fu.weddingplatform.request.rating.UpdateRatingDTO;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.rating.RatingResponse;
import com.fu.weddingplatform.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rating")
@CrossOrigin("*")
public class RatingController {

    @Autowired
    RatingService ratingService;

    @GetMapping("getAllRating")
    public ResponseEntity<?> getAllRating(@RequestParam(required = false) String coupleId,
                                          @RequestParam(required = false) String serviceId,
                                          @RequestParam(defaultValue = "0") int pageNo,
                                          @RequestParam(defaultValue = "10") int pageSize,
                                          @RequestParam(defaultValue = "id") String sortBy,
                                          @RequestParam(defaultValue = "true") boolean isAscending){
        List<RatingResponse> ratingResponseList = ratingService.getAllRating(coupleId, serviceId, pageNo, pageSize, sortBy, isAscending);
        ListResponseDTO<RatingResponse> responseDTO = new ListResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(RatingSuccessMessage.GET_ALL);
        responseDTO.setData(ratingResponseList);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("getRatingById/{id}")
    public ResponseEntity<?> getRatingById(@PathVariable String id){
        RatingResponse ratingResponse = ratingService.getRatingById(id);
        ResponseDTO<RatingResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(RatingSuccessMessage.GET_RATING);
        responseDTO.setData(ratingResponse);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<?> createRating(@Validated @RequestBody CreateRatingDTO request){
        RatingResponse ratingResponse = ratingService.createRating(request);
        ResponseDTO<RatingResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(RatingSuccessMessage.CREATE);
        responseDTO.setData(ratingResponse);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("update")
    public ResponseEntity<?> updateRating(@Validated @RequestBody UpdateRatingDTO request){
        RatingResponse ratingResponse = ratingService.updateRating(request);
        ResponseDTO<RatingResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(RatingSuccessMessage.UPDATE);
        responseDTO.setData(ratingResponse);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
