package com.xuersheng.myProject.security;

import com.xuersheng.myProject.services.RoleServices;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Component
public class ElasticUrlDecisionVoter implements AccessDecisionVoter<FilterInvocation> {

    @Resource
    RoleServices roleServices;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation fi, Collection attributes) {
        assert authentication != null;
        assert fi != null;
        assert attributes != null;
        String path = fi.getHttpRequest().getRequestURI();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            List<Long> roleIds = ((UserDetailsImpl) principal).getUser().getRoleIds();
            return roleServices.hasAction(roleIds, path) ? ACCESS_GRANTED : ACCESS_DENIED;
        }
        return ACCESS_ABSTAIN;
    }
}