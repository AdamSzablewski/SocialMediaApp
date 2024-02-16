package com.adamszablewski.SocialMediaApp.interfaces;



import com.adamszablewski.SocialMediaApp.enteties.posts.Upvote;

import java.util.Set;


public interface Likeable {


   Set<Upvote> getLikes();

}
