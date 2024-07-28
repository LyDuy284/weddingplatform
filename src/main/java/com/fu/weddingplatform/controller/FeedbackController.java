package com.fu.weddingplatform.controller;

import com.fu.weddingplatform.constant.feedback.FeedbackSuccessMessage;
import com.fu.weddingplatform.constant.rating.RatingSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.request.feedback.CreateFeedbackDTO;
import com.fu.weddingplatform.request.feedback.UpdateFeedbackDTO;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.feedback.FeedbackResponse;
import com.fu.weddingplatform.response.rating.RatingResponse;
import com.fu.weddingplatform.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("feedback")
@CrossOrigin("*")
public class FeedbackController {

    @Autowired
    FeedbackService feedbackService;

    @GetMapping("getAllFeedback")
    public ResponseEntity<?> getAllFeedback(@RequestParam(required = false) String coupleId,
                                          @RequestParam(required = false) String supplierId,
                                          @RequestParam(defaultValue = "0") int pageNo,
                                          @RequestParam(defaultValue = "10") int pageSize,
                                          @RequestParam(defaultValue = "id") String sortBy,
                                          @RequestParam(defaultValue = "true") boolean isAscending){
        List<FeedbackResponse> feedbackResponseList = feedbackService.getAllFeedback(coupleId, supplierId, pageNo, pageSize, sortBy, isAscending);
        ListResponseDTO<FeedbackResponse> responseDTO = new ListResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(FeedbackSuccessMessage.GET_ALL);
        responseDTO.setData(feedbackResponseList);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("getFeedbackById/{id}")
    public ResponseEntity<?> createFeedback(@PathVariable String id){
        FeedbackResponse feedbackResponse = feedbackService.getFeedbackById(id);
        ResponseDTO<FeedbackResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(FeedbackSuccessMessage.GET_FEED_BACK);
        responseDTO.setData(feedbackResponse);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<?> createFeedback(@Validated @RequestBody CreateFeedbackDTO request){
        FeedbackResponse feedbackResponse = feedbackService.createFeedback(request);
        ResponseDTO<FeedbackResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(FeedbackSuccessMessage.CREATE);
        responseDTO.setData(feedbackResponse);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("update")
    public ResponseEntity<?> updateFeedback(@Validated @RequestBody UpdateFeedbackDTO request){
        FeedbackResponse feedbackResponse = feedbackService.updateFeedback(request);
        ResponseDTO<FeedbackResponse> responseDTO = new ResponseDTO<>();
        responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
        responseDTO.setMessage(FeedbackSuccessMessage.UPDATE);
        responseDTO.setData(feedbackResponse);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
