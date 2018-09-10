package com.netx.utils.security.rejectflush;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author Runshine
 * @since 2014-11-20
 * @version 1.0.0
 *
 */
public class RejectFlushInterceptor extends HandlerInterceptorAdapter {
    private List<Regulation> regulations;
    private NextStep nextStep;

    public List<Regulation> getRegulations() {
        return regulations;
    }

    public void setRegulations(List<Regulation> regulations) {
        this.regulations = regulations;
    }

    public NextStep getNextStep() {
        return nextStep;
    }

    public void setNextStep(NextStep nextStep) {
        this.nextStep = nextStep;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        for (Regulation r : regulations) {
            RejectProcedureResult reject = r.isReject(request, response);
            if (reject.isReject()) {
                nextStep.doNext(request, response, reject.getBlacklistKey());
                return false;
            } else if (!reject.isContinuate()) {
                break;
            }
        }

        return true;
    }
}
