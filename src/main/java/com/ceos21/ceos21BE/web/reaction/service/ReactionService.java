package com.ceos21.ceos21BE.web.reaction.service;

import com.ceos21.ceos21BE.web.post.dto.request.CreatePostRequest;
import com.ceos21.ceos21BE.web.reaction.dto.request.ReactionRequest;
import com.ceos21.ceos21BE.web.reaction.dto.response.ReactionResponse;
import com.ceos21.ceos21BE.web.reaction.entity.Reaction;

public interface ReactionService {
    public ReactionResponse reactToPost(ReactionRequest request);
}
