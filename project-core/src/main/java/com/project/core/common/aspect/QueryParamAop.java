package com.project.core.common.aspect;

import com.project.core.common.SysConstant;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import org.springframework.stereotype.Component;

import com.project.core.mybatis.model.QueryModel;

@Aspect
@Component
public class QueryParamAop {

    @Pointcut("@annotation(com.project.core.common.anaotation.QueryModelParam)")
    private void queryModelHandle() {

    }

    @Around(value = "queryModelHandle()")
    public Object actionAopAround(ProceedingJoinPoint joinPoint)
            throws Throwable {

//		if (pageNum == null || pageNum == 0) {
//			pageNum = SysConstant.SYSDEFAULTROWNUM;
//		}
//		if (pageNo == null || pageNo == 1) {
//			pageNo = 1;
//		}
        Object[] arguments = joinPoint.getArgs();

        for (Object o : arguments) {

            if (o instanceof QueryModel) {
                QueryModel qm = (QueryModel) o;
                qm.reInitCriteria(); // 初始化前端条件
            } else if (o instanceof Integer) {

            }

        }
        return joinPoint.proceed();
    }

    @Before(value = "queryModelHandle()")
    public void beforeAction(JoinPoint joinPoint) {
//		if (pageNum == null || pageNum == 0) {
//			pageNum = SysConstant.SYSDEFAULTROWNUM;
//		}
//		if (pageNo == null || pageNo == 1) {
//			pageNo = 1;
//		}
    }
}
