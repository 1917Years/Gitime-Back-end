package capstone.gitime.api.common;

import capstone.gitime.domain.memberteam.entity.MemberTeam;
import capstone.gitime.domain.memberteam.service.MemberTeamService;
import capstone.gitime.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class TeamAccessInterceptor implements HandlerInterceptor {

    private final TeamService teamService;
    private final MemberTeamService memberTeamService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException();
        }

        if (authentication.getName().equals("anonymousUser")) {
            response.getWriter().write("No Permission");
            return false;
        }

        Long memberId = Long.valueOf(authentication.getName());
        String teamName = request.getRequestURL().toString().substring(request.getRequestURL().toString().lastIndexOf("dashboard/") + 10);

        memberTeamService.memberAccessToTeam(memberId, teamName);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
