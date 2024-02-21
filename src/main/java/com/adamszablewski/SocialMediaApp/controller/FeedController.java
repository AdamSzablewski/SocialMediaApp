package com.adamszablewski.SocialMediaApp.controller;

import com.adamszablewski.SocialMediaApp.annotations.SecureUserIdResource;
import com.adamszablewski.SocialMediaApp.dtos.PostDto;
import com.adamszablewski.SocialMediaApp.service.FeedService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/feed")
@AllArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping()
    @SecureUserIdResource
    @ResponseBody
    public List<PostDto> getFeedForUser(@RequestParam("userId") long userId,
                                        HttpServletRequest servletRequest){
        return feedService.getFeedForUser(userId);
    }
    @GetMapping("/public")
    @ResponseBody
    public List<PostDto> getPublicFeed(){
        return feedService.getPublicFeed();
    }
}
