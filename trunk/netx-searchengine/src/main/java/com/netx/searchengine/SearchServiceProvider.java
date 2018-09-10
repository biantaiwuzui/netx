package com.netx.searchengine;

import com.netx.searchengine.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by CloudZou on 2/10/18.
 */
@Service
public class SearchServiceProvider {
    @Autowired
    private SellerSearchService sellerSearchService;

    @Autowired
    private ProductSearchService productSearchService;

    @Autowired
    private FriendSearchService friendSearchService;

    @Autowired
    private SkillSearchService skillSearchService;

    @Autowired
    private WishSearchService wishSearchService;

    @Autowired
    private MeetingSearchService meetingSearchService;

    @Autowired
    private DemandSearchService demandSearchService;

    @Autowired
    private ArticleSearchService articleSearchService;

    @Autowired
    private WorthSearchService worthSearchService;

    public SellerSearchService getSellerSearchService() {
        return sellerSearchService;
    }

    public ProductSearchService getProductSearchService() {
        return productSearchService;
    }

    public FriendSearchService getFriendSearchService() {
        return friendSearchService;
    }

    public SkillSearchService getSkillSearchService() {
        return skillSearchService;
    }

    public WishSearchService getWishSearchService() {
        return wishSearchService;
    }

    public MeetingSearchService getMeetingSearchService() {
        return meetingSearchService;
    }

    public DemandSearchService getDemandSearchService() {
        return demandSearchService;
    }

    public ArticleSearchService getArticleSearchService() {
        return articleSearchService;
    }

    public WorthSearchService getWorthSearchService() {
        return worthSearchService;
    }
}
