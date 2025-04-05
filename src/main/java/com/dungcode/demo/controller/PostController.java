package com.dungcode.demo.controller;

import com.dungcode.demo.common.SuccessResponse;
import com.dungcode.demo.dto.request.PostRequest;
import com.dungcode.demo.mongodb.model.Post;
import com.dungcode.demo.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;

@RestController
@RequestMapping("/post")
public class PostController {
    PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping()
    ResponseEntity<?> create(@RequestBody @Valid PostRequest request) {
        return (postService.create(request)).responseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        return (postService.getPostById(id)).responseEntity();
    }


    @GetMapping("test-1")
    public ResponseEntity<?> test1() {

        //Ví dụ về NullPointerException
//        String fullname = null;
//        Integer length = fullname.length();

        //Ví dụ về  IndexOutOfBoundsException
//        ArrayList<String> list = new ArrayList<>();
//        list.add(-1, "element");


        // IllegalArgumentException
        int number = Integer.parseInt("abc");


        return new SuccessResponse<>("Test ok").responseEntity();
    }

    @GetMapping("/test-2")
    public ResponseEntity<?> test2(@RequestParam("id") String userId) {
        // Nếu request URL là /users (thiếu tham số id)
        // thì sẽ ném MissingServletRequestParameterException
        return new SuccessResponse<>(userId).responseEntity();
    }

    @GetMapping("/test-3")
    public ResponseEntity<?> test3() {
        String permission = "USER";

        if (!permission.equals("ADMIN")) {
            try {
                throw new AccessDeniedException("Bạn không có quền");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }

        return new SuccessResponse<>("Test ok").responseEntity();
    }
}
