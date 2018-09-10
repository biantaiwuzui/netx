package com.netx.ucenter.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceProvider {
    @Autowired
    private ArticleClickHistoryService articleClickHistoryService;

    @Autowired
    private ArticleCollectService articleCollectService;

    @Autowired
    private ArticleLikesService articleLikesService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private LoginHistoryService loginHistoryService;

    @Autowired
    private SystemBlacklistLogService systemBlacklistLogService;

    @Autowired
    private SystemBlacklistService systemBlacklistService;

    @Autowired
    private UserBlacklistService userBlacklistService;

    @Autowired
    private UserContributionService userContributionService;

    @Autowired
    private UserCreditService userCreditService;

    @Autowired
    private UserEducationService userEducationService;

    @Autowired
    private UserIncomeService userIncomeService;

    @Autowired
    private UserInterestService userInterestService;

    @Autowired
    private UserOauthService userOauthService;

    @Autowired
    private UserPayAccountService userPayAccountService;

    @Autowired
    private UserPhotoService userPhotoService;

    @Autowired
    private UserProfessionService professionService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserScoreService userScoreService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserValueService userValueService;

    @Autowired
    private UserVerificationCodeService userVerificationCodeService;

    @Autowired
    private UserVerifyCreditService userVerifyCreditService;

    @Autowired
    private UserVerifyResourceService userVerifyResourceService;

    @Autowired
    private UserVerifyService userVerifyService;

    @Autowired
    private UserWatchService userWatchService;

    public ArticleClickHistoryService getArticleClickHistoryService() {
        return articleClickHistoryService;
    }

    public ArticleCollectService getArticleCollectService() {
        return articleCollectService;
    }

    public ArticleLikesService getArticleLikesService() {
        return articleLikesService;
    }

    public ArticleService getArticleService() {
        return articleService;
    }

    public LoginHistoryService getLoginHistoryService() {
        return loginHistoryService;
    }

    public SystemBlacklistLogService getSystemBlacklistLogService() {
        return systemBlacklistLogService;
    }

    public SystemBlacklistService getSystemBlacklistService() {
        return systemBlacklistService;
    }

    public UserBlacklistService getUserBlacklistService() {
        return userBlacklistService;
    }

    public UserContributionService getUserContributionService() {
        return userContributionService;
    }

    public UserCreditService getUserCreditService() {
        return userCreditService;
    }

    public UserEducationService getUserEducationService() {
        return userEducationService;
    }

    public UserIncomeService getUserIncomeService() {
        return userIncomeService;
    }

    public UserInterestService getUserInterestService() {
        return userInterestService;
    }

    public UserOauthService getUserOauthService() {
        return userOauthService;
    }

    public UserPayAccountService getUserPayAccountService() {
        return userPayAccountService;
    }

    public UserPhotoService getUserPhotoService() {
        return userPhotoService;
    }

    public UserProfessionService getProfessionService() {
        return professionService;
    }

    public UserProfileService getUserProfileService() {
        return userProfileService;
    }

    public UserScoreService getUserScoreService() {
        return userScoreService;
    }

    public UserService getUserService() {
        return userService;
    }

    public UserValueService getUserValueService() {
        return userValueService;
    }

    public UserVerificationCodeService getUserVerificationCodeService() {
        return userVerificationCodeService;
    }

    public UserVerifyCreditService getUserVerifyCreditService() {
        return userVerifyCreditService;
    }

    public UserVerifyResourceService getUserVerifyResourceService() {
        return userVerifyResourceService;
    }

    public UserVerifyService getUserVerifyService() {
        return userVerifyService;
    }

    public UserWatchService getUserWatchService() {
        return userWatchService;
    }
}
