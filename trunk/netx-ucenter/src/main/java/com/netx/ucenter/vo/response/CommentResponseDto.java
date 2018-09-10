package com.netx.ucenter.vo.response;

import java.util.List;

public class CommentResponseDto extends ReplyResponseDto {

    private int replyNum = 0;

    private List<ReplyResponseDto> children;

    public List<ReplyResponseDto> getChildren() {
        return children;
    }

    public void setChildren(List<ReplyResponseDto> children) {
        this.children = children;
        if(children!=null){
            replyNum = children.size();
        }
    }

    public int getReplyNum() {
        return replyNum;
    }

}
