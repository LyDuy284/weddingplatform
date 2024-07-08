package com.fu.weddingplatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.weddingplatform.constant.comment.CommentSuccessMessage;
import com.fu.weddingplatform.constant.response.ResponseStatusDTO;
import com.fu.weddingplatform.request.comment.CreateCommentDTO;
import com.fu.weddingplatform.request.comment.CreateReplyCommentDTO;
import com.fu.weddingplatform.response.ListResponseDTO;
import com.fu.weddingplatform.response.ResponseDTO;
import com.fu.weddingplatform.response.comment.CommentResponse;
import com.fu.weddingplatform.response.comment.ReplyCommentResponse;
import com.fu.weddingplatform.service.CommentService;

@RestController
@RequestMapping("/comment")
@CrossOrigin("*")
public class CommentController {

  @Autowired
  private CommentService commentService;

  @PostMapping("create")
  public ResponseEntity<?> createComment(@Validated @RequestBody CreateCommentDTO createDTO) {
    ResponseDTO<CommentResponse> responseDTO = new ResponseDTO<>();
    CommentResponse data = commentService.createComment(createDTO);
    responseDTO.setData(data);
    responseDTO.setMessage(CommentSuccessMessage.CREATE);
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @PostMapping("createReplyComment")
  public ResponseEntity<?> createReplyComment(@Validated @RequestBody CreateReplyCommentDTO createDTO) {
    ResponseDTO<ReplyCommentResponse> responseDTO = new ResponseDTO<>();
    ReplyCommentResponse data = commentService.createReplyComment(createDTO);
    responseDTO.setData(data);
    responseDTO.setMessage(CommentSuccessMessage.CREATE);
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("getById/{id}")
  public ResponseEntity<?> getById(@RequestParam String id) {
    CommentResponse category = commentService.getCommentById(id);
    ResponseDTO<CommentResponse> responseDTO = new ResponseDTO<>();
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    responseDTO.setMessage(CommentSuccessMessage.GET_BY_ID);
    responseDTO.setData(category);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("getAllCommentByPost/{id}")
  public ResponseEntity<?> getAllCommentByPost(@RequestParam String blogPostId,
      @RequestParam(defaultValue = "0") int pageNo,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "true") boolean isAscending) {
    List<CommentResponse> data = commentService.getAllCommentByBlogPost(blogPostId, pageNo, pageSize, sortBy,
        isAscending);
    ListResponseDTO<CommentResponse> responseDTO = new ListResponseDTO<>();
    responseDTO.setStatus(ResponseStatusDTO.SUCCESS);
    responseDTO.setMessage(CommentSuccessMessage.GET_ALL);
    responseDTO.setData(data);
    return ResponseEntity.ok().body(responseDTO);
  }

}
