package com.fu.weddingplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fu.weddingplatform.constant.rating.RatingSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.request.rating.CreateRatingDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.rating.RatingResponse;
import com.fu.weddingplatform.service.RatingService;

@RestController
@RequestMapping("rating")
@CrossOrigin("*")
public class RatingController {

    @Autowired
    RatingService ratingService;

    @GetMapping("getRatingById/{id}")
    public ResponseEntity<?> getRatingById(@PathVariable String id) {
        RatingResponse ratingResponse = ratingService.getRatingById(id);
        ResponseDTO<RatingResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(RatingSuccessMessage.GET_RATING);
        responseDTO.setData(ratingResponse);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<?> createRating(@Validated @RequestBody CreateRatingDTO request) {
        RatingResponse ratingResponse = ratingService.createRating(request);
        ResponseDTO<RatingResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(RatingSuccessMessage.CREATE);
        responseDTO.setData(ratingResponse);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
