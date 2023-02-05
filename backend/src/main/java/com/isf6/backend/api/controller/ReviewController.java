package com.isf6.backend.api.controller;

import com.isf6.backend.api.Request.ReviewSaveReqDto;
import com.isf6.backend.api.Response.ReviewInfoResDto;
import com.isf6.backend.domain.entity.Bill;
import com.isf6.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{productId}")
    public ResponseEntity saveReview(@PathVariable Long productId, @RequestBody ReviewSaveReqDto reviewSaveReqDto) {
        Map<String, Object> response = new HashMap<>();
        Bill bill;

        //해당 상품에 리뷰가 없는지, 상품 상태가 SOLDOUT 인지 확인
        if(reviewService.checkReview(productId)) {
            response.put("result", "FAIL");
            response.put("reason", "해당 상품의 리뷰가 존재");
            return ResponseEntity.status(200).body(response);
        }
        //product service에서 만들고 주석 풀기
//        else if (productService.checkProductStatus(productId)) {
//            response.put("result", "FAIL");
//            response.put("reason", "해당 상품 상태가 SOLDOUT이 아님");
//            return ResponseEntity.status(200).body(response);
//        }

        try{
            bill = reviewService.createReview(productId, reviewSaveReqDto);
        } catch (Exception e) {
            e.printStackTrace();

            response.put("result", "FAIL");
            response.put("reason", "리뷰 등록 실패");
            return ResponseEntity.status(200).body(response);
        }

        response.put("result", "SUCCESS");
        response.put("reason", "리뷰 등록 성공");
        return ResponseEntity.status(200).body(response);
    }

    //로그인한 유저가 쓴 리뷰 전체 목록
    @GetMapping("/{userCode}")
    public ResponseEntity getMyReviewAll(@PathVariable Long userCode) {
        log.info("userCode : {}", userCode);
        Map<String, Object> response = new HashMap<>();
        List<ReviewInfoResDto> reviewList;

        try {
            reviewList = reviewService.getMyReviewAll(userCode);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("result", "FAIL");
            response.put("reason", "리뷰 조회 실패");
            return ResponseEntity.status(200).body(response);
        }

        response.put("result", "SUCCESS");
        response.put("MyReview", reviewList);
        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity updateReview(@PathVariable Long productId, @RequestBody ReviewSaveReqDto reviewSaveReqDto) {
        Map<String, Object> response = new HashMap<>();
        Bill bill = reviewService.getReviewByProductId(productId);

        try{
            bill = reviewService.updateReview(productId, reviewSaveReqDto, bill);
        } catch (Exception e) {
            e.printStackTrace();

            response.put("result", "FAIL");
            response.put("reason", "리뷰 수정 실패");
            return ResponseEntity.status(200).body(response);
        }

        response.put("result", "SUCCESS");
        response.put("reason", "리뷰 수정 성공");
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity deleteReview(@PathVariable Long productId) {
        Map<String, Object> response = new HashMap<>();

        try {
            reviewService.deleteReview(productId);
        } catch (Exception e) {
            e.printStackTrace();

            response.put("result", "FAIL");
            response.put("reason", "리뷰 삭제 실패");
            return ResponseEntity.status(200).body(response);
        }

        response.put("result", "SUCCESS");
        response.put("reason", "리뷰 삭제 성공");
        return ResponseEntity.status(200).body(response);
    }


}